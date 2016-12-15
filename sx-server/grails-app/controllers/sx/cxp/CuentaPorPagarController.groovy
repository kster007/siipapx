package sx.cxp

import grails.rest.RestfulController
import grails.web.http.HttpHeaders
import groovy.transform.ToString
import org.springframework.web.multipart.MultipartFile
import sx.core.Proveedor

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

class CuentaPorPagarController extends RestfulController{

    static responseFormats = ['json']

    def cuentaPorPagarService

    CuentaPorPagarController(){
        super(CuentaPorPagar)
    }

    def importar(){
        MultipartFile xml=request.getFile('file')
        if(!xml){
            render status: UNPROCESSABLE_ENTITY.value(), text: 'Archivo XML nulo'
            return
        }
        if(xml.isEmpty()){
            render status: UNPROCESSABLE_ENTITY.value(), text: 'Archivo XML está vacio vacio'
            return
        }

        CuentaPorPagar cxp = cuentaPorPagarService.registrar(xml,'GASTOS')

        request.withFormat {
            form multipartForm {
                //log.info message(code: 'default.created.message', args: [classMessageArg, instance.id])
                respond cxp, [status: CREATED, view:'show']
            }
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: cxp.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond cxp, [status: CREATED, view:'show']
            }
        }
    }

    def handleImportacionException(CuentaPorPagarException iex){
        render status: UNPROCESSABLE_ENTITY.value(), text: iex.getMessage()
        return
    }

    def index(CuentaPorPagarFiltro filtro){
        log.info('Buscando con filtro: ' + filtro)
        params.max = Math.min(20, 100)
        def query  = CuentaPorPagar.where {}
        if(filtro.fechaInicial){
            Date inicio = filtro.fechaInicial
            Date fin = filtro.fechaFinal ?: inicio
            query = query.where {fecha >= inicio && fecha <= fin}
        }
        if(filtro.proveedor) {
            query = query.where {proveedor == filtro.proveedor}
        }
        //respond listAllResources(params), model: [("${resourceName}Count".toString()): countResources()]
        respond query.list(params)
    }
}

@ToString(includePackage = false)
class CuentaPorPagarFiltro {
    Date fechaInicial
    Date fechaFinal
    Proveedor proveedor
    Integer registros = 1


    static constraints = {
        fechaFinal nullable: true
        proveedor nullable: true
        registros size:(1..1000)

    }

    String toString(){
        return "$fechaInicial al $fechaFinal ${proveedor?.nombre}"
    }
}
