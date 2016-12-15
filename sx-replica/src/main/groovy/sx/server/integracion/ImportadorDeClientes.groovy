package sx.server.integracion

import grails.web.databinding.DataBinder
import org.springframework.stereotype.Component
import sx.core.Cliente
import sx.core.Direccion


/**
 * Created by rcancino on 19/09/16.
 */
@Component
class ImportadorDeClientes implements  Importador{

    def importar(){
        logger.info('Importando clientes' + new Date().format('dd/MM/yyyy HH:mm:ss'))

        //def hoy = toSqlDate(new Date())
        def importados = 0
        leerRegistros(QUERY,[]).each { row ->

            def cliente = Cliente.where{ clave == row.clave}.find()
            if(!cliente){
                cliente = new Cliente()
                importados++
            }
            bindData(cliente,row)
            cliente.direccion = cliente.direccion ?: new Direccion()
            bindData(cliente.direccion,row)

            cliente.save flush:true
        }
        def message = "Clientes importados: $importados"
        return message
    }

    def importar(def sw2){
        def cliente = Cliente.where{ sw2 == sw2}.find() ?: new Cliente()
        def select = QUERY_ROW + ' where cliente_id = ?'
        def row = getSql().firstRow(select,[sw2])
        bindData(cliente,row)
        cliente.direccion = cliente.direccion ?: new Direccion()
        bindData(cliente.direccion,row)
        cliente.save failOnError:true, flush:true

    }

    static String QUERY = """
            select
            cliente_id as sw2,
            clave,nombre,rfc,
            if(suspendido,'false','true') as activo,
            juridico,
            permitir_cheque as permitirCheque,
            calle,
            numero,
            numeroint,
            delmpo as municipio,
            cp as codigoPostal,
            colonia,
            estado,
            pais
            from sx_clientes
            where year(modificado) > 2015  and month(modificado) > 1
            """
    static String QUERY_ROW = """
            select
            cliente_id as sw2,
            clave,nombre,rfc,
            if(suspendido,'false','true') as activo,
            juridico,
            permitir_cheque as permitirCheque,
            calle,
            numero,
            numeroint,
            delmpo as municipio,
            cp as codigoPostal,
            colonia,
            estado,
            pais
            from sx_clientes

            """
}
