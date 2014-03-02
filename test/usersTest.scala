
import org.specs2.mutable._
import org.specs2.mutable.Specification
import org.specs2.runner._
import org.junit.runner._

import play.api.libs.json._

import play.api.test._
import play.api.test.Helpers._
import play.api.test.Helpers.await

import play.api.mvc.Results._

import java.lang.Object

import controllers._

import scala.util.Random

// =================
//
//  NOTE: Users tests will be moved to this file
//
// ==================

object UsersTests extends ApplicationSpec {
  

	
	def ttt_Users_getUserById(userId: Long): JsValue ={

		/*
				GET		/api/v1/users/:id
				
				example request
				curl \
					--header "Content-type: application/json" \
					--request GET \
					--data '{}' \
					ec2-54-193-80-119.us-west-1.compute.amazonaws.com:9000/api/v1/users/1 \
					| python -mjson.tool
				
				example response
				{
				    "user": {
				        "email": "richard@aqume.com",
				        "id": 1,
				        "password": "secret",
				        "primaryLoc": 42,
				        "role": "BIZ",
				        "userName": "Homer"
				    }
				}
		*/

	  
		var (passFailStatus:Boolean, temp:JsValue) = TestCommon.ttt_sendApiCommand(Json.obj(), "users/" + userId.toString.trim, "Get User By Id")

		temp
	}

			
	// =================================================================================
	//                          ttt_Users_createUser
	//
	// The function will create a user at a fixed longitude and latitude	
	//
	// The function ttt_Users_createUserWithRangeAndBearing will create a user at a
	//     distance and compass bearing from the current longitude and latitude
	// The function ttt_Users_createUserWithinRadius will create a user with a random 
	//     latitude and longitude within a radius.
	//	
 	def ttt_Users_createUser (latitude:Double, longitude:Double, role:String): (Long, JsValue) = {
 
 	  // TODO - Not finished
 	  
 		/*
			curl \
				--header "Content-type: application/json" \
				--request POST \
				--data '{ "userName" : "PJ", "email" : "PJ@sample.com", "password" : "secret", "role" : 
				"NORM", "latitude" : 37.774932, "longitude" : -122.419420}' \
				ec2-54-193-80-119.us-west-1.compute.amazonaws.com:9000/api/v1/users \
				| python -mjson.tool
			
			example response
			{
			    "user": {
			        "email": "PJ@sample.com",
			        "id": 30,
			        "password": "secret",
			        "primaryLoc": 7,
			        "role": "NORM",
			        "userName": "PJ"
			    }
			} 		
 		 */
 	  	  
		var(userName:String, email:String, password:String) = TestCommon.ttt_generateUserNameEmailAndPassword
		
 		var userSent = Json.obj(
    		"userName" -> userName, 
    		"email" -> email, 
    		"password" -> password, 
    		"role" -> role, 
    		"latitude" -> latitude,  
    		"longitude" -> longitude
    	)
   
    	var (passFailStatus:Boolean, temp:JsValue) = TestCommon.ttt_sendApiCommand(userSent, "users", "Create User")
    	    
    	var id:Long = 0;
		
		if (passFailStatus == true) {
			id = (temp \ "user" \ "id").as[Long]
		}
    	 
		return (id, temp)	    
 	} 

	
	// =================================================================================
	//                          ttt_Users_getUserProfile
	// 
	//  Gets user profile from user ID
	//
	def ttt_Users_getUserProfile(userId:Long)(): JsValue = {
		/*
		 *  API documentation version 20
		 * 
			example request
			curl \
				--header "Content-type: application/json" \
				--request GET \
				--data '{}' \
				localhost:9000/api/v1/users/1/profile \
				| python -mjson.tool
			
			example response
			{
			    "userProfile": {
			        "bio": "I yam what I yam",
			        "birthdate": null,
			        "firstName": "Ricky",
			        "gender": "male",
			        "id": 1,
			        "lastName": "Ricardo",
			        "nationality": "http://www.codemonkey.com",
			        "story": null,
			        "userId": 1
			    }
			}
		*/
	  
		var (passFailStatus:Boolean, temp:JsValue) = TestCommon.ttt_sendApiCommand(Json.obj(), 
		    "users/" + userId.toString.trim + "/profile", "Get User Profile")
	  
	  	    
		 temp
	}
	
	
	// =================================================================================
	//                      ttt_Users_createUserProfile
	//
	def ttt_Users_createUserProfile(profile:JsValue): (Long, JsValue) = {
	  
		/*
		 *  API documentation version 20
		 *  
			example request
			curl \
				--header "Content-type: application/json" \
				--request POST \
				--data '{"userId" : 1, "firstName" : "Raymond", "lastName" : "Zamora", "gender" : "M", "birthdate" : "1990-02-23 10:30:00.0", "nationality" : "Brazilian", "portraitUrl" : "http://www.me.jpg", "bio" : "I began life as a small child.", "story" : "I am a very tough guy."}' \
				ec2-54-193-80-119.us-west-1.compute.amazonaws.com:9000/api/v1/users/profile \
				| python -mjson.tool
			
			example response
			{
			    "userProfile": {
			        "bio": "I began life as a small child.",
			        "birthdate": 635760000000,
			        "firstName": "Zelda",
			        "gender": "F       ",
			        "id": 12,
			        "lastName": "Zamora",
			        "nationality": "Brazilian",
			        "portraitUrl": "http://www.me.jpg",
			        "story": "I am a very exotic lady.",
			        "userId": 1
			    }
			}
		 */

	  
		var (passFailStatus:Boolean, temp:JsValue) = TestCommon.ttt_sendApiCommand(profile,
		    "users/profile", "Create User Profile")

		var proFileId:Long = 0;
		if (passFailStatus == true) {
			proFileId = (temp \ "userProfile" \ "id").as[Long]
		  
		}

	  
//		var temp = Json.parse(Helpers.await(WS.url(TestCommon.serverLocation + "/api/v1/users/profile").post(profile)).body)
		
		    
//		    var profileId = (temp \ "userProfile" \ "id").as[Long]
		
		return (proFileId, temp)
	}  // End of ttt_Users_createUserProfile
	
	
	
	// =================================================================================
	//                ttt_generateUserProfile
	//
	// This function will generate a random profile so we won't have a bunch
	// of users with the exact same profile.
	//
	def ttt_generateUserProfile(userId: String): JsValue = {

	  // TODO - Not finished and not tested
	  
			/*
example request
			curl \
				--header "Content-type: application/json" \
				--request POST \
				--data '{"userId" : 1, "firstName" : "Raymond", "lastName" : "Zamora", "gender" : "M", "birthdate" : "1990-02-23 10:30:00.0", "nationality" : "Brazilian", "portraitUrl" : "http://www.me.jpg", "bio" : "I began life as a small child.", "story" : "I am a very tough guy."}' \
				ec2-54-193-80-119.us-west-1.compute.amazonaws.com:9000/api/v1/users/profile \
				| python -mjson.tool
						
			example response
			{
			    "userProfile": {
			        "bio": "I began life as a small child.",
			        "birthdate": 635760000000,
			        "firstName": "Zelda",
			        "gender": "F       ",
			        "id": 12,
			        "lastName": "Zamora",
			        "nationality": "Brazilian",
			        "portraitUrl": "http://www.me.jpg",
			        "story": "I am a very exotic lady.",
			        "userId": 1
			    }
			}
		*/
	  
		var firstMale = Array("John", "Tom", "Jon", "Tim", "Jun", "James", "Rick", "Casey", "Mark", "Jack", "Sam")
		var firstFemale = Array("Sally", "Pam", "Jan", "Sarah", "Susie", "Mary", "Marie", "Sue")
		var lastNames = Array("Jones", "Johnson", "Smith", "Voelm", "Anderson", "Walker", "Gibson", "Thompson")
		var theGender = Array("M", "F")
	
		var firstName = ""
		var lastName = ""

		
		var gender = theGender(Random.nextInt(theGender.length))
		if (gender == "M") {firstName = firstMale(Random.nextInt(firstMale.length))}
		if (gender == "F") {firstName = firstFemale(Random.nextInt(firstFemale.length))}
		
		lastName  = lastNames(Random.nextInt(lastNames.length))
			
		// TODO - Create random birth date, nationality, portrait, bio and history

		
		var userProfile = Json.obj(
		    "userId" -> userId, 
		    "firstName" -> firstName, 
		    "lastName" -> lastName, 
		    "gender" -> gender,
			"birthdate" -> "1990-02-23 10:30:00.0",
		    "nationality" -> "Brazilian", 
		    "portraitUrl" -> "http://www.me.jpg",
		    "bio" -> "I began life as a small child.", 
		    "story" -> "I am a very exotic person."
	   )

println ("User profile id = " + userId)
println ("User profile JSON = " + userProfile)

	   var (passFailStatus:Boolean, temp:JsValue) = TestCommon.ttt_sendApiCommand(userProfile,
		   "users/profile", "Generate User Profile")

		   
	   temp
	}

	// =================================================================================
	//                   ttt_Users_associateUserAndEvent
	//

	def ttt_Users_associateUserAndEvent(userId:Long, eventId:Long): (Boolean, JsValue) = {
		// API Documentation version 20
		/*
			example request
			curl \
				--header "Content-type: application/json" \
				--request POST \
				--data '{}' \
				localhost:9000/api/v1/events/2/user/1 \
				| python -mjson.tool
			
			example response   [CORRECTED]
			{
			    "event": {
			        "activityCategories": [
			            1,
			            2
			        ],
			        "activityType": 22,
			        "description": "Outside Lands: best music festival in S.F.",
			        "from": 1393142400000,
			        "id": 1,
			        "lat": 37.768395,
			        "lon": -122.492259,
			        "maxSize": 50,
			        "minSize": 2,
			        "placeId": 1,
			        "rsvpTotal": null,
			        "title": "Outside Lands",
			        "to": null,
			        "waitListTotal": null
			    },
			    "status": "success",
			    "userId": 1,
			    "user_event-PK": 2
			}
		*/

	  
println("=========== User id =  " + userId)
println("=========== Place id = " + eventId)
	  
	  
		var (passFailStatus:Boolean, results:JsValue) = TestCommon.ttt_sendApiCommand(Json.obj(), 
		    "events/" + eventId.toString.trim() + "/user/" + userId.toString.trim(), "Associate User and Event")
		
	  
	    return (passFailStatus, results)
		    
	    	   
	} // End of ttt_Users_associateUserAndEvent
	

	
 	
} // End of UsersTests object



