package sx.cxc

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import sx.core.Sucursal

@ToString(excludes = ["id,version,lastUpdated,dateCreated"],includeNames=true,includePackage=false)
@EqualsAndHashCode(includeFields = true,includes = ['id'])
class AplicacionDeCobro {

    String id

    String cobroId

    String cxcId

    String clave

    String nombre

    //******** Propiedades del cargo

    String cargoSucursal

    Date cargoFecha

    String cargoDocumento

    String cargoOrigen

    BigDecimal cargoTotal

    //******** Propiedades del abono o cobro

    String abonoBanco

    String abonoFolio

    String abonoDescripcion

    Date fecha

    BigDecimal importe

    String tipoId

    Date saf

    Date fechaDeposito

    Boolean anticipo

    BigDecimal diferencia = 0.0

    BigDecimal total

    String cobro

    String tipo

    String sw2

    Date creado

    Date dateCreated
    Date lastUpdated


    static constraints = {
        clave maxSize:10
        cargoOrigen maxSize: 5
        abonoBanco nullable:true, maxSize:100
        abonoFolio nullable:true, maxSize:30
        abonoDescripcion nullable:true
        tipoId maxSize:20
        fechaDeposito nullable:true
        cobro maxSize: 20
        tipo maxSize: 3
        sw2 nullable:true
        creado nullable:true
    }

    static mapping = {
        id generator:'uuid'
        fecha type: 'date'
        cargoFecha type:'date'
        saf type:'date'
        fechaDeposito type:'date'
    }
}


