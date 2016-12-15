package sx.cxc

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import sx.core.Cliente
import sx.core.Cobrador
import sx.core.Socio
import sx.core.Sucursal

@ToString(excludes = ['id,version,sw2,dateCreated,lastUpdated'],includeNames=true,includePackage=false)
@EqualsAndHashCode(includeFields = true,includes = ['id,cliente,fecha,total'])
class CuentaPorCobrar {

    String id

    Sucursal sucursal

    Cliente cliente

    String clave

    String nombre

    String rfc

    String tipo

    String documento

    Date fecha

    String folio

    String serie

    String uuid

    Currency moneda = Currency.getInstance('MXN')

    BigDecimal tipoDeCambio = 1.0

    String formaDePago

    String cuentaDePago

    BigDecimal importe

    BigDecimal descuento

    BigDecimal impuesto

    BigDecimal total

    String comentario

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

    String sw2

    Date creado

    Date dateCreated
    Date lastUpdated


    static constraints = {
        tipo  inList:['CON','COD','CRE','PSF','INE','OTR','ACF','ANT','AND']
        comentarioReprogramarPago nullable:true
        fechaRecepcionCxc nullable:true
        reprogramarPago nullable:true
        socio nullable:true
        documento maxSize: 20
        uuid nullable:true, unique:true
        serie nullable:true, maxSize: 20
        folio nullable:true, unique:'serie', maxSize: 20
        rfc minSize:12, maxSize:13
        tipoDeCambio(scale:6)
        cuentaDePago nullable:true, maxSize:4
        comentario nullable:true
        sw2 nullable:true
    }

    static mapping = {
        id generator:'uuid'
        fecha type:'date' ,index: 'CXC_IDX1'
        serie index: 'CXC_IDX2'
        folio index: 'CXC_IDX2'
        cliente index: 'CXC_IDX3'
        clave index: 'CXC_IDX4'
        nombre index: 'CXC_IDX4'
        fechaRevision type:'date', index: 'CXC_IDX5'
        fechaRevisionCxc type:'date', index: 'CXC_IDX5'
        fechaRecepcionCxc type:'date' ,index: 'CXC_IDX5'
        vencimiento type: 'date', index: 'CXC_IDX6'
        fechaPago type:'date', index: 'CXC_IDX7'
        reprogramarPago type:'date'
    }


}
