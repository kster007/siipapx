package sx.compras

import grails.rest.RestfulController
import grails.web.http.HttpHeaders
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import sx.core.Folio

class ListaDePreciosVentaController extends  RestfulController{

    static responseFormats = ['json']

    ListaDePreciosVentaController(){
        super(ListaDePreciosVenta)
    }

    @Transactional
    def save(ListaDePreciosVenta listaInstance) {
        listaInstance.folio = Folio.nextFolio('LISTA_DE_PRECIOS','COMPRA')
        listaInstance.validate()
        if (listaInstance.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond listaInstance.errors, view:'create' // STATUS CODE 422
            return
        }
        saveResource listaInstance
        log.info('Lista de precios registrada... ' + listaInstance.id)
        respond listaInstance, [status: CREATED, view:'listaDePreciosVenta.show']
    }

    @Override
    protected List listAllResources(Map params) {
        def query = ListaDePreciosVenta.where {}
        params.sort = params.sort ?:'folio'
        params.order = params.order ?:'desc'

        if(params.term){
            def search = '%' + params.term + '%'
            query = query.where { descripcion =~ search }
        }
        return query.list(params)
    }
}
