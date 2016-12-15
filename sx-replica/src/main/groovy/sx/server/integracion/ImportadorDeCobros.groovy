package sx.server.integracion

import org.apache.commons.lang.exception.ExceptionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sx.core.Cliente
import sx.cxc.Cobro
import sx.cxc.CobroCheque
import sx.cxc.CobroDeposito
import sx.cxc.CobroTransferencia

/**
 * Created by rcancino on 25/10/16.
 */
@Component
class ImportadorDeCobros implements Importador, SW2Lookup{

    @Autowired
    ImportadorDeClientes importadorDeClientes

    def importar(f1,f2){
        (f1..f2).each{
            importar(it)
        }
    }

    def importar(fecha){
        logger.info("Importando cobros del : ${fecha.format('dd/MM/yyyy')}" )
        String select = QUERY + " where A.fecha = ? and a.tipo_id like ? order by a.creado"
        def rows = leerRegistros(select,[fecha.format('yyyy-MM-dd'),'PAGO%'])
        def importados = 0
        rows.each { row ->
            build(row)
            importados++
        }
        return importados
    }


    def importar(String sw2){
        logger.info('Importando cobro ' + sw2)
        String select = QUERY + " and abono_id = ? "
        def row = findRegistro(select, [sw2])
        if(row){
            build(row)
        }

    }

    def build(def row){
        logger.info('Importando cobro: ' + row)
        def cobro = Cobro.where{ sw2 == row.sw2}.find()
        if(!cobro){
            cobro = new Cobro()
            cobro.sucursal = buscarSucursal(row.sucursal_id)
            Cliente cliente = Cliente.where {sw2 == row.cliente_id}.find()
            if(!cliente){
                cliente = importadorDeClientes.importar(row.cliente_id)
            }
            cobro.cliente = cliente
            registrarFormaDePago(row,cobro)
        }
        bindData(cobro,row)
        try{
            cobro = cobro.save failOnError:true, flush:true
            return cobro
        }catch (Exception ex){
            logger.error(ExceptionUtils.getRootCauseMessage(ex))
        }
    }

    def registrarFormaDePago(def row,Cobro cobro){

        switch (row.tipo_id){
            case 'PAGO_EFE':
                cobro.formaDePago = 'EFECTIVO'
                return cobro
            case 'PAGO_CHE':
                cobro.formaDePago = 'CHEQUE'
                CobroCheque cheque = new CobroCheque()
                bindData(cheque,row)
                cobro.cheque = cheque
                return cobro
            case 'PAGO_DEP':
                if(row.totalTransferencia > 0.0){
                    cobro.formaDePago = 'TRANSFERENCIA'
                    CobroTransferencia transferencia = new CobroTransferencia()
                    bindData(transferencia,row)
                    cobro.transferencia = transferencia
                } else {
                    cobro.formaDePago = 'DEPOSITO'
                    CobroDeposito deposito = new CobroDeposito()
                    bindData(deposito,row)
                    cobro.deposito = deposito
                }
                break
            case 'PAGO_TAR':
                cobro.formaDePago = 'TARJETA'
                break
            default:
                cobro.formaDePago = row.tipo_id

        }
        return cobro
    }


    static String QUERY = """
    SELECT
        A.TIPO_ID,
        A.ABONO_ID as sw2,
        A.CLIENTE_ID,
        A.clave,
        A.nombre,
        c.rfc,
        A.fecha,
        A.origen,
        A.importe,
        A.impuesto,
        A.total,
        A.moneda,
        A.tc as tipoDeCambio,
        A.anticipo,
        A.enviado,
        A.SAF as saldoAFavor,
        A.diferencia,
        A.DIFERENCIA_FECHA as direrenciaFecha,
        A.COBRADOR_ID,
        A.SUCURSAL_ID,
        A.comentario,
        A.numero,
        A.postFechado,
        A.VTO as vencimiento ,
        A.CUENTA_HABIENTE as emisor,
        A.CUENTA_CLIENTE as cuenta ,
        A.BANCO as banco,
        A.folio,
        A.cheque as totalCheque,
        A.efectivo as totalEfectivo,
        A.transferencia as totalTransferencia,
        0.0 AS totalTarjeta,
        A.fecha_deposito as fechaDeposito,
        A.referencia,
        A.AUTO_TARJETA_BANCO,
        A.comision_tarjeta,
        A.tarjeta_id,
        A.creado
        FROM sx_cxc_abonos A join sx_clientes c on a.cliente_id = c.cliente_id
    """


}
