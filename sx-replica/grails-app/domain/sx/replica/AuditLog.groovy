package sx.replica


class AuditLog {
	
	
	String entityName
	
	String entityId
	
	String action
	
	String message
	
	String tableName
	
	String sucursalOrigen

	String sucursalDestino;
	
	String ip
	
	Date replicado
	
	Date dateCreated
	
	Date lastUpdated
	

    static constraints = {
		entityName maxLength:40
		entityId maxLenth:255
		action maxLength:20
		tableName maxLenth:50
		sucursalOrigen maxLength:50
		sucursalDestino nullable:true,maxLength:50
		ip nullable:true,maxLength:50
		message nullable:true, maxSize:100000
		replicado nullable:true
    }
	
	static mapping = {
		//id generator: 'uuid',params: [separator: '-']
		
	}
    
	
	
}
