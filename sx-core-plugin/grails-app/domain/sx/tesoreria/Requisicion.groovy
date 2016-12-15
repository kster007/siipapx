package sx.tesoreria

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import sx.core.Autorizacion

@ToString(excludes ='id,version,dateCreated,lastUpdated,sw2,partidas',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes='id')
class Requisicion {

    String nombre

    String rfc

    String afavor

    String entidad

    Currency moneda

    BigDecimal tipoDeCambio = 0.0

    Date fecha

    Date fechaDePago

    String formaDePago = 'CHEQUE'

    BigDecimal total=0.0

    List partidas

    String comentario

    Long sw2

    Autorizacion autorizacion

    Date dateCreated
    Date lastUpdated

    static hasMany = [partidas: RequisicionDet]

    static constraints = {
        entidad minSize: 3, maxSize:100
        rfc size:12..13
        comentario nullable:true
        total scale:4
        formaDePago maxSize:50
        sw2 nullable:true
        autorizacion nullable:true
    }

    static mapping = {
        partidas cascade: "all-delete-orphan"
        nombre index: 'REQ_IDX1'
        afavor index: 'REQ_IDX1'
        rfc index: 'REQ_IDX1'
        fechaDePago type:'date' , index: 'REQ_IDX2'
        fecha type:'date', index: 'REQ_IDX3'
    }

    def actualizar(){
        //debe=partidas.sum (0.0,{it.debe})
        //haber=partidas.sum(0.0,{it.haber})
        if(!afavor)
            afavor = nombre
    }

    def beforeInsert(){
        actualizar()
    }

    def beforeUpdate(){
        actualizar()
    }


    enum Entidad {
        GASTOS, COMPRAS, REEMBOLSO, NOMINA, GENERICA, COMISION, COMPRA_DOLARES, TESORERIA
    }
}


