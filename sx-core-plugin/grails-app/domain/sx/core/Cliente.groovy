package sx.core

import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode

@ToString(excludes = 'version,dateCreated,lastUpdated,id',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes='nombre,rfc')
class Cliente {

	String id

    String clave

	String nombre

	String rfc

	Boolean activo = false

	Boolean juridico = false

    Direccion direccion

	Long sw2

	Date dateCreated

	Date lastUpdated

  	//static hasOne=[clienteCredito:ClienteCredito,cuentaBancaria:CuentaBancaria]

	//static hasMany=[contactos:Contacto,telefonos:Telefono,cfdiMails:CfdiMail,direccionDeEntrega:DireccionDeEntrega,direcciones:Direccion]

    static constraints = {
    	nombre unique:true
    	rfc minSize:12, maxSize:13, unique: 'nombre'
    	sw2 nullable:true
    	direccion nullable:true
    }

    static embedded = ['direccion']

    static mapping={
    	id generator:'uuid'
        clave index: 'Clie_IDX_1'
        nombre index: 'Clie_IDX_1'
   		//contactos cascade:"all-delete-orphan"
    	//telefonos cascade:"all-delete-orphan"
    	//cfdiMails cascade:"all-delete-orphan"
    	//direcciones cascade:"all-delete-orphan"
    	//clienteCredito cascade:"all-delete-orphan"
    }
}
