package sx.compras

import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode
import sx.core.Producto
import sx.core.Sucursal


@ToString(includes = 'producto,solicitado,importeNeto', includeNames = true, includePackage = false)
@EqualsAndHashCode(includes = 'id')
class CompraDet {

    String id

    Compra compra

    Producto producto

    Sucursal sucursal

    BigDecimal precio = 0.0

    BigDecimal costo = 0.0

    BigDecimal descuento1 = 0.0

    BigDecimal descuento2 = 0.0

    BigDecimal descuento3 = 0.0

    BigDecimal descuento4 = 0.0

    BigDecimal descuentoFinal = 0.0

    BigDecimal factor = 0.0

    BigDecimal importeBruto = 0.0

    BigDecimal importeDescuento = 0.0

    BigDecimal importeNeto = 0.0

    BigDecimal solicitado = 0.0

    Boolean especial = true

    BigDecimal ancho = 0.0

    BigDecimal largo = 0.0

    Date depuracion

    BigDecimal depurado = 0.0

    String comentario

    String sw2

    Date dateCreated

    Date lastUpdated

    static constraints = {
        comentario nullable: true
        depuracion nullable: true
    }

    static mapping = {
        id generator: 'uuid'
    }

    static belongsTo = [compra:Compra]
}
