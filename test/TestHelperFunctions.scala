import org.specs2.mutable._
import org.specs2.mutable.Specification
import org.specs2.matcher.JsonMatchers
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

import scala.util.Random



object TestHelperFunctions {
  
	val serverLocation = "http://localhost:9998"
	  
	  
	  
	  
 	def ttt_getValue(str: String, name: String): String = {
 	  
  	   var value = ""
 	  			
 	   if (str.length > 0) {
 		   var x = str.indexOf(""""""" + name + """"""") + name.length + 3
 		   value = str.slice(x,str.indexOf(",", x))
 	   }
 		
  		return value 
 	}
  
	def ttt_generateUserName(): String = {
	  // The "ApplicationSpec.currentCount" increments a counter to avoid
	  // getting duplicate user names
	  
	  // TODO - Store the counter number between tests or find the last id in database
	  // TODO - Currently you have to erase the database or increment the 
	  // TODO -     variable "count" near the bottom of ApplicationSpec.scala
	  
	  var userName = "BadUser" + ApplicationSpec.currentCount.toString.trim()
	  userName
	}
	
	def ttt_generateUserProfile(userId: String): String = {
	   /*	  
			curl \
			--header "Content-type: application/json" \
			--request POST \ 
			--data '{"userId" : 1, "firstName" : "Zelda", "lastName" : "Zamora", "gender" : "F",
			"birthdate" : "1990-02-23 10:30:00.0", "nationality" : "Brazilian", "portraitUrl" : "http://www.me.jpg", "bio" : "I began life as a small child.", "story" : "I am a very exotic lady."}' \ 
			localhost:9998/api/v1/users/profile
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
	   
	   var temp = Helpers.await(WS.url(serverLocation + "/api/v1/users/profile").post(userProfile)).body
		   
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
  	}  // End of ttt_generateUserObject
  	
 
  
  
}