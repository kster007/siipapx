package sx.core

import grails.rest.RestfulController

class MarcaController extends RestfulController{

    static responseFormats = ['json']

    public MarcaController(){
        super(Marca)
    }

    @Override
    protected List listAllResources(Map params) {
        params.max = 500
        return super.listAllResources(params)
    }
}
