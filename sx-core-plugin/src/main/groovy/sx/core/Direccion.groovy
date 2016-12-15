package sx.core

import grails.validation.Validateable
import groovy.transform.ToString

/**
 * Created by rcancino on 06/09/16.
 */
@ToString(includeNames=true,includePackage=false)
class Direccion implements Validateable{

    String calle
    String numeroInterior
    String numeroExterior
    String colonia
    String municipio
    String codigoPostal
    String estado
    String pais='MEXICO'


    static constraints = {
        calle(nullable:true,size:1..200)
        numeroInterior(size:1..50,nullable:true)
        numeroExterior(size:1..50,nullable:true)
        colonia(nullable:true)
        municipio(nullable:true)
        codigoPostal(nullable:true)
        estado(nullable:true)
        pais(nullable:true,size:1..100)
    }

    String toLabel(){
        return "Calle:$calle #:$numeroExterior ${numeroInterior?:''} Col:$colonia CP:$codigoPostal Del/Mun:$municipio $estado $pais"
    }


}
