package sx.core

import grails.rest.RestfulController

class LineaController extends RestfulController{

    static responseFormats = ['json']

    LineaController() {
        super(Linea)
    }

    protected List listAllResources(Map params) {
        params.max = 500
        resource.list()
    }



}
