package sx.cxc

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includes = ["dateCreated,lastUpdated,version,id,cobro"],includeNames=true,includePackage=false)
@EqualsAndHashCode(includeFields = true,includes = ['id,clave,fecha,total'])
class CobroDeposito {

    String id

    Long folio = 0

    Date fechaDeposito

    String referencia

    String banco

    BigDecimal totalCheque = 0.0

    BigDecimal totalEfectivo = 0.0

    BigDecimal totalTarjeta = 0.0

    Boolean conciliado = false

    Date dateCreated
    Date lastUpdated

    static belongsTo = [cobro: Cobro]

    static constraints = {
        referencia nullable:true
        banco nullable:true
        fechaDeposito nullable:true
    }

    static mapping={
        id generator:'uuid'
        fechaDeposito type:'date' ,index: 'COBRO_DEP_IDX1'
    }
}
