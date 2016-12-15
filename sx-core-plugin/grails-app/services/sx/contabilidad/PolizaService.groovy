package sx.contabilidad

import grails.transaction.Transactional

@Transactional
class PolizaService {

    def save(Poliza poliza) {
        poliza.with{
            if(!folio)
                folio=nextFolio(poliza)

        }
        poliza.actualizar()
        //log.info 'Salvando poliza: '+poliza+ ' Partidas: '+poliza.partidas.size()+ ' Folio asignado: '+poliza.folio
        cuadrar(poliza)
        poliza=poliza.save failOnError:true
        return poliza
    }

    def cuadrar(Poliza poliza){

    }

    private Long nextFolio(Poliza poliza){
        if(poliza.subtipo == 'CIERRE_ANUAL')
            return 1;
        def folio=PolizaFolio.findByEjercicioAndMesAndSubTipo(poliza.ejercicio,poliza.mes,poliza.subtipo)
        if(folio==null){
            folio=new PolizaFolio(
                    ejercicio:poliza.ejercicio,
                    mes:poliza.mes,
                    subTipo:poliza.subtipo,
                    folio:0l)
        }
        def res=folio.next()
        folio.save failOnError: true, flush:true
        return res
    }
}
