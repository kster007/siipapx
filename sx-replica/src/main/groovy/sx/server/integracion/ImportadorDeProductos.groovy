package sx.server.integracion

import org.apache.commons.lang.exception.ExceptionUtils
import org.springframework.stereotype.Component
import sx.core.Producto
import sx.core.Proveedor

/**
 * Created by rcancino on 16/08/16.
 */
@Component
class ImportadorDeProductos implements Importador{


    def importar(boolean all = false){
        logger.info('Importando productos' + new Date().format('dd/MM/yyyy HH:mm:ss'))

        def importados = 0
        String query = SELECT //+ all ? "" : "  where year(creado) = 2016"
        if(!all) query+= " where year(creado) = 2016 "
        leerRegistros(query,[]).each { row ->
            def producto = build(row)
            if(producto){
                importados++
                logger.info('Producto importado: '+producto.clave)
            }
        }
        def message = "Productos  importados o actualizados: $importados"
        return message
    }



    def importar(Long id){
        def row = findRegistro(SELECT + " where producto_id = ?", [id])
        build(row)
    }

    def build(def row){
        def producto = Producto.where{ sw2 == row.sw2}.find()
        String tipo = 'actualizado'
        if(!producto){
            producto = new Producto()
            tipo = 'importado'
        }
        bindData(producto,row)
        if(row.proveedor_id){
            producto.proveedorFavorito = Proveedor.where {sw2 == row.proveedor_id}.find()
        }

        try{
            producto = producto.save failOnError:true, flush:true
            logger.info("Producto ${producto.clave} $tipo" )
            return producto
        }catch (Exception ex){
            ex.printStackTrace()
            logger.error(ExceptionUtils.getRootCauseMessage(ex))
        }
    }


    static String SELECT = """
           select
            p.producto_id as sw2,
            p.clave,
            p.descripcion,
            unidad,
            activo,
            kilos,
            gramos,
            caras,
            color,
            acabado,
            presentacion,
            nacional,
            ancho,
            largo,
            delinea,
            precioContado,
            precioCredito,
            fecha_LP as fechaLista,
            modoDeVenta as modoVenta,
            calibre,
            inventariable,
            l.nombre as linea,
            c.nombre as clase,
            m.nombre as marca,
            p.proveedor_id
            from sx_productos p
            join sx_lineas l on(l.LINEA_ID = p.linea_id)
            join sx_marcas m on(m.marca_id = p.marca_id)
            join sx_clases c on(c.clase_id = p.clase_id)
            """

    static String WHERE = """
            where year(creado) = 2016
            """
}

