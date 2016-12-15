package sx.core

import grails.rest.RestfulController

class ClaseController extends RestfulController{

    static responseFormats = ['json']

    public ClaseController(){
        super(Clase)
    }

    @Override
    protected List listAllResources(Map params) {
        params.max = 500
        return super.listAllResources(params)
    }

}
