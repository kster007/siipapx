package sx.server

import sx.security.*

class BootStrap {

    def init = { servletContext ->
      
      def userRol=Role.findOrSaveWhere(authority:'USUARIO')
      def adminRole=Role.findOrSaveWhere(authority:'ADMIN')

      def admin=User.findByUsername('admin')
      if(!admin){
        admin=new User(username:'admin'
          ,password:'admin'
          ,apellidoPaterno:'admin'
          ,apellidoMaterno:'admin'
          ,nombres:'admin'
          ,nombre:' ADMIN ADMIN'
          ,numeroDeEmpleado:'0000')
        .save(flush:true,failOnError:true)
        UserRole.create(admin,userRole,true)
        UserRole.create(admin,adminRole,true)
      }

    }
    def destroy = {
    }
}
