package controllers

import models.User
import anorm.NotAssigned
import anorm._
import play.api.db._
import play.api.mvc._
import play.api._
import play.api.libs.json.Json._
import play.api.libs.json._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

object Users extends Controller {
	
  def createUserJson = Action { implicit request =>
	    request.body.asJson.map { json =>
	        val userName 	= (json \ "userName").as[String]
	        val email    	= (json \ "email").as[String]
	        val password 	= (json \ "password").as[String]
	        val role 	 	= (json \ "role").as[String]
	        val primaryLoc 	= (json \ "primaryLoc").as[Option[Long]]
	        
	        val newUser  = User(NotAssigned, null, null, null, userName, email, password, role, primaryLoc)
	        User.create(newUser)
			
			Redirect(routes.Users.allUsers)
		}.getOrElse {
			BadRequest("Expecting Json data")
		}
	}
	
	def createUser = Action { implicit request =>
	    val newUser: User = userForm.bindFromRequest.get
	    User.create(newUser)
	    Redirect(routes.Users.allUsers)
	}
		
	val userForm = Form(
		mapping(
			"userName"   -> text,
			"email"    	 -> text,
			"password"   -> text,
			"role"	   	 -> text,
			"primaryLoc" -> optional(of[Long])
		)((userName, email, password, role, primaryLoc) => User(NotAssigned, null, null, null, userName, email, password, role, primaryLoc))
		 ((user: User) => Some(user.userName, user.email, user.password, user.role, user.primaryLoc))
	)
	
	def allUsers = Action {
	    val users = User.findAll	    
	    Ok(views.html.protoUsers(users, userForm))
	}
	
	def allUsersJson = Action {
	    val users = User.findAll
	    val usersJson = Json.obj(
	    	"users"	-> {
  		    	users.map(user => Json.obj(
	    	  	    "userName" -> user.userName,
	        	    "email"    -> user.email,
	        	    "password" -> user.password,
	        	    "role"	   -> user.role,
	        	    "primaryLoc" -> user.primaryLoc
  		    ))}
	    )
	    
	    Json.toJson(usersJson)
	    Ok(usersJson)
	}
	
}