package sx.core


import grails.rest.*
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='id')
@Resource(uri='/api/core/clases', formats=['json'])
class Clase {

	String id

	String clase

  static constraints = {
    clase minSize:2, maxSize:50, unique:true
  }

  
}