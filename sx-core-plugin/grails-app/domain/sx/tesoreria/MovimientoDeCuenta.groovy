package sx.tesoreria

import groovy.transform.EqualsAndHashCode
import sx.core.Sucursal

@EqualsAndHashCode(includes='id')
class MovimientoDeCuenta {

    String id

    CuentaDeBanco cuenta

    String afavor

    Sucursal sucursal

    Date fecha

    String formaDePago

    String origen

    String referencia

    String concepto

    BigDecimal importe

    Currency moneda = Currency.getInstance('MXN')

    BigDecimal tipoDeCambio = 1.0

    String comentario

    String comentario2

    Date creado

    Long sw2

    Date dateCreated
    Date lastUpdated
    String creadoPor
    String modificadoPor

    static constraints = {
        tipoDeCambio scale:6
        formaDePago maxSie: 20
        origen maxSie: 15
        concepto maxSie:20
        importe scale:4
        referencia nullable: true
        comentario nullable: true
        comentario2 nullable: true
        concepto nullable:true,maxSize:50
        sw2 nullable:true
        creado nullable:true
        creadoPor nullable: true
        modificadoPor nullable: true
    }

    static mapping ={
        fecha type:'date' , index: 'MOV_CTA_IDX1'
        afavor index: 'MOV_CTA_IDX2'
        formaDePago index: 'MOV_CTA_IDX3'
    }

    //static belongsTo = [cobro: Cobro,comision:Comision,traspaso:Traspaso]
    //static belongsTo =[Traspaso,Comision,PagoProveedor,CompraDeMoneda]

    String toString(){
        return "Folio:$folio ${cuenta}  (${fecha.format('dd/MM/yyyy')}) ${importe}"
    }
}
