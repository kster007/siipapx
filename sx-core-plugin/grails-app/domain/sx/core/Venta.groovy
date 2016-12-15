package sx.core

import groovy.transform.ToString


@ToString( includes = "clave,nombre,sucursal,fecha,total",includeNames=true,includePackage=false)
class Venta {

    String id

    Sucursal sucursal    // Cambio de Long a String
    
    Cliente cliente
    
    String clave
    
    String nombre
    
    String rfc
    
    String tipo
    
    String documento  

    Date fecha 

    String folio

    String serie

    String uuid

    Currency moneda = Currency.getInstance('MXN')

    BigDecimal tipoDeCambio = 1.01

    String formaDePago

    String cuentaDePago

    BigDecimal importe

    BigDecimal descuento

    BigDecimal impuesto

    BigDecimal total

    String comentario

    Date facturar

    Date puesto

    BigDecimal descuentoOriginal

    BigDecimal cargosPorManiobra = 0.0

    BigDecimal kilos = 0.0

    Date impreso

    BigDecimal comisionTarjeta  = 0.0

    BigDecimal comisionTarjetaImp = 0.0

    Vendedor vendedor

    String comprador

    Sucursal sucursalVenta    // Nuevo

    String atencion = 'ND'

    String clasificacionVale

    Sucursal sucursalVale    // cambio de Long a String

    Boolean vale = false

    

    CondicionDeEnvio envio

    String sw2

    Date dateCreated

    Date lastUpdated

    String createUser    // Nuevo

    String updateUser    // Nuevo

    List partidas = []

    static constraints = {
        tipo  inList:['CON','COD','CRE','PSF','INE','OTR','ACF','ANT','AND']
        documento maxSize: 20
        envio nullable:true
        uuid nullable:true, unique:true
        serie nullable:true, maxSize: 20
        folio nullable:true, unique:'serie', maxSize: 20
        rfc minSize:12, maxSize:13
        puesto nullable: true
        tipoDeCambio(scale:6)
        impreso nullable:true
        cuentaDePago nullable:true, maxSize:4
        atencion inList:['MOSTRADOR','TELEFONICA','ND']
        clasificacionVale nullable:true,maxSize:30
        comentario nullable:true
        facturar nullable:true
        comprador nullable:true
        cancelacion nullable:true
        credito nullable:true
        sw2 nullable:true
        createUser nullable:true, maxSize: 100
        updateUser nullable:true, maxSize: 100
        
        sucursalVale nullable:true
        sucursalVenta nullable:true

    }

    static mapping = {
        partidas cascade: "all-delete-orphan"
        id generator:'uuid'
        fecha type:'date' ,index: 'VENTA_IDX1'
        serie index: 'VENTA_IDX2'
        folio index: 'VENTA_IDX2'
        cliente index: 'VENTA_IDX3'
        nombre index: 'VENTA_IDX4'
        credito unique: true
        cancelacion unique: true

    }

    static hasMany =[partidas:VentaDet]

    static hasOne=[credito:VentaCredito,cancelacion: VentaCancelada]

}
