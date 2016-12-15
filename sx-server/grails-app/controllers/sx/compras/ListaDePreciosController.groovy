package sx.compras

import grails.rest.RestfulController

class ListaDePreciosController extends RestfulController{

    static responseFormats = ['json']

    ListaDePreciosController(){
        super(ListaDePrecios)
    }


}
