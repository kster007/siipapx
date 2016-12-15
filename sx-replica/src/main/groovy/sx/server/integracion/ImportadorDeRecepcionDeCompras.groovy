package sx.server.integracion

import org.apache.commons.lang.exception.ExceptionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sx.compras.Compra
import sx.compras.CompraDet
import sx.compras.RecepcionDeCompra
import sx.compras.RecepcionDeCompraDet
import sx.core.Producto
import sx.core.Proveedor
import sx.core.Sucursal

/**
 * Created by rcancino on 17/10/16.
 */
@Component
class ImportadorDeRecepcionDeCompras implements Importador, SW2Lookup {

    @Autowired
    ImportadorDeCompras importadorDeCompras

    def importar(f1){
        return importar(f1,f1)
    }

    def importar(Date ini, Date fin){
        logger.info("Importando recepciones de compra  del : ${ini.format('dd/MM/yyyy')} al ${ini.format('dd/MM/yyyy')}" )
        def ids = leerRegistros("select id from SX_ENTRADA_COMPRAS where date(fecha) between ? and ? ",[ini,fin])
        ids.each { r ->
            importar(r.id)
        }
    }

    def importar(String sw2){
        logger.info('Importando recepcion ' + sw2)
        String select = QUERY + " where id = ? "
        def row = findRegistro(select, [sw2])
        build(row)
    }


    def build(def row){
        def recepcion = RecepcionDeCompra.where{ sw2 == row.sw2}.find()
        if(!recepcion){
            recepcion = new RecepcionDeCompra()
        }
        bindData(recepcion,row)
        recepcion.compra = buscarCompra(row.compra_id)
        recepcion.sucursal = buscarSucursal(row.sucursal_id)
        importarPartidas(recepcion)
        try{
            recepcion = recepcion.save failOnError:true, flush:true
            return recepcion
        }catch (Exception ex){
            logger.error(ExceptionUtils.getRootCauseMessage(ex))
        }
    }

    def importarPartidas(RecepcionDeCompra recepcion){
        List partidas = leerRegistros(QUERY_PARTIDAS,[recepcion.sw2])
        recepcion.partidas.clear()
        partidas.each{ row ->
            RecepcionDeCompraDet det = new  RecepcionDeCompraDet()
            det.producto = buscarProducto(row.producto_id)
            det.compraUnitaria = buscarCompraDet(row.compradet_id)
            bindData(det,row)
            recepcion.addToPartidas(det)
        }
    }

    def buscarCompra(def siipapId){

        Compra compra = Compra.where {sw2 == siipapId}.find()
        if (!compra) {
           importadorDeCompras.importar(siipapId)
            compra = Compra.where {sw2 == siipapId}.find()
        }
        assert compra, 'No se ha importado la compra  $siipapId'
        return compra
    }

    def buscarCompraDet(def siipapId){
        CompraDet compraDet = CompraDet.where {sw2 == siipapId}.find()
        assert compraDet, "No se ha importado la compra unitaria $siipapId"
        return compraDet
    }



    static String QUERY  = """
        select
            e.id as sw2,
            e.fecha,
            e.remision,
            e.compra_id,
            e.sucursal_id,
            e.documento as folio,
            e.comentario
        from SX_ENTRADA_COMPRAS e


        """
    static String QUERY_PARTIDAS ="""
        select
            e.inventario_id as sw2,
            e.compradet_id,
            e.producto_id,
            e.unidad_id as unidad,
            e.cantidad,
            e.factoru as factor,
            e.kilos,
            e.costo,
            e.costop as costoPromedio,
            e.costou as costoUltimo,
            e.*
            from sx_inventario_com e
            where recepcion_id = ?
        """
}
