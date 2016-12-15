package sx.core

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includes = ["clave,descripcion,cantidad,precio"],includeNames=true,includePackage=false)
@EqualsAndHashCode(includes = 'id,sucursal,clave,cantidad,importe')
class VentaDet {

	String id

	Sucursal sucursal

	Producto producto

	String clave

	String descripcion

	String unidad

	BigDecimal factor

	Boolean nacional  = true

	Long documento

	Date fecha

	BigDecimal cantidad

	BigDecimal kilos = 0.0

	BigDecimal precioOriginal

	BigDecimal precioLista

	BigDecimal precio

	BigDecimal importe = 0.0

	BigDecimal desctoOriginal

	BigDecimal descuento = 0.0

	BigDecimal importeDescuento

	BigDecimal importeNeto

	Integer cortes = 0

	BigDecimal precioCortes

	BigDecimal importeCortes

	String cortesInstruccion

	BigDecimal subtotal

	BigDecimal costoReposicion

	BigDecimal costoPromedio

	BigDecimal costoUltimo

	Boolean conVale = false

	Boolean cortado = false

	String comentario

	String sw2

	Venta venta

    Date dateCreated

    Date lastUpdated

    String createUser
    String updateUser


    static constraints = {
        sw2 nullable:true
        createUser nullable: true
        updateUser nullable: true
		comentario nullable: true
        cortesInstruccion nullable: true

    }

    static mapping = {
        id generator:'uuid'
        fecha index: 'VENTADET_IDX1'
        producto index: 'VENTADET_IDX2'
        clave index: 'VENTADET_IDX2'
        descripcion index: 'VENTADET_IDX2'
    }

    static belongsTo = [venta:Venta]
}
