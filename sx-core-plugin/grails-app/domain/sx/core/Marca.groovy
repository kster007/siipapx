package sx.core


import grails.rest.*
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='id')
@Resource(uri='/api/core/marcas', formats=['json'])
class Marca {

	String id

	String marca

  static constraints = {
    marca minSize:3, maxSize:20, unique:true
  }

  String toString(){
   return marca
 }
 
}