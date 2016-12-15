package sx.contabilidad

import groovy.transform.EqualsAndHashCode


@EqualsAndHashCode(includes='clave')
class SatBanco {

    String clave
    String nombreCorto
    String razonSocial


    static constraints = {
        clave nullable:false,unique:true,maxSize:20
    }


    String toString(){
        return "$clave $nombreCorto"
    }
}

