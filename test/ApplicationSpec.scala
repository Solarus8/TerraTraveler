import org.specs2.mutable._
import org.specs2.mutable.Specification
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import play.api.test.Helpers.await
import play.api.libs.json._
import play.api.libs.ws._
import play.api.mvc.Results._
import play.api.Play
import play.api.Play.current
import play.api.Application

import scala.io.Source._


import java.lang.Object
import java.util.Calendar
import java.text.SimpleDateFormat
import java.io._
import java.io.PrintWriter
import java.io.File


// To run this test 
//     1. Start the oostgresql server
//     2. Open two terminal windows and go to the Terra Traveler directory    
//        a. On one window type "play" and wait until you get the $ prompt the type 
//           $ start -Dhttp.port=9998
//        b. In the other terminal window type: play test
//


/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 * 
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {
  

	val serverLocation = "http://localhost:9998"
	  

	"Terra Traveler Test" should {
  
	
		println("\n\n\n\n==================== Test Started " + Calendar.getInstance().getTime() + " =======================")
		var temp = ""
		
		
		println("--- Web Page - check for 404 error condition")
		"send 404 on a bad request" in new WithApplication{
			  route(FakeRequest(GET, "/boum")) must beNone
		}
		
		println("--- Web Page - check for application ready on web page")
		"render the index page" in new WithApplication{
			val home = route(FakeRequest(GET, "/")).get
			
			status(home) must equalTo(OK)
			contentType(home) must beSome.which(_ == "text/html")
			contentAsString(home) must contain ("Terra Traveler app is ready.")
		}
		

	
		"Users API test" should
		{
			"Create new user and verify user was created" should {
			  			
				// Gernerate user object	
				var user =  ttt_generateUserObject(37.774932, -122.419420, 1000, true)
	 			
				// Create new user 
				var newUser = ttt_testCreateUser(user, false)
		
				// Duplicate users 
				ttt_testCreateUser(user, true) // Check duplicate name	
				
				// Get user Id value from newUser
				var id = ttt_getUserIdValue(newUser)  
				
				// Use user id from previous step to get user by Id
				var userFromId =  ttt_getUserById(id)
				
				// Verify 
				ttt_verifyNewUserHasCorrectData(user, newUser, userFromId)
				
			
				"End of users test" in {			
					"End of users test" must contain("End")
				}
							
				  
			} // End of Users API test
		

		
			"Edge Tests - Create New User /api/v1/users" should {
				
				"Create User - send commands with missing data" in {
					"TODO write tests" must beEqualTo( "Not done yet")
				}
				  
				"Create User - Use latitude and longitude greater than 360" in {
					"TODO write tests" must beEqualTo( "Not done yet")
				}
				
				"Create User - Send too many parameters" in {
					 "TODO write tests" must beEqualTo( "Not done yet")
			  	}
				
				"Create User - Send too few parameters" in {
					 "TODO write tests" must beEqualTo( "Not done yet")
				}
				
				"Create User - Send badly formed JSON" in  {
					 "TODO write tests" must beEqualTo( "Not done yet")
				}
			
			} // End of Create User edge tests
		
			"Edge Tests - Get User By Id /api/v1/users/1:" should {
			  
				"Get User By Id - Send negative number" in {
					// "status": "None")
					"TODO write tests" must contain( "Not done yet")			  
				}
				
				"Get User By Id - Send text" in {
					// No JSON object could be decoded
					"TODO write tests" must contain( "Not done yet")
				}
			}

		
			"This is where the user profile tests will be created" should {
				"User profile test will be created here" in {
					"TODO - Write user profile tests" must contain("test")
			}
				
				
		} // End of the Users API test
		
	} // End of Terra Traveler Test should

  } // End of "Application" should 
  

// TODO - Move these functions to another file
// TODO - Move these functions to another file
// TODO - Move these functions to another file
	def ttt_generateUserObject(latitude: Double, longitude: Double, radius: Int, randomLocation: Boolean): JsObject = {
 		// parameters
 	    // latitude
 	    // longitude
 	    // radius:  For creating random location based on the radius form the longitude
 	    //          and latitude. Radius in meters
 	    // randomLocation: true - creates a random location based on latitude and longitude
 	    //                 false - no change is latitude and longitude
 
	  
		// userName uses the format name plus a unique number	 	  
 		var userName = "User" + ApplicationSpec.currentCount.toString.trim()

 	  //  var userName = "User142" // TODO add the ability automatically create unique user names
 	    var email = userName + "@gmail.com"
 	    var password = userName + "secret"
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
  	}
	
	
 	def ttt_getUserById(id: String): String = {
	    println("ttt_getUserById, id = " + id )
	    
	  /*
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
 	   */
	    
	    var temp = ""
	    
	    "ttt_getUserById - Get user from this id: " + id in {
	      
	    	temp = Helpers.await(WS.url(serverLocation + "/api/v1/users/" + id.trim()).get()).body
  		
			temp must contain(""""id":""" + id.trim() )  
	    }
	    
	    println("######## " + temp)
	       
	    temp    
  	}
 	
 	
 	def ttt_testCreateUser(user: JsObject, checkDuplicateUserName: Boolean): String = {
 	  
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
 	  		
		var temp = Helpers.await(WS.url(serverLocation + "/api/v1/users")
		      .post(user)).body 
		      
	     println("^^^^^^^^" + temp + "^^^^^^^^^")
	    
		// Duplicate userName returns an error message web page      
		if (checkDuplicateUserName == true)  {
			"ttt_testCreateUser - Attempt to create user with duplicate name" in {
		  
				temp must contain("This exception has been logged with id")	
			}
		} 
		else {
		  
			"ttt_testCreateUser - Create new user" in {
	
			  println("***********" + temp)
			  
				temp must contain("userName")
			      	      
				//Check for duplicate user name
				temp must not contain("PSQLException: ERROR: duplicate key value violates unique constraint")
				temp must not contain("This exception has been logged with id")
			  
			}
		}  
	
		
		
		println("((((((((((((" + temp)
	    temp
 	}
 	
 	

 	
 	def ttt_getUserIdValue(result: String): String = {
 	    // Return the value of the user Id
 	  
		var x =  result.indexOf(""""id":""")		
		var id = result.substring(x + 5, result.indexOf(",", x + 1))
		return id
 	 }
 	
 	def ttt_verifyNewUserHasCorrectData (user: JsObject, newUser: String, userFromId: String) {
 		println("ttt_verifyNewUserHasCorrectData")
 		
 		//println("\n====== user =====\n" + user + "\n==== newUser ======\n" + newUser + "\n===== userFromId ====\n" + userFromId)
 
 		
 		
 		val tempUser = user.toString
 		
 		"ttt_verifyNewUserHasCorrectData - Verify the user that was just created has the correct data" in {
 			newUser must contain (userFromId)
 		  
 		}
 		
 		// Compare User that was created with the data from Get User by ID
 		
 		
 		// TODO - remove "id":xx from string since original user object doen't have an id
 		
 		//newUser must contain (tempUser)
 	}
     
}  // end of class ApplicationSpec

// Each user name has a number that gets incremented to avoid duplicate users
object incrementUserName {

}

// Incrementing userName
object ApplicationSpec {
	var count = 205

	def currentCount(): Long = {
	    count += 1
	    count
	}
  
}

