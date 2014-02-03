import org.specs2.mutable._
import org.specs2.mutable.Specification
import org.specs2.runner._
import org.junit.runner._

import play.api.test._

import play.api.libs.json._

import play.api.test._
import play.api.test.Helpers._
import play.api.test.Helpers.await

import play.api.libs.ws._
import play.api.mvc.Results._

import java.lang.Object

import java.util.Calendar
import java.text.SimpleDateFormat


// To run this test open two terminal windows and go to the Terra Traveler directory    
//    On one window type "play" and wait until you get the $ prompt the type 
//        $ start -Dhttp.port=9998
//    In the other terminal window type: play test

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 * 
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {
  
	val serverLocation = "http://localhost:9998"

  "Application" should {
    
    
	import controllers._
	
	println("\n\n\n\n==================== Test Started " + Calendar.getInstance().getTime() + "=======================")
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


	"test WS logic" in new WithServer {
	
		// API call testing
	  
	  
		 // Create new user, check for duplicate user, get user id
	     // then compare new user and user obtained from user id
	     var newUser = ttt_testCreateUser(ttt_getUserObject, false)
		 ttt_testCreateUser(ttt_getUserObject, true) // Check duplicate name	     
		 var id = ttt_getUserIdValue(newUser)  	     
	     var userFromId = ttt_getUserById(id)
	     ttt_verifyNewUserHasCorrectData(ttt_getUserObject, newUser, userFromId)
	     
	     // Create user profile then get user profile and compair the data
	
	}
		

  } // End of "Application" should 
  
	
 	def ttt_getUserById(id: String): String = {
	    println("Start - ttt_getUserById" + id)
  	  
  		var temp = Helpers.await(WS.url(serverLocation + "/api/v1/users/" + id.trim()).get()).body
  		
  		temp must contain(""""id":""" + id.trim() )  
	    
	    temp    
  	}
 	
 	def ttt_testCreateUser(user: JsObject, checkDuplicateUserName: Boolean): String = {
		println("--- API - Create User")
		
		var temp = Helpers.await(WS.url(serverLocation + "/api/v1/users")
		      .post(user)).body 
	    
		// Duplicate userName returns an error message web page      
		if (checkDuplicateUserName == true)  {
			temp must contain("This exception has been logged with id")		  
		} 
		else {
		
			temp must contain("userName")
		      	      
			//Check for duplicate user name
			temp must not contain("PSQLException: ERROR: duplicate key value violates unique constraint")
			temp must not contain("This exception has been logged with id")
		}  
	    
	    // TODO - return temp as JSON instead of a string
	    temp
 	}
 	
 	
 	def ttt_getUserObject(): JsObject = {
  	  
  		// TODO add the ability to create random users and user data.
  	  
		var user = Json.obj(
    		"userName" -> "User82", 
    		"email" -> "PJ@sample.com", 
    		"password" -> "secret", 
    		"role" -> "NORM", 
    		"latitude" -> 37.774932, 
    		"longitude" -> -122.419420
    	)
	    	
	    user
  	}
 	
 	def ttt_getUserIdValue(result: String): String = {
 	    // Return the value of the user Id
 	  
		var x =  result.indexOf(""""id":""")		
		var id = result.substring(x + 5, result.indexOf(",", x + 1))
		return id
 	 }
 	
 	def ttt_verifyNewUserHasCorrectData (user: JsObject, newUser: String, userFromId: String) {
 	  
 	}
     
}  // end of class ApplicationSpec



