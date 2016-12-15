package sx.compras

import grails.rest.RestfulController

class ListaDePreciosDetController extends  RestfulController{

    static responseFormats = ['json']

    ListaDePreciosDetController(){
        super(ListaDePreciosDet)
    }
}
