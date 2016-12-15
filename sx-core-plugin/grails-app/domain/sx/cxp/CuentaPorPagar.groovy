package sx.cxp

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import sx.cfdi.Acuse
import sx.cfdi.ComprobanteFiscal
import sx.core.Proveedor
import sx.utils.MonedaUtils

@ToString(includeNames=true,includePackage=false, includes = ['nombre', 'documento','fecha','total','comprobante'])
@EqualsAndHashCode(includeFields = true,includes = ['proveedor','documento','fecha', 'total','comprobante'])
class CuentaPorPagar {

    Proveedor proveedor

    String tipo

    String nombre

    String rfc = 'XAXX010101000'

    String documento

    Date fecha = new Date()

    Date vencimiento

    Date revision

    Currency moneda = Currency.getInstance('MXN')

    BigDecimal tipoDeCambio=1.0

    //Importes
    BigDecimal importe = 0.0

    BigDecimal impuestoTasa=MonedaUtils.IVA

    BigDecimal impuesto=0.0

    BigDecimal total=0.0

    BigDecimal descuentof = 0

    Date descuentofVto

    BigDecimal retencionIvaTasa=0

    BigDecimal retencionIva=0

    BigDecimal retencionIsrTasa=0

    BigDecimal retencionIsr=0

    Boolean anticipo = false;

    //Datos de CFDI...
    ComprobanteFiscal comprobante

    String comentario

    BigDecimal requisitado = 0.0

    BigDecimal pendienteRequisitar = 0.0

    BigDecimal pagosAplicados = 0.0

    Long sw2

    Date dateCreated
    Date lastUpdated


    static constraints = {
        rfc nullable:true, minSize:12, maxSize:13
        tipo inList:['COMPRAS', 'GASTOS']
        tipoDeCambio(scale:6)
        importe(scale:4)
        impuesto(scale:4)
        total(scale:4)
        retencionIsr(scale:4)
        retencionIva(sacle:4)
        comentario(nullable:true)
        vencimiento (validator:{vencimiento,cxp->
            if( (vencimiento <=> cxp.fecha) < 0 )
                return "vencimientoInvalido"
            else return true
        })
        revision nullable:true
        descuentofVto nullable:true
        comprobante nullable:true, unique:true

    }

    static mapping ={
        //requisitado formula:'(select ifnull(sum(x.requisitado),0) from requisicion_det x where x.cuenta_por_pagar_id=id)'
        //pagosAplicados formula:'(select ifnull(sum(x.importe),0) from aplicacion_de_pago x where x.cuenta_por_pagar_id=id)'
        nombre index:'CXP_IDX1'
        rfc index:'CXP_IDX1'
        fecha type:'date' , index: 'CXP_IDX2'
        vencimiento type:'date', index: 'CXP_IDX2'
        revision type:'date'
        descuentofVto type:'date'
    }



    static transients = ['pendienteRequisitar','saldoActual','saldoAlCorte','saldo']

    BigDecimal toPesos(String property){
        return "${property}" * tipoDeCambio

    }

    public BigDecimal getPendienteRequisitar(){
        def req=requisitado?:0.0
        return total-req
    }

    public BigDecimal getSaldo(){
        return total - pagosAplicados?:0.0
    }



    def actualizar(){
    }

    def beforeInsert(){
        actualizar()
    }

    def beforeUpdate(){
        actualizar()
    }
    def beforeValidate(){
        if(!nombre)
            nombre = this.proveedor.nombre
    }
}
