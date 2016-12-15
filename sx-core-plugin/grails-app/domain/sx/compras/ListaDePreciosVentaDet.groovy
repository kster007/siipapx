package sx.compras

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import sx.core.Producto

@ToString(includeNames=true,includePackage=false, excludes = ['lastUpdated', 'dateCreated','id','version'])
@EqualsAndHashCode(includeFields = true,includes = ['producto'])
class ListaDePreciosVentaDet {

    String id
    
    Producto producto

    String clave

    String descripcion

    BigDecimal precioContado = 0.0

    BigDecimal precioCredito = 0.0
    
    BigDecimal precioAnteriorContado = 0.0
    
    BigDecimal precioAnteriorCredito = 0.0
    
    BigDecimal costo = 0.0
    
    BigDecimal costoUltimo = 0.0
    
    BigDecimal incremento = 0.0
    
    BigDecimal factorContado = 0.0
    
    BigDecimal factorCredito = 0.0

    String proveedorClave

    String proveedorNombre

    Date dateCreated

    Date lastUpdated

    static constraints = {
        proveedorClave nullable:true
        proveedorNombre nullable:true
        descripcion nullable:true
    }

    static mapping ={
        id generator:'uuid'

    }

    static belongsTo =[lista:ListaDePreciosVenta]
}
