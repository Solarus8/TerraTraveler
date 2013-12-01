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

object Application extends Controller {
	 
	def index = Action {
		Redirect(routes.Application.allUsers)
	}
  
	def createUserJson = Action { implicit request =>
	    request.body.asJson.map { json =>
	        val userName = (json \ "userName").as[String]
	        val email    = (json \ "email").as[String]
	        val password = (json \ "password").as[String]
	        
	        val newUser  = User(NotAssigned, userName, email, password)
			val quizId   = User.create(newUser)
			
			Redirect(routes.Application.allUsers)
		}.getOrElse {
			BadRequest("Expecting Json data")
		}
	}
	
	def createUser = Action { implicit request =>
	    val user: User = userForm.bindFromRequest.get
	    User.create(user)
	    Redirect(routes.Application.allUsers)
	}
		
	val userForm = Form(
		mapping(
			"userName" -> text,
			"email"    -> text,
			"password" -> text
		)((userName, email, password) => User(NotAssigned, userName, email, password))
		 ((user: User) => Some(user.userName, user.email, user.password))
	)
	
	def allUsers = Action {
	    val users = User.findAll	    
	    Ok(views.html.proto(users, userForm))
	}
	
	def allUsersJson = Action {
	    val users = User.findAll
	    val usersJson = Json.obj(
	    	"users"	-> {
  		    	users.map(user => Json.obj(
	    	  	    "userName" -> user.userName,
	        	    "email"    -> user.email,
	        	    "password" -> user.password
  		    ))}
	    )
	    
	    Json.toJson(usersJson)
	    Ok(usersJson)
	}
}


