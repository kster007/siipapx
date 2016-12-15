package sx.core

import grails.rest.RestfulController

class ProveedorController extends RestfulController{

    static responseFormats = ['json']

    public ProveedorController(){
        super(Proveedor)
    }

    @Override
    protected List listAllResources(Map params) {

        def query = Proveedor.where {}
        params.max = 500
        if(params.tipo){
            query = query.where {tipo == params.tipo}
        }
        if(params.activos){
            query = query.where {activo == params.activos}
        }
        if(params.term){
            def search = params.term + '%'
            query = query.where { nombre =~ search}
            params.max = 50
            return query.list(params)
        }
        return query.list(params)
    }

}
