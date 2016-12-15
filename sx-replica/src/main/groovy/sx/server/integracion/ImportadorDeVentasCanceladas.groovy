package sx.server.integracion

import org.apache.commons.lang.exception.ExceptionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sx.core.Cliente
import sx.core.Venta
import sx.core.VentaCancelada
import sx.cxc.Cobro

/**
 * Created by rcancino on 26/10/16.
 */
@Component
class ImportadorDeVentasCanceladas implements Importador, SW2Lookup {

    @Autowired
    ImportadorDeVentas importadorDeVentas

    def importar(f1,f2){
        (f1..f2).each{
            importar(it)
        }
    }

    def importar(fecha){
        logger.info("Importando ventas canceladas : ${fecha.format('dd/MM/yyyy')}" )
        String select = QUERY + " where date(c.CREADO) = ? and v.tipo = 'FAC' "
        def ids = leerRegistros(select,[fecha.format('yyyy-MM-dd')])
        def importados = 0
        ids.each { row ->
            build(row)
            importados++
        }
        return importados
    }


    def importar(String sw2){
        String select = QUERY + " where c.id = ? and v.tipo = 'FAC' "
        def row = findRegistro(select, [sw2])
        build(row)
    }

    def build(def row){
        logger.info('Importando venta cancelada: ' + row)
        def cancelada = VentaCancelada.where{ sw2 == row.sw2}.find()
        if(!cancelada){
            cancelada = new VentaCancelada()
            bindData(cancelada,row)
            Venta venta = Venta.where {sw2 == row.venta_id}.find()
            if(!venta){
                venta = importadorDeVentas.importar(row.venta_id)
            }
            assert venta, 'No se ha importado la venta: ' + row.venta_id
            cancelada.venta = venta
            try{
                cancelada = cancelada.save failOnError:true, flush:true
                return cancelada
            }catch (Exception ex){
                logger.error(ExceptionUtils.getRootCauseMessage(ex))
            }
        }
    }

    static QUERY = """
    SELECT
        c.fecha,
        c.creado_userid as creadoUserid,
        c.comentario,
        c.id as sw2,
        c.cargo_id as venta_id,
        C.AUT_ID as autorizacion
    FROM sx_cxc_cargos_cancelados c
    join sx_ventas v on(v.cargo_id=c.cargo_id)
    """
}
