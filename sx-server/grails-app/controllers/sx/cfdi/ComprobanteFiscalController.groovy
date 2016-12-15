package sx.cfdi

import grails.rest.RestfulController
import grails.web.http.HttpHeaders
import groovy.xml.XmlUtil
import org.springframework.web.multipart.MultipartFile
import sx.cxp.CuentaPorPagar

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

class ComprobanteFiscalController extends RestfulController{

    static responseFormats = ['json']

    ComprobanteFiscalService comprobanteFiscalService

    ComprobanteFiscalController(){
        super(ComprobanteFiscal)
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

        ComprobanteFiscal cfdi = comprobanteFiscalService.importar(xml)

        request.withFormat {
            form multipartForm {
                log.info('Salvando con multipartForm o form')
                //log.info message(code: 'default.created.message', args: [classMessageArg, instance.id])
                respond cfdi, [status: CREATED, view:'show']
            }
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: instance.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond cfdi, [status: CREATED, view:'show']
            }
        }
    }

    def mostrarXml(ComprobanteFiscal cfdi){
        if (cfdi == null) {
            notFound()
            return
        }
        def comprobante = new XmlSlurper().parse(cfdi.getUrl().newInputStream())
        //Map xml = [xml:XmlUtil.serialize(comprobante) ]
        render XmlUtil.serialize(comprobante)
    }

    def handleImportacionException(CfdiDuplicadoException iex){
        render status: UNPROCESSABLE_ENTITY.value(), text: iex.getMessage()
        return
    }


}
