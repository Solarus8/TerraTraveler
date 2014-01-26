package controllers

import java.util.Date

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
	    request.body.asJson.map { json =>
	        println("Users.createUser - json: " + json)
	        
	        val userName 	= (json \ "userName").validate[String]
	        //println("Users.createUser - userName: " + userName)
	        val email    	= (json \ "email").validate[String]
	        //println("Users.createUser - email: " + email)
	        val password 	= (json \ "password").validate[String]
	        //println("Users.createUser - password: " + password)
	        val role 	 	= (json \ "role").validate[String]
	        //println("Users.createUser - role: " + role)
	        val latitude	= (json \ "latitude").validate[Double]
	        //println("Users.createUser - latitude: " + latitude)
	        val longitude	= (json \ "longitude").validate[Double]
	        //println("Users.createUser - longitude: " + longitude)
	        
	        val newUserLoc	= Location(NotAssigned, null, null, null, null, null, null, latitude.get, longitude.get, null, null, null)
	        //println("Users.createUser - newUserLoc: " + newUserLoc)
	        val newLocPK = Location.create(newUserLoc)
	        // println("Users.createUser - newLocPK: " + newLocPK)
	        			        
	        val newUser  = User(NotAssigned, null, null, null, userName.get, email.get, password.get, role.get, newLocPK.get)
	        val newUserPK = User.create(newUser)
	        			        
	        val persistedUser = User.byId(newUserPK.get)
	        			        
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
	}
	
	def createUserProfile = Action { implicit request =>
	    println("Users.createUserProfile - TOP")
	    request.body.asJson.map { json =>
	        println("Users.createUserProfile - json: " + json)
	        
	        val userId			= (json \ "userId").validate[Int]
	        		println("Users.createUserProfile - userId" + userId)
			val firstName		= (json \ "firstName").validate[Option[String]]
					println("Users.createUserProfile - firstName" + firstName)
			val lastName		= (json \ "lastName").validate[Option[String]]
					println("Users.createUserProfile - lastName" + lastName)
			val gender			= (json \ "gender").validate[Option[String]]
					println("Users.createUserProfile - gender" + gender)
			val birthdate		= (json \ "birthdate").validate[Option[Date]]
					println("Users.createUserProfile - birthdate" + birthdate)
			val nationality		= (json \ "nationality").validate[Option[String]]
					println("Users.createUserProfile - nationality" + nationality)
			val portraitUrl		= (json \ "portraitUrl").validate[Option[String]]
					println("Users.createUserProfile - portraitUrl" + portraitUrl)
			val bio				= (json \ "bio").validate[Option[String]]
					println("Users.createUserProfile - bio" + bio)
			val story			= (json \ "story").validate[Option[String]]
					println("Users.createUserProfile - story" + story)
	        
	        val newProfile = UserProfile(
                NotAssigned, 
                userId.get, 
                firstName.get, 
                lastName.get, 
                gender.get, 
                birthdate.get, 
                nationality.get, 
                portraitUrl.get, 
                bio.get, 
                story.get
            )
            val newUserProfilePK = UserProfile.create(newProfile)
            val persistedUserProfile = UserProfile.findById(newUserProfilePK.get)
            persistedUserProfile match {
	            case Some(persistedUserProfile) => {
	                val jsonResp = Json.obj( "userProfile" -> {
	                    Json.obj(
							"id"    	 	-> persistedUserProfile.id.get,
							"userId" 		-> persistedUserProfile.userId,
							"firstName" 	-> persistedUserProfile.firstName,
							"lastName"		-> persistedUserProfile.lastName,
							"gender" 		-> persistedUserProfile.gender,
							"birthdate" 	-> persistedUserProfile.birthdate,
							"nationality" 	-> persistedUserProfile.nationality,
							"portraitUrl" 	-> persistedUserProfile.portraitUrl,
							"bio"			-> persistedUserProfile.bio,
							"story"			-> persistedUserProfile.story
			        	)
		            })
		            Ok(jsonResp)
	            }
	            case None => BadRequest("UserProfile not found")
	        }
		}.getOrElse {
			BadRequest("Expecting Json data")
		}
	}
		
	def allUsers = Action { implicit request =>
	    val users = User.findAll
        val usersJson = Json.obj(
    		"users"	-> {
    			users.map(user   => Json.obj(
    			    "id"		 -> user.id.get,
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
	
	/*def byEmail(email: String) = Action { implicit request =>
		User.findByEmail(email).map { user =>
	      	user.primaryLoc match {
	      	  	case Some(locId) => 
	      	  	  	Ok(html.protoLocation(Location.single(locId))(user))
	      	  	case _ => 
	      	  	  	Ok(html.protoLocation(null)(user))
	      	}
	    }.getOrElse(Forbidden)	
	}*/
	
	def byId(id: Long) = Action { implicit request =>
	    User.byId(id) match { 
	        case Some(user) => {
	             val userJson = Json.obj(
	                 "user" -> Json.obj(
	                      "id"		 	-> user.id.get,
			    	  	  "userName"   	-> user.userName,
			        	  "email"      	-> user.email,
			        	  "password" 	-> user.password,
			        	  "role"	    -> user.role,
			        	  "primaryLoc" 	-> user.primaryLoc
	                 )   
	             )
	             Ok(userJson)
	        }
	        case None => Ok(Json.obj("status" -> "None"))
	    }
	}
	
	def profile(userId: Long) = Action { implicit request =>
		val up = UserProfile.findProfile(userId) 
		up match {
		    case Some(persistedProfile) => {
		        val profile = Json.obj(
			        "userProfile" -> Json.obj(
			            "id" 			-> persistedProfile.id.get,
			            "userId"		-> persistedProfile.userId,
			            "firstName"		-> persistedProfile.firstName,
			            "lastName"		-> persistedProfile.lastName,
			            "gender"		-> persistedProfile.gender,
			            "birthdate"		-> persistedProfile.birthdate,
			            "nationality"	-> persistedProfile.portraitUrl,
			            "portraitUrl" 	-> persistedProfile.portraitUrl,
			            "bio"			-> persistedProfile.bio,
			            "story"			-> persistedProfile.story
			         )
		         )
		         Ok(profile)
		    }
		    case None => Ok(Json.obj("status" -> "None"))
		}
	}
	
	def contacts(userId: Long) = Action { implicit request =>
	    val users = User.contacts(userId)
	    NotFound // TEMP
	}
	
	def itinerary(userId: Long) = Action { implicit request =>
	    val itineraries = Itinerary.findByUserId(userId).map { itinerary =>
	        val items = ItineraryItem.findById(itinerary.id.get)
	        (itinerary, items)
	    }
	    NotFound // TEMP
	}
}





