package sx.server.integracion

import org.apache.commons.lang.exception.ExceptionUtils
import org.springframework.stereotype.Component
import sx.cxc.AplicacionDeCobro

/**
 * Created by rcancino on 01/11/16.
 */
@Component
class ImportadorDeAplicacionDeCobro implements Importador, SW2Lookup{

    def importar(f1,f2){
        (f1..f2).each{
            importar(it)
        }
    }

    def importar(fecha){
        logger.info("Importando cobranza  del : ${fecha.format('dd/MM/yyyy')}" )
        String select = QUERY + " where A.fecha = ?  order by a.creado"
        def rows = leerRegistros(select,[fecha.format('yyyy-MM-dd')])
        def importados = 0
        rows.each { row ->
            build(row)
            importados++
        }
        return importados
    }


    def importar(String sw2){
        logger.info('Importando cobro ' + sw2)
        String select = QUERY + " and abono_id = ? "
        def row = findRegistro(select, [sw2])
        build(row)

    }

    def build(def row){
        logger.info('Importando aplicacion de cobro: ' + row)
        def aplicacion = AplicacionDeCobro.where { sw2 == row.sw2}.find()
        if(!aplicacion){
            aplicacion = new AplicacionDeCobro()
            bindData(aplicacion,row)
            aplicacion = aplicacion.save failOnError:true, flush:true
            return aplicacion
            /*try{

            }catch (Exception ex){
                logger.error(ExceptionUtils.getRootCauseMessage(ex))
            }*/
        }
    }

    static QUERY = """
SELECT
A.APLICACION_ID as sw2,
A.CARGO_ID as cxcId,
A.ABONO_ID as cobroId
,A.clave
,A.nombre
,A.CAR_SUCURSAL as cargoSucursal
,A.CAR_FECHA as cargoFecha
,A.CAR_DOCTO as cargoDocumento
,A.CAR_ORIGEN  as cargoOrigen
,A.CAR_TOTAL as cargoTotal
,A.ABN_BANCO as abonoBanco
,A.ABN_FOLIO as abonoFolio
,A.ABN_DESCRIPCION as abonoDescripcion
,A.fecha
,A.importe
,B.TIPO_ID as tipoId
,B.saf
,B.fecha_deposito as fechaDeposito
,B.anticipo
,B.diferencia
,B.total
,(CASE WHEN B.TIPO_ID IN('PAGO_EFE') THEN 'FICHA_EFE'
WHEN B.TIPO_ID IN('PAGO_HXE','PAGO_CHE') THEN 'FICHA_CHE'
WHEN  B.TIPO_ID IN('PAGO_DEP') AND B.TRANSFERENCIA=0 THEN 'DEPOSITO'
WHEN  B.TIPO_ID IN('PAGO_DEP') AND B.TRANSFERENCIA>0 THEN 'TRANSFERENCIA'
WHEN  B.TIPO_ID IN('PAGO_TAR') AND B.COMISION_TARJETA>2.36 THEN 'TARJETA_AMEX'
WHEN  B.TIPO_ID IN('PAGO_TAR') AND B.COMISION_TARJETA=2.36 THEN 'TARJETA_CRE'
WHEN  B.TIPO_ID IN('PAGO_TAR') AND B.COMISION_TARJETA=1.46 THEN 'TARJETA_DEB'
WHEN  B.TIPO_ID IN('PAGO_DIF') THEN B.TIPO_ID
WHEN B.TIPO_ID IN('NOTA_BON','NOTA_DEV') THEN B.TIPO_ID ELSE 'ND' END) AS cobro
,(CASE WHEN B.ANTICIPO IS TRUE THEN 'ANT'
WHEN B.TIPO_ID IN('PAGO_DEP') AND MONTH(B.FECHA_DEPOSITO)<>MONTH(B.SAF) THEN 'DXI'
WHEN B.TOTAL>A.IMPORTE AND B.SAF<A.FECHA THEN 'SAF'
WHEN B.DIFERENCIA>0 THEN 'DIF' ELSE 'PAG' END) AS tipo
,a.creado
FROM sx_cxc_aplicaciones a
join sx_cxc_abonos b on(b.ABONO_ID=a.ABONO_ID)
    """
}
