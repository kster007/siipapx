package sx.compras

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import sx.core.Proveedor
import sx.core.Sucursal

@ToString(includes = 'folio,remision,sucursal,fecha,comentario',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes='sucursal,documento')
class RecepcionDeCompra {

    String id

    Long folio

    String remision

    Compra compra

    Proveedor proveedor

    Sucursal sucursal

    Date fecha

    String comentario

    List partidas = []

    Date dateCreated

    Date lastUpdated

    String sw2

    static constraints = {
        comentario nullable:true
        sw2 nullable:true
    }

    static hasMany =[partidas:RecepcionDeCompraDet]

    static mapping = {
        id generator:'uuid'
        partidas cascade: "all-delete-orphan"
        fecha type:'date', index: 'RECOMPRA_IDX1'
        folio unique: 'sucursal'
    }

    public void setCompra(Compra compra){
        this.compra = compra
        this.proveedor = compra.proveedor
    }


}
