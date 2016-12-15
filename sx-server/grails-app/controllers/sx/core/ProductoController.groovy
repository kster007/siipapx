package sx.core

import grails.rest.RestfulController


class ProductoController extends RestfulController{

    static responseFormats = ['json']

    public ProductoController(){
        super(Producto)
    }



    @Override
    protected List listAllResources(Map params) {
        def query = Producto.where {}
        params.sort = params.sort ?:'linea'
        params.order = params.order ?:'desc'
        params.max = params.max ?: 100

        if(params.term){
            def search = '%' + params.term + '%'
            query = query.where { clave =~ search || descripcion =~ search}

            query = query.where {deLinea == true }
            query = query.where {activo == true }

        }
        if(params.activos){
            query = query.where {activo == activos}
        }
        return query.list(params)
    }
}
