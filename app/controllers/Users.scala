package controllers

import models._
import views._
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
	
	def createUser = Action { implicit request =>
	    render {
	        case Accepts.Json() => {
	            request.body.asJson.map { json =>
			        val userName 	= (json \ "userName").as[String]
			        val email    	= (json \ "email").as[String]
			        val password 	= (json \ "password").as[String]
			        val role 	 	= (json \ "role").as[String]
			        val primaryLoc 	= (json \ "primaryLoc").as[Option[Long]]
			        			        
			        val newUser  = User(NotAssigned, null, null, null, userName, email, password, role, primaryLoc)
			        val pk = User.create(newUser)
			        			        
			        val persistedUser = User.findById(pk.get)
			        			        
			        persistedUser match {
			            case Some(persistedUser) => {
			                val jsonResp = Json.obj( "user" -> {
			                    Json.obj(
				                    "id"		 -> persistedUser.id.get,
					    	  	    "userName"   -> persistedUser.userName,
					        	    "email"      -> persistedUser.email,
					        	    "password" 	 -> persistedUser.password,
					        	    "role"	     -> persistedUser.role,
					        	    "primaryLoc" -> persistedUser.primaryLoc
					        	)
				            })
				            Ok(jsonResp)
			            }
			            case None => BadRequest("User not found")
			        }
				}.getOrElse {
					BadRequest("Expecting Json data")
				}
	        } // End - case Accpts.Json()
	        
	        case Accepts.Html() => {
	            val newUser:User = userForm.bindFromRequest.get
			    User.create(newUser)
			    Redirect(routes.Users.allUsers)
	        }
	    }
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
	
	def registrationForm = userForm
	
	def allUsers = Action { implicit request =>
	    val users = User.findAll
	    render {
	        case Accepts.Json() => {
	            val usersJson = Json.obj(
            		"users"	-> {
            			users.map(user   => Json.obj(
			    	  	    "userName"   -> user.userName,
			        	    "email"      -> user.email,
			        	    "password" 	 -> user.password,
			        	    "role"	     -> user.role,
			        	    "primaryLoc" -> user.primaryLoc
            			))
            		}
        		)
	    
			    Json.toJson(usersJson)
			    Ok(usersJson)
	        }
	        case Accepts.Html() => Ok(views.html.protoUsers(users))
	    }   
	}
	
	def byEmail(email: String) = Action { implicit request =>
		User.findByEmail(email).map { user =>
	      	user.primaryLoc match {
	      	  	case Some(locId) => 
	      	  	  	Ok(html.protoLocation(Location.single(locId))(user))
	      	  	case _ => 
	      	  	  	Ok(html.protoLocation(null)(user))
	      	}
	    }.getOrElse(Forbidden)	
	}
	
	def profile(userId: Long) = Action { implicit request =>
	    User.findById(userId).map { user =>
		  	val profile = UserProfile.findProfile(userId)
		  	Ok(html.protoProfile(user, profile))
	    }.getOrElse(Forbidden)
	}
	
	def contacts(userId: Long) = Action { implicit request =>
	    val users = User.contacts(userId)
	    Ok(html.protoContacts(users))
	}
	
	def itinerary(userId: Long) = Action { implicit request =>
	    val itineraries = Itinerary.findByUserId(userId).map { itinerary =>
	        val items = ItineraryItem.findById(itinerary.id.get)
	        (itinerary, items)
	    }
	    Ok(html.protoItinerary(itineraries))
	}
}





