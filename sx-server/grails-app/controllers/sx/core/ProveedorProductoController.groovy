package sx.core

import grails.rest.RestfulController

class ProveedorProductoController extends RestfulController{

    static responseFormats = ['json']

    ProveedorProductoController(){
        super(ProveedorProducto)
    }

    @Override
    protected List listAllResources(Map params) {
        def query = ProveedorProducto.where {}
        if(params.proveedorId){
            //println 'Buscando productos del proveedor ' + params.proveedorId

            query = query.where{proveedor.id == params.proveedorId}
            return query.list()
        }
        return query.list(params)
    }


}
