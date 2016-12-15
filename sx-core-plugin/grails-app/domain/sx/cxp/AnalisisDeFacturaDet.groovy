package sx.cxp

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import sx.compras.RecepcionDeCompraDet

@ToString(excludes = ['id,version,sw2,dateCreated,lastUpdated'],includeNames=true,includePackage=false)
@EqualsAndHashCode(includeFields = true,includes = ['id'])
class AnalisisDeFacturaDet {

    String id

    RecepcionDeCompraDet entrada

    BigDecimal cantidad = 0.0

    BigDecimal precio = 0.0

    BigDecimal desc1 = 0.0

    BigDecimal desc2 = 0.0

    BigDecimal desc3 = 0.0

    BigDecimal desc4 = 0.0

    BigDecimal desc5 = 0.0

    BigDecimal desc6 = 0.0

    BigDecimal descuentof = 0.0

    BigDecimal costo = 0.0

    BigDecimal neto = 0.0

    BigDecimal importe = 0.0

    Long sw2

    Date dateCreated
    Date lastUpdated

    static belongsTo = [analisis: AnalisisDeFactura]

    static constraints = {
        desc1 scale:4
        desc2 scale:4
        desc3 scale:4
        desc4 scale:4
        desc5 scale:4
        desc6 scale:4
        descuentof scale:4
        costo scale:6
        neto scale:6
        sw2 nullable:true
    }

    static mapping = {
        id generator:'uuid'
    }
}
