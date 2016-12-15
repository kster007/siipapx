package sx.server.integracion

import org.apache.commons.lang.exception.ExceptionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sx.tesoreria.Requisicion
import sx.tesoreria.RequisicionDet

/**
 * Created by rcancino on 27/09/16.
 */
@Component
class ImportadorDeRequisiciones implements Importador{

    @Autowired
    ImportadorDeCuentasPorPagar importadorDeCxP

    def importar(f1){
        return importar(f1,f1)
    }

    def importar(Long sw2){
        logger.info('Importando requisicion ' + sw2)
        String select = QUERY + " where requisicion_id = ? "
        def row = findRegistro(select, [sw2])
        build(row)
    }

    def build(def row){
        def requisicion = Requisicion.where{ sw2 == row.sw2}.find()
        if(!requisicion){
            requisicion = new Requisicion(partidas:[])
        }
        bindData(requisicion,row)
        try{
            importarPartidas(requisicion)
            requisicion.save failOnError:true, flush:true
            return requisicion
        }catch (Exception ex){logger.error(ExceptionUtils.getRootCauseMessage(ex))}
    }

    def importar(Date f1 = new Date(), Date f2 = new Date()){

        logger.info("Importando requisiciones del ${f1.format('dd/MM/yyyy')} al ${f2.format('dd/MM/yyyy')}" )

        List importados = []
        String select = QUERY + " where fecha between date(?) and date(?) "

        leerRegistros(select,[f1,f2]).each { row ->
            logger.info('Importando requisicion: ' + row.sw2)
            build(row)
        }
        def message = "Requisiciones importadas: ${importados.size()}"
        logger.info(message)
        return message
    }

    def importarPartidas(Requisicion requisicion){
        //logger.info("Importando partidas para requisicion ${requisicion.sw2}")
        List partidas = leerRegistros(QUERY_PARTIDAS,[requisicion.sw2])
        if(requisicion.partidas)
            requisicion.partidas.clear()
        else
            requisicion.partidas = []
        partidas.each{ row ->
            logger.info("Importando partida: " + row)
            RequisicionDet det = new  RequisicionDet()
            bindData(det,row)
            if(row.cxp_id){
                det.cxp = importadorDeCxP.importar(row.cxp_id)
            }
            requisicion.addToPartidas(det)
        }
    }

    static String QUERY  = """
        select
        p.nombre,
        p.rfc,
        r.afavor,
        r.origen as entidad,
        r.moneda,
        if(FORMADEPAGO = 0,'TRANSFERENCIA','CHEQUE')  as formaDePago ,
        r.tc as tipoDeCambio,
        r.total,
        r.comentario,
        r.fecha,
        r.fechaDePago,
        r.requisicion_id as sw2
        from sw_trequisicion r join sx_proveedores p on(r.proveedor_id = p.proveedor_id)

    """
    static String QUERY_PARTIDAS ="""
        select
            documento,
            fechad as documentoFecha,
            importe,
            impuesto,
            total,
            total_docto as documentoTotal,
            importe,
            s.nombre as sucursal,
            comentario,
            cxp_id
            from sw_trequisiciondet d join sw_sucursales s on (s.sucursal_id = d.sucursal_id)
            where requisicion_id = ?
    """
}
