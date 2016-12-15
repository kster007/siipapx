package sx.cxc

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import sx.core.Cliente
import sx.core.Sucursal

@ToString(includes = ["nombre,clave,fecha,sucursal,formaDePago,total"],includeNames=true,includePackage=false)
@EqualsAndHashCode(includeFields = true,includes = ['id,clave,fecha,total'])
class Cobro {

    String id

    Cliente cliente

    String nombre

    String clave

    String rfc

    Sucursal sucursal

    String origen

    Date fecha

    String formaDePago

    Currency moneda = Currency.getInstance('MXN')

    BigDecimal tipoDeCambio = 1.0

    BigDecimal importe

    BigDecimal impuesto

    BigDecimal total

    Date dateCreated

    Date lastUpdated

    String creadoPor

    String modificadoPor

    String sw2


    static constraints = {
        origen inList:['CAM','MOS','CRE','CHE','JUR']
        sw2 nullable:true, unique:true
        creadoPor nullable:true
        modificadoPor nullable:true
        cheque nullable: true
        deposito nullable:true
        transferencia nullable:true
    }

    static mapping={
        id generator:'uuid'
        fecha type:'date' ,index: 'COBRO_IDX1'
        cliente index: 'COBRO_IDX2'
        formaDePago index: 'COBRO_IDX3'
    }

    static hasOne = [cheque: CobroCheque, deposito: CobroDeposito, transferencia: CobroTransferencia]
}
