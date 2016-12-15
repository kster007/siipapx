package sx.cxp

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(excludes = ['id,version,sw2,dateCreated,lastUpdated'],includeNames=true,includePackage=false)
@EqualsAndHashCode(includeFields = true,includes = ['id'])
class ContraReciboDet {

    String id

    String documento

    Date fecha = new Date()

    Date vencimiento = new Date()


    BigDecimal tipoDeCambio = 1.0


    BigDecimal total = 0.0

    Currency moneda = Currency.getInstance('MXN')

    //CuentaPorPagar cxp
    Long cxp

    String tipo = 'FACTURA'


    Long requisicionDet = 0

    Date dateCreated
    Date lastUpdated

    Long sw2

    static belongsTo = [recibo: ContraRecibo]

    static constraints = {
        tipo inList:['FACTURA', 'CREDITO', 'CARGO']
        vencimiento nullable:true
        sw2 nullable:true
        requisicionDet nullable: true
        cxp nullable: true
    }

    static mapping = {
        id generator:'uuid'
        fecha type:'date' , index: 'CFDIBODET_IDX1'
        vencimiento type: 'date', index: 'CFDIBODET_IDX1'
    }
}
