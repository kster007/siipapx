package sx.server.integracion

import org.apache.commons.lang.exception.ExceptionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sx.core.Cliente
import sx.core.Producto
import sx.core.Socio
import sx.core.Venta
import sx.core.VentaCredito
import sx.core.VentaDet

/**
 * Created by rcancino on 16/08/16.
 */
@Component
class ImportadorDeCuentasPorCobrar implements Importador, SW2Lookup{

    @Autowired
    ImportadorDeClientes importadorDeClientes

    @Autowired
    ImportadorDeProductos importadorDeProductos

    def importar(Date ini, Date fin){

    }

    def importar(fecha){
        logger.info("Importando cuentas por cobrar del : ${fecha.format('dd/MM/yyyy')}" )
        def ids = leerRegistros("select cargo_id from SX_VENTAS where fecha = ? ",[fecha.format('yyyy-MM-dd')])
        logger.info('Registros: ' + ids.size())
        ids.each { r ->
            importar(r.cargo_id)
        }
    }

    def importar(String sw2){
        logger.info('Importando venta ' + sw2)
        String select = QUERY + " where tipo = ? and cargo_id = ? "
        def row = findRegistro(select, ['FAC',sw2])
        build(row)
    }

    def build(def row){

        def venta = Venta.where{ sw2 == row.sw2}.find()
        if(!venta){
            venta = new Venta()
        }
        bindData(venta,row)
        venta.sucursal = buscarSucursal(row.sucursal_id)
        Cliente cliente = Cliente.where {sw2 == row.cliente_id}.find()
        if(!cliente){
            cliente = importadorDeClientes.importar(row.cliente_id)
        }
        venta.cliente = cliente
        venta.vendedor = buscarVendedor(row.vendedor_id)

        if(venta.tipo == 'CRE' || venta.tipo == 'PSF') {
            if(!venta.credito)
                venta.credito = new VentaCredito()
            VentaCredito credito = venta.credito
            bindData(credito , row)
            credito.cobrador = buscarCobrador(row.cobrador_id)
            credito.socio = Socio.where{sw2 == row.socio_id}.find()
            venta.credito=credito
        }
        importarPartidas(venta)
        try{
            venta = venta.save failOnError:true, flush:true
            return venta
        }catch (Exception ex){
            logger.error(ExceptionUtils.getRootCauseMessage(ex))
        }

    }

    def importarPartidas(Venta venta){

        List partidas = leerRegistros(QUERY_PARTIDAS,[venta.sw2])
        venta.partidas.clear()
        partidas.each{ row ->
            logger.info('Importando partida: ' + row.clave)
            VentaDet det = new  VentaDet()
            det.sucursal = buscarSucursal(row.sucursal_id)
            Producto producto = Producto.where {sw2 == row.producto_id}.find()
            if(!producto) {
                logger.info("Importando producto $row.producto_id para la venta")
                producto = importadorDeProductos.importar( row.producto_id)
                assert producto, 'No fue posible importar el producto :' +row.producto_id
            }
            det.producto = producto
            bindData(det,row)
            venta.addToPartidas(det)
        }
    }


    static String QUERY = """
       select
        cliente_id,
        clave,
        nombre,
        (SELECT c.rfc FROM sx_clientes c where c.CLIENTE_ID=v.CLIENTE_ID) rfc,
        (case when v.ANTICIPO is true then 'ANT'
            when v.POST_FECHADO is true then 'PSF'
            when v.POST_FECHADO is false and v.origen='CRE' then 'CRE'
            when v.origen='CAM' and V.CE is true then 'COD'
            when v.origen='CAM' and V.CE is false then 'CON'
            when v.origen='MOS' then 'CON' else 'ND'  end     ) tipo,
        v.origen
    ,v.docto as documento
    ,v.fecha fecha
    ,(SELECT c.folio FROM sx_cfdi c where c.ORIGEN_ID=v.cargo_id) as folio
    ,(SELECT c.serie FROM sx_cfdi c where c.ORIGEN_ID=v.cargo_id) as serie
    ,(SELECT c.uuid FROM sx_cfdi c where c.ORIGEN_ID=v.cargo_id) as uuid
    ,fecha as facturar
    ,(case when puesto is true then v.creado else null end) as puesto
    ,v.moneda
    ,v.tc as tipoDeCambio
    ,v.importe
    ,v.descuento
    ,v.descuento as descuentoOriginal
    ,v.impuesto
    ,v.total
    ,v.flete as cargosPorManiobra
    ,0.0 as kilos
    ,v.impreso
    ,v.fpago as formaDePago
    ,v.cancelacion_dbf_com as cuentaDePago
    ,0.00 as comisionTarjeta
    ,v.cargos as comisionTarjetaImp
    ,v.vendedor_id
    ,v.comprador
    ,v.sucursal_id
    ,'ND' AS atencion
    ,'SIN_VALE' as clasificacion_vale
    ,v.pedido_fentrega as  forma_de_entrega
    ,v.comentario
    ,v.cargo_id as sw2
    ,FECHA_RECEPCION_CXC as     fechaRecepcionCxc
    ,DIA_DE_REV as     diaRevision
    ,FECHA_REVISION as     fechaRevision
    ,FECHA_REVISION_CXC as     fechaRevisionCxc
    ,PLAZO as     plazo
    ,VTO as     vencimiento
    ,REVISION as     revision
    ,REVISADA as     revisada
    ,DIA_DEL_PAGO as     diaPago
    ,DIA_PAGO as     fechaPago
    ,REPROGRAMAR_PAGO as     reprogramarPago
    ,COMENTARIO_REP_PAGO as     comentarioReprogramarPago
    ,COBRADOR_ID
    ,SOCIO_ID
    from sx_ventas v

    """

    static String QUERY_PARTIDAS  = """
    select
        D.sucursal_id,
        D.producto_id,
        D.clave,
        D.descripcion,
        D.unidad_id as unidad,
        D.factorU as factor,
        D.nacional,
        D.documento,
        D.fecha,
        D.cantidad,
        D.kilos,
        D.precio_l AS precioOriginal,
        D.precio_l as precioLista,
        D.precio,
        D.importe,
        D.dscto_orig as desctoOriginal,
        D.dscto as descuento,
        D.importe-D.importe_neto AS importeDescuento,
        D.importe_neto as importeNeto,
        D.cortes,
        D.precio_cortes as precioCortes,
        D.cortes*D.precio_cortes as importeCortes,
        D.cortes_Instruccion as cortesInstruccion,
        D.subtotal,
        D.costo as costoReposicion,
        D.costoP as costoPromedio,
        D.costoU as costoUltimo,
        (case when D.CORTES=0 then 'false' else 'true' end) as cortado,
        D.comentario,
        D.inventario_id as sw2
        from sx_ventasdet D
        where venta_id = ?

    """
}
