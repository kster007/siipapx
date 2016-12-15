package sx.cxc

import grails.rest.RestfulController

class CuentaPorCobrarController extends RestfulController{

    static responseFormats = ['json']

    CuentaPorCobrarController() {
        super(CuentaPorCobrar)
    }

}
