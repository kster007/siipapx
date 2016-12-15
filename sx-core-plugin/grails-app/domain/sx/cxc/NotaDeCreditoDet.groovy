package sx.cxc

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames=true,includePackage=false, excludes = ['nota','dateCreated', 'lastUptdated','id','version'])
@EqualsAndHashCode(includeFields = true)
class NotaDeCreditoDet {

    BigDecimal cantidad

    String unidad

    String numeroDeIdentificacion

    String descripcion

    BigDecimal valorUnitario

    BigDecimal importe

    String comentario

    Date dateCreated

    Date lastUpdated


    static constraints = {
        unidad maxSize:100
        numeroDeIdentificacion maxSize:50
        comentario nullable:true
    }

    static belongsTo =[nota:NotaDeCredito]
}
