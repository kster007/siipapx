package sx.cxp

import grails.rest.RestfulController

class ContraReciboController extends RestfulController{

    static responseFormats = ['json']

    ContraReciboController(){
        super(ContraRecibo)
    }


}
