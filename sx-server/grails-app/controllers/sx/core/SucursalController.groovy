package sx.core

import grails.rest.RestfulController

class SucursalController extends RestfulController{

    static responseFormats = ['json']

    SucursalController(){
        super(Sucursal)
    }

    @Override
    protected List listAllResources(Map params) {
        def query = Sucursal.where {}
        params.sort = params.sort ?:'nombre'
        params.order = params.order ?:'desc'

        if(params.activa){
            query = query.where {activa == params.activa}
        }
        return query.list(params)
    }
}
