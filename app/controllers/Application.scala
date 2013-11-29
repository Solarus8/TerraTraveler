package controllers

import models.User
import anorm.NotAssigned
import play.api.mvc._
import play.api.libs.json.Json._
import play.api.libs.json._

object Application extends Controller {
	 
	def index = Action {
		Redirect(routes.Application.allUsers)
	}
  
	def createUser = Action { implicit request =>
	    request.body.asJson.map { json =>
	        val userName = (json \ "userName").as[String]
	        val email = (json \ "email").as[String]
	        val password = (json \ "password").as[String]
	        
	        val newUser = User(NotAssigned, userName, email, password)
			val quizId = User.create(newUser)
			
			Redirect(routes.Application.allUsers)
		}.getOrElse {
			BadRequest("Expecting Json data")
		}
	}
	
	def allUsers = Action {
	    val users = User.findAll
	    val usersJson = Json.obj(
	    	"users"	-> {
  		    	users.map(user => Json.obj(
	    	  	    "userName"	-> user.userName,
	        	    "email" 	-> user.email,
	        	    "password"	-> user.password
  		    ))}
	    )
	    
	    Json.toJson(usersJson)
	    Ok(usersJson)
	}
}


