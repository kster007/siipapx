package sx.compras

import sx.core.Producto

class RecepcionDeCompraDet {

    String id

    CompraDet compraUnitaria

    Producto producto

    String unidad

    BigDecimal factor

    BigDecimal cantidad

    BigDecimal kilos = 0.0

    BigDecimal costo = 0.0

    BigDecimal costoPromedio = 0.0

    BigDecimal costoUltimo = 0.0

    BigDecimal analizado = 0

    String sw2

    Date dateCreated
    Date lastUpdated

    static constraints = {
        sw2 nullable:true
    }

    static mapping = {
        id generator:'uuid'

    }

    static belongsTo = [recepcion:RecepcionDeCompra]
}
