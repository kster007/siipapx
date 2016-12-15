package sx.replica

class ImportadorGeneralJob {
    
	def mailService

    static triggers = {
      simple repeatInterval: 10000l // execute job once in 5 seconds
    }

    def execute() {
    	mailService.sendMail{
    		from 'erendira.cuellar@gmail.com'
    		to 'rubencancino6@gmail.com'
    		subject "New user"
            text "A new user has been created"
    	}
        log.info('Ejcutando importacion de entidades......')
    }
}
