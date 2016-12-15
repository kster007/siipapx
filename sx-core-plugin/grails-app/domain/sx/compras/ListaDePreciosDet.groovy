package sx.compras

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import sx.core.Producto

@ToString(includeNames=true,includePackage=false, excludes = ['lastUpdated', 'dateCreated','id','version','lista'])
@EqualsAndHashCode(includeFields = true,includes = ['producto'])
class ListaDePreciosDet {

    Producto producto

    String clave

    String descripcion

    String unidad

    String moneda  = 'MXN'

    BigDecimal precio = 0.0

    BigDecimal neto = 0.0

    BigDecimal descuento1 = 0.0

    BigDecimal descuento2 = 0.0

    BigDecimal descuento3 = 0.0

    BigDecimal descuento4 = 0.0

    BigDecimal descuento5 = 0.0

    BigDecimal descuento6 = 0.0

    BigDecimal descuentoFinanciero = 0.0

    BigDecimal cargo1 = 0.0

    BigDecimal cargo2 = 0.0

    BigDecimal precioAnterior = 0.0

    Date dateCreated

    Date lastUpdated


    static constraints = {
        clave maxSize:50
        moneda maxSize:10
        unidad maxSize:20
    }

    static belongsTo =[lista:ListaDePrecios]




}
