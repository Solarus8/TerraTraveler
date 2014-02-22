
import org.specs2.mutable._
import org.specs2.mutable.Specification
import org.specs2.runner._
import org.junit.runner._

import play.api.libs.json._

import play.api.test._
import play.api.test.Helpers._
import play.api.test.Helpers.await

import play.api.libs.ws._
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
	  
		var temp = Json.parse(Helpers.await(WS.url(TestCommon.serverLocation + "/api/v1/users/" + userId.toString.trim).get()).body)

		temp
	}

	
	
	// This function will take the latitude and longitude passed to it and create at
	// use at the range and compass bearing passed to it.
	def ttt_Users_createUserWithRangeAndBearing(latitude:Double, longitude:Double, rangeMeters:Double, bearing:Long): String = {

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
	  
	  
	  
	  
		return "something"
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
 	  
// TODO - Add test to verify user created data matches the data coming back from 
// TODO -    the server and abort test if it fails.  All other tests will fail
 	  
		var(userName:String, email:String, password:String) = TestCommon.ttt_generateUserNameEmailAndPassword
		
 		var userSent = Json.obj(
    		"userName" -> userName, 
    		"email" -> email, 
    		"password" -> password, 
    		"role" -> role, 
    		"latitude" -> latitude,  
    		"longitude" -> longitude
    	)
 
    	
    	var temp:JsValue = Json.obj()
    	var id:Long = 0
    	
    	try{  		    	
			temp = Json.parse(Helpers.await(WS.url(TestCommon.serverLocation + "/api/v1/users").post(userSent)).body) 			
			id = (temp \ "user" \ "id").as[Long]
    	} catch {
			case e: Exception => println("exception caught: " + e);
		}
  
		return (id, temp)	    
 	} 

 
 	
 	// This not part of any Terra Traveler test but is being used for other testing
	def runEdgeTests() {
		"User Edge Tests" should {
			"Test1" in {
			  
				1 must beEqualTo(1)
			}
			
			test2
			
			"Test3" in {
			  
				3 must beEqualTo(3)
			}		
		  
		  
		}
	  
	}
	
	// This not part of any Terra Traveler test but is being used for other testing
	def test2() {
		"Test2" in {
		  
			2 must beEqualTo(2)
		}	
		
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
			        "firstName": "Richard",
			        "gender": "male",
			        "id": 1,
			        "lastName": "Walker",
			        "nationality": "http://www.codemonkey.com",
			        "story": null,
			        "userId": 1
			    }
			}
		*/
	  
		var temp = Json.parse(Helpers.await(WS.url(TestCommon.serverLocation + "/api/v1/users/" 
		    + userId.toString.trim + "/profile").get()).body)	  
	  	    
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
	  	  	  	  
		var temp = Json.parse(Helpers.await(WS.url(TestCommon.serverLocation + "/api/v1/users/profile").post(profile)).body)
		var profileId = (temp \ "userProfile" \ "id").as[Long]
		
		return (profileId, temp)
	}
	
	
	
	// =================================================================================
	//                ttt_generateUserProfile
	//
	// This function will generate a random profile so we won't have a bunch
	// of users with the exact same profile.
	//
	def ttt_generateUserProfile(userId: String): String = {

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
	   
	   var temp = Helpers.await(WS.url(TestCommon.serverLocation + "/api/v1/users/profile").post(userProfile)).body
		   
	   temp
	}
		
	def ttt_generateUserObject(latitude: Double, longitude: Double, radius: Int, randomLocation: Boolean): JsObject = {
 		// parameters
 	    // latitude
 	    // longitude
 	    // radius:  For creating random location based on the radius form the longitude
 	    //          and latitude. Radius in meters
 	    // randomLocation: true - creates a random location based on latitude and longitude
 	    //                 false - no change is latitude and longitude
 
		var (userName:String, email:String, password:String) = TestCommon.ttt_generateUserNameEmailAndPassword	  
 	    var role = "NORM"
 	      
 	    if (randomLocation == true) {
 	    	// TODO - add code for random location within radius
 	    }
  			   	  
		var user = Json.obj(
    		"userName" -> userName, 
    		"email" -> email, 
    		"password" -> password, 
    		"role" -> role, 
    		"latitude" -> latitude,  
    		"longitude" -> longitude
    	)
	    	
	    user
  	}  // End of ttt_generateUserObject
  	
 	
}



