package sx.cfdi

import grails.transaction.Transactional
import groovy.util.slurpersupport.GPathResult
import groovy.xml.XmlUtil
import org.springframework.web.multipart.MultipartFile


@Transactional
class ComprobanteFiscalService {

    def grailsApplication


    ComprobanteFiscal importar(MultipartFile multipartFile){
        return importar(multipartFile.getInputStream(),null,multipartFile.getOriginalFilename())
    }

    ComprobanteFiscal importar(File file){
        return importar(file.newInputStream(), null, file.getName())
    }

    ComprobanteFiscal importar(InputStream io, String name, String originalName) {

        GPathResult cfdi = new XmlSlurper().parse(io)
        String UUID = getUUID(cfdi)
        ComprobanteFiscal found = ComprobanteFiscal.where {uuid == UUID}.find()
        if(found){
            throw new CfdiDuplicadoException(found)
        }
        ComprobanteFiscal comprobanteFiscal = buildComprobante(cfdi)

        comprobanteFiscal.fileName = name ?: "${comprobanteFiscal.receptorRfc}_${comprobanteFiscal.uuid.split('-').last()}.xml"
        comprobanteFiscal.originalName = originalName?: comprobanteFiscal.fileName
        saveXml(comprobanteFiscal,XmlUtil.serialize(cfdi).getBytes('UTF-8'))
        comprobanteFiscal.save failOnError:true
        return comprobanteFiscal


    }

    String getUUID(GPathResult cfdi){
        def timbre = cfdi.'**'.find{ node -> node.name() == 'TimbreFiscalDigital' }
        if(!timbre) {
            throw new ImporatcionDeComprobanteException(message: 'Comprobante sin Timbre Fiscal Digital')
        }
        return timbre.@UUID
    }

    ComprobanteFiscal buildComprobante(GPathResult cfdi, ComprobanteFiscal comprobante = new ComprobanteFiscal()){

        def timbre = cfdi.'**'.find{ node -> node.name() == 'TimbreFiscalDigital' }
        if(!timbre) {
            throw new ImporatcionDeComprobanteException(message: 'Comprobante sin Timbre Fiscal Digital')
        }

        def emisor = cfdi.'**'.find{ node -> node.name() == 'Emisor'}
        if(!emisor)
            throw new ImporatcionDeComprobanteException(message: 'Comprobante incorrecto sin Emisor')

        def receptor = cfdi.'**'.find{ node -> node.name() == 'Receptor'}
        if(!receptor)
            throw new ImporatcionDeComprobanteException(message: 'Comprobante incorrecto no tiene Receptor')

        comprobante.uuid = timbre.@UUID
        comprobante.fecha = Date.parse("yyyy-MM-dd'T'HH:mm:s", cfdi.@fecha.toString())
        comprobante.emisorRfc = emisor.@rfc
        comprobante.emisor = emisor.@nombre
        comprobante.receptorRfc = receptor.@rfc
        comprobante.serie = cfdi.@serie
        comprobante.folio = cfdi.@folio
        comprobante.tipoDeComprobante = cfdi.@tipoDeComprobante
        comprobante.formaDePago = cfdi.@formaDePago
        comprobante.metodoDePago = cfdi.@metodoDePago.toString()
        comprobante.total = cfdi.@total.toBigDecimal()
        return comprobante
    }



    void saveXml(ComprobanteFiscal cfdi, byte[] data){
        def date = cfdi.fecha
        String year = date[Calendar.YEAR]
        String month = date[Calendar.MONTH]+1
        String day = date[Calendar.DATE]
        def sx = grailsApplication.config.sx
        if(!sx )
            throw new ImporatcionDeComprobanteException(message: 'No se ha configurado la ruta para guardar los Comprobantes fiscales digitales')
        def cfdiRootDir = new File(sx.cfdi.dirPath)
        final FileTreeBuilder treeBuilder = new FileTreeBuilder(cfdiRootDir)
        treeBuilder{
            dir(cfdi.tipoDeComprobante){
                dir(year){
                    dir(month){
                        dir(day){
                            File res = file(cfdi.fileName) {
                                setBytes(data)
                            }
                            cfdi.url = res.toURI().toURL()
                        }
                    }
                }
            }
        }
    }
}

 class ImporatcionDeComprobanteException extends RuntimeException {

     String message
     ComprobanteFiscal cfdi


 }

class CfdiDuplicadoException extends ImporatcionDeComprobanteException{

    public CfdiDuplicadoException(ComprobanteFiscal cfdi){
        message = " El CFDI con UUID: $cfdi.uuid ya se ha importado con el folio (id): ${cfdi.id} y pertenece al emisor ${cfdi.emisorRfc}"
        cfdi = cfdi
    }
}
