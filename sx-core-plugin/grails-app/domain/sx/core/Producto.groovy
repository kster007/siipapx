package sx.core

import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode

@ToString(includes='id,clave,decripcion',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes='clave,descripcion')
class Producto {

	String id 

	String clave

    String descripcion

	String unidad

	String codigo

	Boolean activo = true

	BigDecimal kilos = 0

	BigDecimal gramos = 0

	BigDecimal calibre = 0

	Integer caras = 0

	String color

	String acabado

	String presentacion

	Boolean nacional = true

	BigDecimal ancho

	BigDecimal largo

	Boolean deLinea = true

	BigDecimal precioContado  = 0.0

	BigDecimal precioCredito = 0.0

	Date fechaLista

	String modoVenta

	BigDecimal m2XMillar = 0.0

	Boolean inventariable=true

	String linea

    String marca

    String clase

    Proveedor proveedorFavorito

	Long sw2

	Date dateCreated

	Date lastUpdated

    static constraints = {

		unidad minSize:2,maxSize:10
		codigo nullable:true
		caras range:0..2
		color nullable:true, maxSize:15
		acabado nullable:true, maxSize:20
		presentacion nullable:true, maxSize:25
		ancho nullable:true
		largo nullable:true
		fechaLista nullable:true
		modoVenta nullable:true, maxSize:1
		sw2 nullable:true
        linea nullable:true
        marca nullable: true
        clase nullable: true
        proveedorFavorito nullable:true
    }

    static mapping={
    	id generator:'uuid'
    }
}
