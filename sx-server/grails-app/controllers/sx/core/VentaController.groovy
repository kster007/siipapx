package sx.core

import grails.rest.RestfulController
import groovy.transform.ToString

class VentaController extends RestfulController{

    static responseFormats = ['json']

    VentaController(){
        super(Venta)
    }


    def index(VentasFiltro filtro) {
        params.max = params.registros ?:100
        params.sort = params.sort ?:'lastUpdated'
        params.order = params.order ?:'desc'

        def query = Venta.where {}

        if(filtro.fechaInicial){
            Date inicio = filtro.fechaInicial
            Date fin = filtro.fechaFinal ?: inicio
            query = query.where {fecha >= inicio && fecha <= fin}
        }
        if (filtro.sucursal) {
            query = query.where { sucursal == filtro.sucursal}
        }
        if (filtro.cliente ) {
            query = query.where { cliente == filtro.cliente}
        }
        respond query.list(params)
    }


    /*def queryForResource(Serializable id) {
        return Venta.get(id)
    }*/
}

@ToString(includeNames=true,includePackage=false)
class VentasFiltro {
    Date fechaInicial
    Date fechaFinal
    Sucursal sucursal
    Cliente cliente

    int registros = 20


    static constraints = {
        fechaInicial nullable:true
        fechaFinal nullable: true
        sucursal nullable:true
        cliente nullable:true
        registros size:(1..500)
    }

    /*String toString(){
        return "$fechaInicial al $fechaFinal ${proveedor?.nombre}"
    }*/
}
