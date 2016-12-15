package sx.cxp

import grails.transaction.Transactional
import groovy.transform.TupleConstructor
import org.springframework.web.multipart.MultipartFile
import sx.cfdi.ComprobanteFiscal
import sx.cfdi.ComprobanteFiscalService
import static CxpImportadorBuilder.*

@Transactional
class CuentaPorPagarService {

    ComprobanteFiscalService comprobanteFiscalService

    def registrar(MultipartFile file, String tipo){
       return registrar(file.getInputStream(), tipo, file.getOriginalFilename())
    }

    def registrar(File file,String tipo){
        return registrar(file.newInputStream(),tipo,file.getName())
    }

    def registrar(InputStream is, String tipo, String originalName){

        def cfdi = new XmlSlurper().parse(is)
        def UUID = CxpImportadorBuilder.getUUID(cfdi)
        ComprobanteFiscal comprobanteFiscal = ComprobanteFiscal.where {uuid == UUID}.find()
        if(!comprobanteFiscal){
            throw new CuentaPorPagarException(message: "No se ha importado y validado el CFDI: ${originalName}")
        }
        CuentaPorPagar cxp = CuentaPorPagar.where { comprobante == comprobanteFiscal }.find()
        if(!cxp){
            cxp = new CuentaPorPagar(tipo:tipo, comprobante: comprobanteFiscal)
        }
        cxp.comprobante = comprobanteFiscal
        resolverDatosGenerales (cfdi, cxp )
        resolverProveedor(cfdi,cxp)
        resolverMoneda(cfdi,cxp)
        resolverImpuestos(cfdi,cxp)
        resolverRetenciones(cfdi,cxp)

        cxp.comentario = "Importado del archivo $originalName"
        cxp.save failOnError: true, flush:true
    }

}

@TupleConstructor(includeSuperProperties = true)
class CuentaPorPagarException extends  RuntimeException {

    CuentaPorPagar cxp
    String message

}

