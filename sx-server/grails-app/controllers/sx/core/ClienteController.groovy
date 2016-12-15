package sx.core

import grails.rest.RestfulController

class ClienteController extends RestfulController{

    static responseFormats = ['json']

    ClienteController(){
        super(Cliente)
    }

    @Override
    protected List listAllResources(Map params) {
        def query = Cliente.where {}
        params.sort = params.sort ?:'lastUpdated'
        params.order = params.order ?:'desc'

        if(params.term){
            def search = '%' + params.term + '%'
            query = query.where { clave =~ search || nombre =~ search}
        }
        if(params.activo){
            query = query.where { activo == params.activo}
        }

        return query.list(params)
    }
}
