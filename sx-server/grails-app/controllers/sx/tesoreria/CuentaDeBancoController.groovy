package sx.tesoreria


import grails.rest.*
import grails.converters.*

class CuentaDeBancoController extends RestfulController {
    static responseFormats = ['json', 'xml']
    CuentaDeBancoController() {
        super(CuentaDeBanco)
    }
}
