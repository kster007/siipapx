package sx.server.integracion

import org.springframework.stereotype.Component
import sx.core.Direccion
import sx.core.Producto
import sx.core.Proveedor
import sx.core.ProveedorProducto

/**
 * Created by rcancino on 06/09/16.
 */
@Component
class ImportadorDeProveedores implements Importador{

    def importar(tipo = 'COMPRAS'){

        logger.info('Importando proveedores '+'tipo ' + new Date().format('dd/MM/yyyy HH:mm:ss'))

        String query = tipo == 'COMPRAS' ? QUERY_COMPRAS : QUERY_GASTOS
        int importados = 0

        leerRegistros(query,[]).each { row ->

            def proveedor = Proveedor.findByClave(row.clave)
            if(!proveedor){
                proveedor = new Proveedor(row)
            }
            proveedor.rfc = proveedor.rfc ?:'XAXX010101000'
            proveedor.direccion = resolveDireccion(row)
            proveedor.sw2 = row.sw2
            proveedor.save flush:true

            importados++
            logger.info("Proveedor importado: $row.clave (${row.sw2})")
        }

        def message = "Proveedores iportados importadas: $importados"
        return message
    }

    def resolveDireccion(row){
        return new Direccion(
                calle:row.calle,
                numeroInterior: row.numeroInt,
                numeroExterior: row.numero,
                colonia: row.colonia,
                municipio: row.municipio,
                estado: row.estado,
                pais: row.pais,
                codigoPostal: row.codigoPostal
            )
    }

    def importarProveedorProductos(){
        String query = "select * from sx_proveedor_productos order by proveedor_id"
        leerRegistros(query, []).each {  row ->
            def proveedor = Proveedor.findBySw2(row.proveedor_id)
            def producto = Producto.findBySw2(row.producto_id)
            if(proveedor && producto){
                ProveedorProducto provProd = ProveedorProducto.where {proveedor == proveedor && producto == producto}.find()
                if(!provProd){
                    provProd = new ProveedorProducto(proveedor:proveedor, producto:producto)
                }
                provProd.claveProveedor = row.claveprov
                provProd.codigoProveedor = row.codigoprov
                provProd.descripcionProveedor = row.descriprov
                provProd.paqueteTarima = row.paqtarima
                provProd.piezaPaquete = row.piezapaq
                provProd.save flush: true
            }
        }
        def message = "Productos por proveedor importados"
        return message
    }

    static String QUERY_COMPRAS = """
        select nombre,clave,rfc, proveedor_id as sw2,
        activo,'COMPRAS' as tipo,telefono1,telefono2,telefono3,calle,numero,numeroint,delmpo as municipio,cp as codigoPostal,colonia,estado,pais,email1,email2
        from sx_proveedores order by clave desc
    """
    static String QUERY_GASTOS = """
        select nombre,clave,rfc,activo,'COMPRAS' as tipo,telefono1,telefono2,telefono3,calle,numero,numeroint,delmpo,colonia,estado,pais,email1,email2
        from sx_proveedores
    """
}
