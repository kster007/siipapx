package sx.siipap

import grails.plugin.springsecurity.annotation.Secured

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class TesoreriaController {

    def index() {
        session.modulo = 'SX-TESORERIA'
        render view: 'index'
    }
}
