package sx.server.integracion

import org.apache.commons.lang.exception.ExceptionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import sx.tesoreria.*

/**
 * Created by rcancino on 25/10/16.
 */
@Component
class ImportadorDeMovimientosDeCuenta implements Importador, SW2Lookup{

    

    def importar(f1,f2){
        (f1..f2).each{
            importar(it)
        }
    }

    def importar(Date fecha){
        logger.info("Importando movimientos de cuenta del : ${fecha.format('dd/MM/yyyy')}" )
        String select = QUERY + " WHERE FECHA BETWEEN ? AND ?  AND IMPORTE >0 AND ORIGEN<>? order by creado"
        def rows = leerRegistros(select,[fecha.format('yyyy-MM-dd'),fecha.format('yyyy-MM-dd'),'TESORERIA'])
        def importados = 0
        rows.each { row ->
            build(row)
            importados++
        }
        return importados
    }


    def importar(Long sw2){
        String select = QUERY + " where CARGOABONO_ID = ? "
        def row = findRegistro(select, [sw2])
        build(row)

    }

    def build(def row){
        logger.info('Importando movimiento: ' + row)
        println 'Importando: ' + row
        def movimiento = MovimientoDeCuenta.where{ sw2 == row.sw2}.find()
        if(!movimiento){
            movimiento = new MovimientoDeCuenta()
            movimiento.sucursal = buscarSucursal(row.sucursal_id)
            movimiento.cuenta = buscarCuentaDeBanco(row.cuenta_id)
        }
        bindData(movimiento,row)
        if(row.depositoTransferencia){
            movimiento.formaDePago = row.depositoTransferencia
        }
        else if( row.ficha) {
            movimiento.formaDePago = 'FICHA'
        }
        else if (row.corteDeTarjeta) {
            movimiento.formaDePago = 'TARJETA'
        }
        else {
            movimiento.formaDePago 'NA'
        }
        try{
            movimiento = movimiento.save failOnError:true, flush:true
            return movimiento
        }catch (Exception ex){
            ex.printStackTrace()
            logger.error(ExceptionUtils.getRootCauseMessage(ex))
        }
    }

    

    static String QUERY = """
        SELECT 
            CARGOABONO_ID as sw2,
            afavor,
            sucursal_id,        
            fecha,
            FORMAPAGO as formaDePago,
            ORIGEN as origen,
            referencia,
            cuenta_id,
            importe,
            moneda,
            TC as tipoDeCambio,
            comentario,
            comentario2,
            creado,
            FECHA_DEPOSITO as fechaDeposito,
            'COBRO' AS concepto,
            (SELECT (CASE WHEN X.TRANSFERENCIA>0 THEN 'TRANSFERENCIA' ELSE 'DEPOSITO' END) FROM sx_cxc_abonos X WHERE X.ABONO_ID=B.PAGO_ID )  as depositoTransferencia,
            (SELECT X.TIPO_FICHA  FROM sx_fichas X WHERE X.CARGOABONO_ID=B.CARGOABONO_ID) AS ficha,
            (SELECT B.REFERENCIA FROM sx_corte_tarjetas_aplicaciones X WHERE X.CARGOABONO_ID=B.CARGOABONO_ID)  as corteDeTarjeta
        FROM sw_bcargoabono B 
    """
//WHERE FECHA BETWEEN '2016/10/01' AND '2016/10/31'  AND IMPORTE >0 AND ORIGEN<>'TESORERIA'

}
