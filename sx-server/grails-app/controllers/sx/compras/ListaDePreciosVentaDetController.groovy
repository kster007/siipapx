package sx.compras

import grails.rest.RestfulController

class ListaDePreciosVentaDetController extends RestfulController{

    static responseFormats = ['json']

    ListaDePreciosVentaDetController(){
        super(ListaDePreciosDetController)
    }
}
