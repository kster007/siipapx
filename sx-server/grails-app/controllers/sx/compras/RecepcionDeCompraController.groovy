package sx.compras

import grails.rest.RestfulController
import groovy.transform.ToString
import sx.core.Proveedor
import sx.core.Sucursal

class RecepcionDeCompraController extends  RestfulController{

    static responseFormats = ['json']

    public RecepcionDeCompraController() {
        super(RecepcionDeCompra)
    }


    def index(RecepcionesFiltro filtro){
        log.info('Buscando con filtro: ' + filtro)
        params.max = filtro.registros ?:10
        def query  = RecepcionDeCompra.where {}
        if(filtro.fechaInicial){
            Date inicio = filtro.fechaInicial
            Date fin = filtro.fechaFinal ?: inicio
            query = query.where {fecha >= inicio && fecha <= fin}
        }
        if (filtro.sucursal) {
            query = query.where { sucursal == filtro.sucursal}
        }
        if(filtro.proveedor) {
            query = query.where {proveedor == filtro.proveedor}
        }
        respond query.list(params)
    }


    @Override
    protected List listAllResources(Map params) {
        log.info('Cargando recepciones con parametros: ' + params)
        def query = RecepcionDeCompra.where {}
        params.max = params.registros ?:10
        params.sort = params.sort ?:'folio'
        params.order = params.order ?:'desc'

        if(params.term){
            def search = '%' + params.term + '%'
            query = query.where { proveedor.nombre =~ search }
        }

        return query.list(params)
    }
}

@ToString(includeNames=true,includePackage=false)
class RecepcionesFiltro {
    Date fechaInicial
    Date fechaFinal
    Sucursal sucursal
    Proveedor proveedor
    int registros = 20


    static constraints = {
        fechaInicial nullable:true
        fechaFinal nullable: true
        sucursal nullable:true
        proveedor nullable: true
        registros size:(1..1000)

    }

    /*String toString(){
        return "$fechaInicial al $fechaFinal ${proveedor?.nombre}"
    }*/
}
