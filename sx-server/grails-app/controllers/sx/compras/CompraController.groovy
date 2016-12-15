package sx.compras

import grails.rest.RestfulController
import grails.transaction.Transactional
import grails.web.http.HttpHeaders
import sx.core.Folio

import static org.springframework.http.HttpStatus.CREATED

class CompraController extends RestfulController{


    static responseFormats = ['json']

    CompraController(){
        super(Compra)
    }

    @Transactional
    def save(Compra compraInstance) {
        String ser = 'OFICINAS'
        compraInstance.serie = ser
        compraInstance.folio = Folio.nextFolio('COMPRA',ser)
        compraInstance.validate()
        if (compraInstance.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond compraInstance.errors, view:'create' // STATUS CODE 422
            return
        }

        saveResource compraInstance

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [classMessageArg, compraInstance.id])
                redirect compraInstance
            }
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: compraInstance.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond compraInstance, [status: CREATED, view:'show']
            }
        }
    }
}
