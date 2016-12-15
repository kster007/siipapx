package sx.tesoreria

import grails.rest.RestfulController

class RequisicionController extends  RestfulController{

    static responseFormats = ['json']

    RequisicionController(){
       super(Requisicion)
    }

    @Override
    protected List listAllResources(Map params) {
        def query = Requisicion.where {}
        params.max = 20
        if(params.entidad){
            query = query.where {entidad == params.entidad}
        }
        if(params.term){

            def search = params.term + '%'
            query = query.where { nombre =~ search || afavor =~search}
            params.max = 50
            return query.list(params)
        }
        return query.list(params)
    }


}
