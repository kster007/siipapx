package sx.siipap

import grails.plugin.springsecurity.annotation.Secured

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class HomeController {

    def index() { 

    }


}
