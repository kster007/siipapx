package sx.sat

import grails.rest.*


//@Resource(uri='/api/sat/bancos', formats=['json'])
class BancoSat {

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
