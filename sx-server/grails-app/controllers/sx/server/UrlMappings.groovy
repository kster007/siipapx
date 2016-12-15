package sx.server

class UrlMappings {

    static mappings = {
        delete "/$controller/$id(.$format)?"(action:"delete")
        get "/$controller(.$format)?"(action:"index")
        get "/$controller/$id(.$format)?"(action:"show")
        post "/$controller(.$format)?"(action:"save")
        put "/$controller/$id(.$format)?"(action:"update")
        patch "/$controller/$id(.$format)?"(action:"patch")

        // Catalogos principales
        "/api/core/sucursales"(resources: "sucursal")
        "/api/core/lineas"(resources: "linea")
        "/api/core/marcas"(resources: "marca")
        "/api/core/clases"(resources: "clase")
        "/api/core/productos"(resources: "producto")
        "/api/core/proveedores"(resources: "proveedor"){
            "/productos"(resources:'proveedorProducto')
        }
        "/api/core/clientes"(resources: "cliente")

        //Contabilidad y finanzas
        "/api/sat/bancos"(resources: "SatBanco")
        "/api/sat/cuentas"(resources:"SatCuenta")

        "/api/contabilidad/cuentas"(resources:"cuentaContable")
        "/api/contabilidad/subtipos"(resources:"subTipoDePoliza")
        "/api/contabilidad/polizas"(resources:"poliza")

        // Tesoreria
        "/api/tesoreria/requisiciones"(resources:"requisicion"){
            "/partidas"(resources:"requisicionDet")
        }

        // CxP
        "/api/cxp/cuentas"(resources: "cuentaPorPagar")
        "/api/cxp/importar"(controller:"cuentaPorPagar", action:"importar")

        //Comprobantes fiscales CFDI's
        "/api/cfdis"(resources: "comprobanteFiscal")
        "/api/cfdis/importar"(controller:"comprobanteFiscal", action:"importar")
        "/api/cfdis/mostrarXml/$id?"(controller:"comprobanteFiscal", action:"mostrarXml")

        // CxP
        "/api/cxc/notas"(resources: "notaDeCredito"){
            "/partidas"(resources:"notaDeCreditoDet")
        }
        "/api/cxp/contrarecibos"(resources: "contraRecibo")

        // Compras
        "/api/compras"(resources: "compra"){
            "/partidas"(resources:"compraDet")
        }
        "/api/listaDePrecios"(resources: "listaDePrecios")
        "/api/compras/recepciones"(resources: "recepcionDeCompra") {
            "/partidas"(resource: "recepcionDeCompraDet")
        }

        // Ventas
        "/api/ventas/listas"(resources: "listaDePreciosVenta")
        "/api/core/ventas"(resources:"venta")
        

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
