package sx.tesoreria

import groovy.transform.EqualsAndHashCode
import sx.cxp.CuentaPorPagar

//@EqualsAndHashCode(includes='cuentaPorPagar')
class AplicacionDePago {

	MovimientoDeCuenta egreso

	CuentaPorPagar cxp
    
	Date fecha

	BigDecimal importe

	String comentario

    static constraints = {
    	comentario nullable:true
    }

    static mapping = {
		fecha type:'date'
		
	}
	
}
