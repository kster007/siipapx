package sx.core


class Linea {

	String id

	String linea

	String descripcion

	Date dateCreated

	Date lastUpdated

    static constraints = {
        linea minSize:2, maxSize:50, unique:true
    }

    String toString(){
    	return linea
    }
}
