package sx.tesoreria

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import sx.cxp.CuentaPorPagar

@ToString(excludes ='id,version,dateCreated,lastUpdated,sw2, requisicion',includeNames=true,includePackage=false)
@EqualsAndHashCode
class RequisicionDet {

    String documento

    Date documentoFecha

    BigDecimal documentoTotal = 0.0

    CuentaPorPagar cxp

    BigDecimal importe = 0.0

    BigDecimal impuesto = 0.0

    BigDecimal retencionIva = 0

    BigDecimal retencionIsr = 0

    BigDecimal total = 0.0

    String sucursal

    String comentario

    Date dateCreated
    Date lastUpdated

    static belongsTo = [requisicion:Requisicion]

    static constraints = {
        documento nullable:true, maxSize:100
        documentoFecha nullable:true
        cxp nullable:true
        comentario nullable:true
    }

    static mapping ={
        ducumentoFecha type:'date'
    }

    def actualizarImportes(){
        //total=importe+(impuestos-retencionFlete-retencionHonorarios-retencionISR)
    }

    def beforeUpdate(){
        actualizarImportes()
    }

    def beforeInsert(){
        actualizarImportes()
    }

}
