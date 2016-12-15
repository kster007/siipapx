package sx.cxc

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includes = ["dateCreated,lastUpdated,version,id,cobro"],includeNames=true,includePackage=false)
@EqualsAndHashCode(includeFields = true,includes = ['id,clave,fecha,total'])
class CobroCheque {

    String id

    String banco

    String cuenta

    String emisor

    Long numero = 0

    Boolean postFechado = false

    Date vencimiento

    Date dateCreated
    Date lastUpdated

    static constraints = {
        banco nullable:true
        cuenta nullable:true
        emisor nullable:true
        vencimiento nullable:true
    }

    static mapping={
        id generator:'uuid'
        vencimiento type: 'date'
    }

    static belongsTo = [cobro: Cobro]
}
