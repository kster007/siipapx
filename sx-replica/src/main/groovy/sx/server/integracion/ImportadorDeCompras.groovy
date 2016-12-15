package sx.server.integracion

import groovy.sql.Sql
import org.apache.commons.lang.exception.ExceptionUtils
import org.springframework.beans.factory.annotation.Autowire
import org.springframework.beans.factory.annotation.Autowired

//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import sx.compras.Compra
import sx.compras.CompraDet
import sx.core.Producto
import sx.core.Proveedor
import sx.core.Sucursal

/**
 * Created by rcancino on 15/08/16.
 */
@Component
class ImportadorDeCompras implements  Importador, SW2Lookup{

    @Autowired
    ImportadorDeProductos importadorDeProductos

    def importar(f1){
        return importar(f1,f1)
    }

    def importar(Date ini, Date fin){
        logger.info("Importando compras  del : ${ini.format('dd/MM/yyyy')} al ${ini.format('dd/MM/yyyy')}" )
        def ids = leerRegistros("select compra_id from SX_COMPRAS2 where date(fecha) between ? and ? ",[ini,fin])
        ids.each { row ->
            importar(row.compra_id)
        }
    }

    def importar(String sw2){
        logger.info('Importando compra ' + sw2)
        String select = QUERY + " where compra_id = ? "
        def row = findRegistro(select, [sw2])
        build(row)
    }

    def build(def row){
        def compra = Compra.where{ sw2 == row.sw2}.find()
        if(!compra){
            compra = new Compra()
        }
        bindData(compra,row)
        try{

            compra.sucursal = buscarSucursal( row.sucursal_id)
            compra.serie = compra.sucursal.nombre
            compra.proveedor = buscarProveedor(row.proveedor_id)
            importarPartidas(compra)
            compra.save failOnError:true, flush:true
        }catch (Exception ex){
            //ex.printStackTrace()
            logger.error(ExceptionUtils.getRootCauseMessage(ex))
        }
    }

    def importarPartidas(Compra compra){
        logger.info("Importando partidas para compra ${compra.folio}")
        validarProductos(compra)
        List partidas = leerRegistros(QUERY_PARTIDAS,[compra.sw2])

        partidas.each{ row ->
            //logger.info("Importando partida: " + row)
            CompraDet det = new  CompraDet()
            det.producto = buscarProducto(row.producto_id)
            det.sucursal = buscarSucursal( row.sucursal_id)
            bindData(det,row)
            def existente = compra.partidas.find{ it.sw2 == row.sw2}
            if(existente){
                println 'Partida existente solo se actualiza....ID: ' + existente.id
                bindData(existente,det,['id','version'])
            } else {
                compra.addToPartidas(det)
            }

        }
    }

    def validarProductos(Compra compra){
        def select ="select distinct producto_id from sx_compras2_det where compra_id = ?"
        leerRegistros(select,[compra.sw2]).each{  row ->
            def found = Producto.where {sw2 == row.producto_id}
            if (!found) {
                importadorDeProductos.importar(row.producto_id)
            }
        }
    }

    static String QUERY = """
      select
        compra_id as sw2,
        comentario,
        depuracion,
        entrega,
        fecha,
        folio,
        importe_bruto as importeBruto,
        importe_desc as importeDescuento,
        importe_neto as importeNeto,
        total,
        pendiente,
        impuestos,
        sucursal_id,
        proveedor_id,
        nombre,
        consolidada,
        especial,
        dscto_especial as descuentoEspecial
        from sx_compras2
    """

    static String QUERY_PARTIDAS ="""
        select
            COMPRADET_ID as sw2,
            producto_id,
            clave,
            sucursal_id,
            comentario,
            costo,
            depuracion,
            precio,
            costo,
            desc1 as descuento1,
            desc2 as descuento2,
            desc2 as descuento3,
            desc2 as descuento4,
            desc2 as descuento5,
            desc2 as descuento6,
            descf as descuentoFinal,
            factor,
            importe_bruto as importeBruto,
            importe_neto as importeNeto,
            solicitado,
            especial,
            ancho,
            largo,
            depuracion,
            comentario
         from sx_compras2_det where compra_id = ?
    """
}
