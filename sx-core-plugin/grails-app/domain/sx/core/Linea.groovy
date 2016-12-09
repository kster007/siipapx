package sx.core

import grails.rest.*

@Resource(uri='/api/core/lineas', formats=['json'])
class Linea {

  String id

  String linea

  String descripcion

  static constraints = {
    linea minSize:2, maxSize:50, unique:true
  }

  String toString(){
    return linea
  }
}
