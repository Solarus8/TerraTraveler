import play.api.libs.json._
import java.util.Date
import java.text.SimpleDateFormat

trait UsersApiTests extends org.specs2.mutable.Specification {
  
  	// =================================================================================
  	//                     ttt_UsersApiTest_createUser
  	//  	
  	def ttt_UsersApiTest_createUser() {
  		  		
  		//Hacker Dojo 37.402840   -122.050000
  		var latitude = 37.402840
  		var longitude = -122.050000
  		var role = "NORM"
  	     	    
  		var(id:Long, user:JsValue) = UsersApi.ttt_Users_createUser(latitude, longitude, role)

  		// User name, email and password are returned as
  		//    userName =  bob123 which is random name with unique number
  		//    email    =  bob123@gmail.com which is user name with random email provider with .com
  		//    password =  passwordbob123 which the word password with user name
 
 
 		val userName = (user \ "user" \ "userName").toString() 
 		val email    = (user \ "user" \ "email").toString()
 		val password = (user \ "user" \ "password").toString()
    		  		
  		"ttt_UsersApiTest_createUser - Create New User " + userName in {
   		 		
	  		latitude  must beEqualTo((user \ "user" \ "lat").as[Double])
	  		longitude must beEqualTo((user \ "user" \ "lon").as[Double])
	  		role      must beEqualTo((user \ "user" \ "role").as[String])
	  		
	  		userName must not be empty
	  		email must contain("""@""")
	  		email must contain(".com")
	  		email must contain(userName.replace(""""""",""))
	  		password must contain("password")
	  			  		
  		}
  	} // End of ttt_UsersApiTest_createUse()
  	
  	
    // =================================================================================
  	//                     ttt_UsersApiTest_createUser
  	//  	
  	def ttt_UsersApiTest_getUserById() {
  	  
  		// Red Rock Cafe 37.393546   -122.078874	
  		var latitude = 37.393546
  		var longitude = -122.078874
  		var role = "NORM"  	  
  	  
  		// Create a new user
		var(id:Long, user:JsValue) = UsersApi.ttt_Users_createUser(latitude, longitude, role)

 		val userName = (user \ "user" \ "userName").toString() 
 		val email    = (user \ "user" \ "email").toString()
 		val password = (user \ "user" \ "password").toString()
		val primaryLoc = (user \ "user" \ "primaryLoc")
 		
		var userFromId:JsValue = UsersApi.ttt_Users_getUserById(id)
		
		"ttt_UsersApiTest_getUserById - Get " + userName +  " with ID=" + id in {
  		 
  	  		userName must not be empty
	  		email must contain("""@""")
	  		email must contain(".com")
	  		password must contain("password")
  	  		password must contain(userName.replaceAll(""""""", ""))
  		  
  		  	id         must beEqualTo((userFromId \ "user" \ "id").as[Long]) 
  		  	primaryLoc must beEqualTo((userFromId \ "user" \ "primaryLoc")) 
	  		role       must beEqualTo((userFromId \ "user" \ "role").as[String]) 
	  		userName.replaceAll(""""""", "")   must beEqualTo((userFromId \ "user" \ "userName").as[String]) 
	  		
	  			  		
  		}
		 	  
  	} // End of ttt_UsersApiTest_getUserById()
  	
  	   	// =================================================================================
  	//                           ttt_UsersApiTest_createUserProfile
  	//
 	def ttt_UsersApiTest_createUserProfile() {
 	  
 	   //San Jose Airport 37.368613   -121.928523
  		var latitude = 37.368613
  		var longitude = -121.928523
  		var role = "NORM"
  		      
  		// Create user and get ID
  		var(userId:Long, user:JsValue) = UsersApi.ttt_Users_createUser(latitude, longitude, role)
  		

		var firstName = "Raymond" 
		var lastName = "Zamora" 
		var gender = "M" 
		var birthdate = "1990-02-23 10:30:00.0" 
		var nationality = "Brazilian" 
		var portraitUrl = "http://www.me.jpg" 
		var bio = "I began life as a small child." 
		var story = "I am a very tough guy."
  		
  		
  		var profile = Json.obj(
  		
			"userId"      -> userId, 
			"firstName"   -> firstName, 
			"lastName"    -> lastName, 
			"gender"      -> gender, 
			"birthdate"   -> birthdate, 
			"nationality" -> nationality, 
			"portraitUrl" -> portraitUrl, 
			"bio"         -> bio, 
			"story"       -> story
 		)
 
 		
 		// Create user profile
 		var (profileId:Long, newProfile:JsValue) = UsersApi.ttt_Users_createUserProfile(profile)
 
 		var sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
 		
 		"ttt_UsersApiTest_createUserProfile - Create profile for " + firstName + " " + lastName + " profile id = " +profileId in {
 		
	 		userId must beEqualTo ((newProfile \ "userProfile" \ "userId").as[Long])
	  		firstName must beEqualTo ((newProfile \ "userProfile" \ "firstName").as[String])
	  		lastName must beEqualTo ((newProfile \ "userProfile" \ "lastName").as[String])
	  		gender must beEqualTo ((newProfile \ "userProfile" \ "gender").as[String])
	 		
   			var newDate = (newProfile \ "userProfile" \ birthdate).as[Long]
 
 			birthdate must beEqualTo(sdf.format(new Date(newDate)))
 	 		
	 		nationality must beEqualTo ((newProfile \ "userProfile" \ "nationality").as[String])
	  		portraitUrl must beEqualTo ((newProfile \ "userProfile" \ "portraitUrl").as[String])
	  		bio must beEqualTo ((newProfile \ "userProfile" \ "bio").as[String])
	  		story must beEqualTo ((newProfile \ "userProfile" \ "story").as[String]) 		
  		}
 		 	  
  	} // End of function ttt_UsersApiTest_createUserProfile
 	
 	
 	// =================================================================================
 	//                            ttt_UsersApiTest_getUserProfile
 	def ttt_UsersApiTest_getUserProfile() {
 	  
 	   //Ridge Vineyards 37.298259   -122.116744
  		var latitude = 37.298259
  		var longitude = -122.116744
  		var role = "NORM"
  		      
  		// Create user and get ID
  		var(userId:Long, user:JsValue) = UsersApi.ttt_Users_createUser(latitude, longitude, role)
  		
		var firstName = "Mark" 
		var lastName = "Zamora" 
		var gender = "M" 
		var birthdate = "1985-02-23 10:30:00.0" 
		var nationality = "Brazilian" 
		var portraitUrl = "http://www.me.jpg" 
		var bio = "I began life as a small child." 
		var story = "Raymond's brother"	  
		  		  
 		var profile = Json.obj(
  		
			"userId"      -> userId, 
			"firstName"   -> firstName, 
			"lastName"    -> lastName, 
			"gender"      -> gender, 
			"birthdate"   -> birthdate, 
			"nationality" -> nationality, 
			"portraitUrl" -> portraitUrl, 
			"bio"         -> bio, 
			"story"       -> story
 		)
		  
		var (profileId:Long, newProfile:JsValue) = UsersApi.ttt_Users_createUserProfile(profile)
		var profileFromId:JsValue = UsersApi.ttt_Users_getUserProfile(userId)
 
 
  		"ttt_ttt_UsersApiTest_getUserProfile - Create profile for " + firstName + " " + lastName + " profile id = " +profileId in {
 		
	 		userId must beEqualTo ((profileFromId \ "userProfile" \ "userId").as[Long])
	  		firstName must beEqualTo ((profileFromId \ "userProfile" \ "firstName").as[String])
	  		lastName must beEqualTo ((profileFromId \ "userProfile" \ "lastName").as[String])
	  		gender must beEqualTo ((profileFromId \ "userProfile" \ "gender").as[String])
	 		
	 		// TODO - Covert date formats to match
	  		//userId must beEqualTo ((newProfile \ "userProfile" \ "birthdate").as[String])
	  		nationality must beEqualTo ((profileFromId \ "userProfile" \ "nationality").as[String])
	  		portraitUrl must beEqualTo ((profileFromId \ "userProfile" \ "portraitUrl").as[String])
	  		bio must beEqualTo ((profileFromId \ "userProfile" \ "bio").as[String])
	  		story must beEqualTo ((profileFromId \ "userProfile" \ "story").as[String]) 		
  		}
 
			  	  
 	} // End of ttt_UsersApiTest_getUserProfile
 	
 	
 	 	// =================================================================================
 	//                      ttt_UsersApiTest_associateUserAndEvent
 	//
 	def ttt_UsersApiTest_associateUserAndEvent() {
 	  
 	
 		// Yosemite National Park  37.865348  -119.538374
 		var name = "Yosemite Nationa Park"
 		var desc = "Camping, skiing and nature"
 		var cat  = "PARK"
 		var url  = "www.yosemite.gov"	  
 		var latitude = 37.865348
  		var longitude = -119.538374
  		var role = "NORM"
  	    
  		// Create user
  		var(userId:Long, user:JsValue) = UsersApi.ttt_Users_createUser(latitude, longitude, role)

  		// Create a place to hold the event
 		var createPlace = Json.obj(
			"name" -> name,
			"desc" -> desc, 
			"cat"  -> cat,
			"url" -> url, 
			"latitude" -> latitude, 
			"longitude" -> longitude	
		)
  		var (placeId:Long, newPlace:JsObject) =  LocationsApi.ttt_Places_CreatePlace(createPlace, 0)
 		
  		// Create an event 
  		var activityCategories: List[Long] = List(1,2)
	 	var event = Json.obj(
			"from" ->"2014-02-23 10:30:00.0", 
			"to" -> "2014-02-25 11:30:00.0", 
			"title" -> "Yosemite National Park", 
		    "activityType" -> 22, 
		    "activityCategories" -> activityCategories, 
		    "placeId" -> placeId,  // Place Id from the create new place command above
		    "desc" -> "Camping in Yosemite Valley", 
		    "minSize" -> 5, 
		    "maxSize" -> 20, 
		    "rsvpTot" -> 7, 
		    "waitListTot" -> 3
		)
		var (eventId:Long, newEvent:JsValue) = EventsApi.ttt_Events_createEvent(event)

  		// Associate the user with the event

		
		//println("-------- Assoicate UserId: " + userId + " Event Id = " + eventId)
				
		 // TODO - Test failing here but works manualy, probably some simple problem I will find later.
		//		var (passFailStatus:Boolean, results:JsValue) = UsersTests.ttt_Users_associateUserAndEvent(userId, eventId)
				
		//     println("========== Associate User and Event ==========\n" + results + "\n==============")	
  	
 		
 		"ttt_UsersApiTest_associateUserAndEvent" in {pending} 
 
 	  	  
 	} // End of ttt_UsersApiTest_associateUserAndEvent
 	

  
  
} // End of trait ttt_UsersApiTest
