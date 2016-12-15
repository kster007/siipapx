package sx.server.integracion

import org.apache.commons.lang.exception.ExceptionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sx.cxp.ContraRecibo
import sx.cxp.ContraReciboDet

/**
 * Created by rcancino on 04/11/16.
 */
@Component
class ImportadorDeContraRecibos implements Importador, SW2Lookup {

    @Autowired
    ImportadorDeCuentasPorPagar importadorDeCuentasPorPagar

    def importar(Date ini, Date fin){

    }

    def importar(fecha){
        logger.info("Importando contra recibos del : ${fecha.format('dd/MM/yyyy')}" )
        def ids = leerRegistros("select recibo_id from SX_CXP_RECIBOS where fecha = ? ",[fecha.format('yyyy-MM-dd')])
        logger.info('Registros: ' + ids.size())
        ids.each { r ->
            importar(r.recibo_id)
        }
    }

    def importar(Long sw2){
        logger.info('Importando contrare recibo ' + sw2)
        String select = QUERY + " where recibo_id = ? "
        def row = findRegistro(select, [sw2])
        build(row)
    }


    def build(def row){
        def recibo = ContraRecibo.where{ sw2 == row.sw2}.find()
        if(!recibo){
            try{
                recibo = new ContraRecibo()
                bindData(recibo,row)
                recibo.proveedor = buscarProveedor(row.proveedor_id)
                importarPartidas(recibo)
                recibo = recibo.save failOnError:true, flush:true
                return recibo
            }catch (Exception ex){
                logger.error(ExceptionUtils.getRootCauseMessage(ex))
            }
        }

    }

    def importarPartidas(ContraRecibo recibo){
        List partidas = leerRegistros(QUERY_PARTIDAS,[recibo.sw2])
        partidas.each{ row ->
            ContraReciboDet det = new  ContraReciboDet()
            bindData(det,row)
            recibo.addToPartidas(det)
        }
    }





    static String QUERY = """
        select
            c.recibo_id as sw2,
            c.fecha,
            c.total,
            c.proveedor_id,
            c.comentario,
            c.*
            from SX_CXP_RECIBOS c
    """

    static String QUERY_PARTIDAS = """
        select
        c.recibodet_id as sw2,
        c.recibodet_id as folio,
        c.documento,
        c.fecha,
        c.tc,
        c.total,
        c.moneda,
        c.tipo,
        c.vto as vencimiento,
        c.requisicion_det as requisicionDet
        from SX_CXP_RECIBOS_DET   c
        where c.recibo_id = ?
    """
}
