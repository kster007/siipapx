package sx.core

import groovy.transform.ToString

@ToString(excludes = ["id,version,venta,cobrador,socio,dateCreated,lastUpdated"],includeNames=true,includePackage=false)
class VentaCredito {

    String id

    Integer plazo

    Date vencimiento

	Date fechaRecepcionCxc

    Integer diaRevision

    Date fechaRevision

    Date fechaRevisionCxc

    Boolean revision = true

    Boolean revisada = false

    Integer diaPago

    Date fechaPago

    Date reprogramarPago

    String comentarioReprogramarPago

    Cobrador cobrador

    Socio socio

    static belongsTo = [venta: Venta]

    static constraints = {
        comentarioReprogramarPago nullable:true
        fechaRecepcionCxc nullable:true
        reprogramarPago nullable:true
        socio nullable:true
    }

    static mapping = {
        id generator:'uuid'
        fechaRevision type:'date', index: 'VENTACRE_IDX1'
        fechaRevisionCxc type:'date', index: 'VENTACRE_IDX1'
        fechaRecepcionCxc type:'date' ,index: 'VENTACRE_IDX1'
        vencimiento type: 'date', index: 'VENTACRE_IDX2'
        fechaPago type:'date', index: 'VENTACRE_IDX2'
        reprogramarPago type:'date'

    }
}