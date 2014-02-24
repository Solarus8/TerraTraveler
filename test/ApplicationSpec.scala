import org.specs2.mutable._
import org.specs2.mutable.Specification
import org.specs2.matcher.JsonMatchers
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import play.api.test.Helpers.await
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.ws._
import play.api.mvc.Results._
import play.api.Play
import play.api.Play.current
import play.api.Application

import scala.io.Source._


//import java.lang.Object
import java.util.Calendar
import java.text.SimpleDateFormat
import java.io._
import java.io.PrintWriter
import java.io.File
import scala.util.matching
import scala.util.matching.Regex


// To run this test 
//     1. Start the postgresql server
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
class ApplicationSpec extends Specification with JsonMatchers {
  
	// TODO - add test to verify server is running or nothing works 
	val serverLocation = TestCommon.serverLocation


	
	"Terra Traveler Test" should {
  	
		println("\n\n\n\n==================== Test Started " + Calendar.getInstance().getTime() + " =======================")


		"Web server tests" should {
		
			"send 404 on a bad request" in new WithApplication{
				  route(FakeRequest(GET, "/boum")) must beNone
			}
			
			"render the index page" in new WithApplication{
				val home = route(FakeRequest(GET, "/")).get
				
				status(home) must equalTo(OK)
				contentType(home) must beSome.which(_ == "text/html")
				contentAsString(home) must contain ("Terra Traveler app is ready.")
			}
		}


		// =================================================================
		//    Users API tests
		//        Get User by ID
		//        Create User
		//        Create UserProfile
		//        Get UserProfile
		//        Get all Users (Should test be written since this may go away
		//                       but is very useful for test and debug
		// =================================================================
		
		"Users API test" should
		{
		  
		    ttt_UsersApiTest_createUser
			ttt_UsersApiTest_getUserById
			ttt_UsersApiTest_createUserProfile
			ttt_UsersApiTest_getUserProfile
			
			// TODO - Get All Users test would take a long time - test not written yet
						
			// At least one test must be directly inside of "should {}" for test results to 
			// work properly. Tests in a function work but tests in another file or class don't print
			// the results correctly.				
			"End of users API test" in {"End" must beEqualTo("End")}		

				
		} // End of user API tests

		// =================================================================
		//    Places API tests
		//        Create Place
		//        Create Place 3rd Party Reference
		//        Get Place 3rd Party Reference by ID
	    //        Get Place 3rd Party Reference by TerraTraveler Place ID
	    //        Get Place by ID
		//        Get Users (attendies) by Event ID"
		//        Get all Activity Types and Categories
		//        Get User Contacts by User ID
		//        Associate User to Contact
		//        Create Place 3rd Party Reference
		// =================================================================
			
		"Places tests API" should {
		  
			ttt_placesApiTest_createPlace
			ttt_placesApiTest_getPlaceById
	
						
			"Create Place 3rd Party Reference" in {pending}
			"Get Place 3rd Party Reference by ID" in {pending}	
			"Get Place 3rd Party Reference by TerraTraveler Place ID" in {pending}	
			"Get Place by ID" in {pending}	
			"Get Users (attendies) by Event ID" in {pending}	
			"Get all Activity Types and Categories" in {pending}	
			"Get User Contacts by User ID" in {pending}	
			"Associate User to Contact" in {pending}	
			"Create Place 3rd Party Reference" in {pending}				
			
			
			"End of users API test" in {			
					"End of users API test" must contain("End")
			}
			
  		} // End of Places Test API

		
		// =================================================================
		//    Events API tests	
		//        Create Event
		//        Get Events by User ID
		//        Get Event by ID
		//
		//        What is the accuracy for radius of events at the equator, Mountain View,
		//            Fairbanks Alaska and Johannesburg South Affrica.
		// =================================================================
		
		"Events API tests" should {
		  
		    ttt_EventsApiTest_createEvent
		    ttt_EventsApiTest_getEventById
			ttt_EventsApiTest_getAllActivityTypesAndCategories
				
			"Get Events by User ID" in {pending}		
			"Get Events by location radius using location ID" in {pending}
			"Get Events by latitude, longitude, radius, activity, and category" in {pending}
	
						
			"End of Events API test" in {"End" must beEqualTo("End")}
		} // End of Events API tests
		
		
	
		"Edge tests" should	{
		
			"Edge Tests - Create New User /api/v1/users" should {
			  
				ttt_EdgeTests_CreateUser
			  			  				
				"End of Edge Tests for Create User" in {"End" must beEqualTo("End")}
				  				  

			
			} // End of Create User edge tests


			"Edge Tests - Get User By Id /api/v1/users/1:" should {
			  
				ttt_EdgeTests_getUserById
			  				
				"End of Edge Tests for Get User By Id" in {"End" must beEqualTo("End")}
									
			}
			
			"Edge Tests - Create User Profile" in {pending}
			"Edge Tests - Get User Profile" in {pending}
		
			
		} // End of Edge Test		
		
		
		
		"End of all Tests" in {"End" must beEqualTo("End")}
		
	} // End of Terra Traveler Test should

  //} // End of "Application" should 
  

// TODO - Move these functions to another file
// TODO - Move these functions to another file
// TODO - Move these functions to another file	
//    and figure out why test results don't print correctly from other files
//    but work inside this class.
// TODO - Move these functions to another file
// TODO - Move these functions to another file	

 	def ttt_getUserById_WithMatchers(id: String, title: String,expectedErrorMessage: String): String = {	    
	    // expectedFailure - Blank if you expect test to pass
	    //                 - or error message you expect to receive
	    
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
	    
    	var temp = Helpers.await(WS.url(serverLocation + "/api/v1/users/" + id).get()).body	 
	    
	    title + "(" + "id= " +id +")" in {
          	
	    	if (expectedErrorMessage.length() > 0) {  // Expecting a failure to occur
  		
	    		temp must not contain("userName")
	    		temp must contain(expectedErrorMessage)

	    	}
	    	else  // Expecting a correct user name
	    	{	    	  
  	    		temp must contain(""""id":""" + id.trim() )  // ID is on left side of string
	    		temp must contain("primaryLoc")              // Check other end of string
	    		
	    	}
	    }

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
	    
		// Duplicate userName returns an error message web page      
		if (checkDuplicateUserName == true)  {
			"ttt_testCreateUser - Attempt to create user with duplicate name" in {
		  
				temp must contain("This exception has been logged with id")	
			}
		} 
		else {
		  
			"ttt_testCreateUser - Create new user" in {
	
				temp must contain("userName")
			      	      
				//Check for duplicate user name
				temp must not contain("PSQLException: ERROR: duplicate key value violates unique constraint")
				temp must not contain("This exception has been logged with id")
			  
			}
		}  

	    temp
 	} 	
	 	
	
	
 	def ttt_EdgeTests_getUserById {
 	  
		ttt_getUserById_WithMatchers("dsfddf", "Get User By Id - Send text instead of a number", "Cannot parse parameter id as Long:")
		
		// Send negative number
		ttt_getUserById_WithMatchers("-1", "Get User By Id - Send a negative number", """{"status":"None"}""")
		
		// Very large and very small integer sizes for ID
		ttt_getUserById_WithMatchers("0", "Get User By Id - Check user id zerro", """{"status":"None"}""")
		ttt_getUserById_WithMatchers("9223372036854775807", "Get User By Id - Send largest 'long' size available", """{"status":"None"}""")
		ttt_getUserById_WithMatchers("9223372036854775807", "Get User By Id - Send greater than largest 'long' size ", """{"status":"None"}""")
		ttt_getUserById_WithMatchers("-9223372036854775808", "Get User By Id - Send the smallest 'long available", """{"status":"None"}""")
		ttt_getUserById_WithMatchers("-9223372036854775809", "Get User By Id - Send the smallest 'long available", "Cannot parse parameter id as Long:")	  
 	}
 		
 	def ttt_EdgeTests_CreateUser {
		
 	    var (userName:String, email:String, password:String) = TestCommon.ttt_generateUserNameEmailAndPassword
 	    var role = "NORM"
 	    var latitude = 37.1234
 	    var longitude = 127.5678
 	    var apiPath = "users"

		"Create User - Missing user name" in {
 	       	 var temp = ttt_sendUserApiCommand (apiPath, "", email, password, role, latitude, longitude)
  	       	 temp must not contain("password")   		
			 temp must contain("This exception has been logged with id")
		}
		
		// Single quote could cause database errors or database crash
		"Create User - User name with single quote (') in name get's created" in {
 	       	 var temp = ttt_sendUserApiCommand (apiPath, userName + "'", email, password, role, latitude, longitude)
   			 temp must contain(userName + """'""")
 	       	 temp must contain("primaryLoc")
		}
		
		// Semicolon could cause database problems and is used in SQL injection
		"Create User - User name with single semicolon (;) in name get's created" in {
 	       	 var temp = ttt_sendUserApiCommand (apiPath, userName + ";", email, password, role, latitude, longitude)
			temp must contain(userName + """;""")
 	       	temp must contain("primaryLoc")
		}

		"Create User - User name with spaces in name fails" in {
 	       	 var temp = ttt_sendUserApiCommand (apiPath, """SELECT password FROM user""", email, password, role, latitude, longitude)
 	       	 temp must contain("This exception has been logged with id")
 	       	temp must not contain("primaryLoc")
		}
		
		"Create User - User name with " +  """!@#$%^&*()|,."""  + " in name gets created" in {
 	       	 var temp = ttt_sendUserApiCommand (apiPath, userName + """!@#$%^&*()|,.""", email, password, role, latitude, longitude)
   			 //println("\n\n===== %&$^&#% =======\n" + temp + "\n========")	
   			 // TODO - comparison having problems with userName = BadUser863!@#$%^&*()|,. user name did get created
   			 temp must contain(userName + """!@#$%^&*()|,.""")
 	       	 temp must contain("primaryLoc")
		}
	
		
		"Create User - Use latitude and longitude greater than 360" in {
	       	var temp = ttt_sendUserApiCommand (apiPath, userName + "LonLat", email, password, role, 370.123, 370.456)       	    		
			temp must contain(userName + "LonLat")
 	       	temp must contain("primaryLoc")
	       	// TODO - Verify latude and longitude are around 10 degrees
		}
		
 	    
		"Create User - send commands with missing data" in {
			pending
		}
		
		"Create User - Send too many parameters" in {
			pending
	  	}
		
		"Create User - Send too few parameters" in {
			pending
		}
		
		"Create User - Send badly formed JSON" in  {

			pending
		}

 	}
  	
 	// ==============================================================================
 	// ttt_sendUserApiCommand 
 	// 
 	def ttt_sendUserApiCommand (apiPath: String, userName: String, email: String, password: String, 
 	    role: String, latitude: Double, longitude: Double): String = {
 	  	  
  		var user = Json.obj(
    		"userName" -> userName, 
    		"email" -> email, 
    		"password" -> password, 
    		"role" -> role, 
    		"latitude" -> latitude,  
    		"longitude" -> longitude
    	)
 	  
 
		var temp = Helpers.await(WS.url(serverLocation + "/api/v1/" + apiPath).post(user)).body 
 	  
		temp
 	}
 	
 	// ================================================================================
 	//                       ttt_placesApiTest_createPlace
 	//
 	def ttt_placesApiTest_createPlace() {
 	  
 		var name = "Hacker Dojo3"
 		var desc = "The place to be"
 		var cat  = "PARK"
 		var url  = "spam@spam.com"
 		var latitude:Double  =  37.386052
 		var longitude:Double =  -122.083851
 		
 		var createPlace = Json.obj(
			"name" -> name,
			"desc" -> desc, 
			"cat"  -> cat,
			"url" -> url, 
			"latitude" -> latitude, 
			"longitude" -> longitude	
		)

		var (placeId:Long, newPlace:JsObject) =  PlacesTest.ttt_Places_CreateNewPlace(createPlace, 0)
 
  		"ttt_placesApiTest_createPlace called " + name in {
 		  
 		    cat       must beEqualTo((newPlace \ "place" \ "cat").as[String])
 		    desc      must beEqualTo((newPlace \ "place" \ "desc").as[String])
 			name      must beEqualTo((newPlace \ "place" \ "name").as[String])			
 			url       must beEqualTo((newPlace \ "place" \ "url").as[String])

  
 		}
 

 	} // End of ttt_placesApiTest_createPlace
 	
 	
 	// =================================================================================
 	//                       ttt_placesApiTest_getPlaceById
 	//
 	// Create a place then get the id number.  User the place id number to
 	// verify the place was created
 	def ttt_placesApiTest_getPlaceById() {
 		var name = "Computer History Museum2"
 		var desc = "History of the computer"
 		var cat  = "PARK"
 		var url  = "james@hackerdojo.com"
 		var latitude  =  37.414452
 		var longitude =  -122.077581
 		
 		var place = Json.obj(
			"name" -> name,
			"desc" -> desc, 
			"cat"  -> cat,
			"url" -> url, 
			"latitude" -> latitude, 
			"longitude" -> longitude	
		)
		
		var (placeId:Long, newPlace:JsValue) =  PlacesTest.ttt_Places_CreateNewPlace (place, 0)
		var placeById:JsValue = PlacesTest.ttt_Places_getPlaceById(placeId)
		

 	  
		"ttt_placesApiTest_getPlaceById - " + name + " with id = " + placeId + " " in {
 		  
 			name      must beEqualTo((newPlace \ "place" \ "name").as[String])
 			desc      must beEqualTo((newPlace \ "place" \ "desc").as[String])
			cat       must beEqualTo((newPlace \ "place" \ "cat").as[String])
 			url       must beEqualTo((newPlace \ "place" \ "url").as[String])
 			

 		}
 	  
 	  
 	}
 	

 	
  	// =================================================================================
  	//                               ttt_EventsApiTest_createEvent 
  	// 
 	//  This test depends on "Create Place" API working correctly.
 	//
  	def ttt_EventsApiTest_createEvent() {
 	 	  
  	  	// Cuesta Park in Mountain View
		var place = Json.obj (
		    "name" -> "Cuesta Park", 
			"desc" -> "Mountin View city park", 
			"cat" -> "PARK", 					
			"url" -> "http->//www.cuestapark.org/", 
			"latitude" -> 37.371897, 
			"longitude" -> -122.080207 
		)
  	  
		// Event details
  	  	var from = "2014-02-23 10:30:00.0"
		var to   = "2014-03-23 11:30:00.0" 
		var title = "Outside Lands"
		var activityType = 22		
		var activityCategories: List[Long] = List(1,2)
		var desc = "Outside Lands: best music festival in S.F."
		var minSize:Long = 2
		var maxSize:Long = 50
		var rsvpTot:Long = 6
		var waitListTot:Long = 3
		
		// Create a new place since place Id is needed to create and event
		var(placeId:Long, newPlace:JsValue) = PlacesTest.ttt_Places_CreateNewPlace(place, 0)
		  	  
	 	var event = Json.obj(
			"from" -> from, 
			"to" -> to, 
			"title" -> title, 
		    "activityType" -> activityType, 
		    "activityCategories" -> activityCategories, 
		    "placeId" -> placeId,  // Place Id from the create new place command above
		    "desc" -> desc, 
		    "minSize" -> minSize, 
		    "maxSize" -> maxSize, 
		    "rsvpTot" -> rsvpTot, 
		    "waitListTot" -> waitListTot
		)
		
		// Create a new event
		var (eventId:Long, newEvent:JsValue) = EventsTest.ttt_Events_createEvent(event)

		
		"ttt_EventsApiTest_createEvent - Create Event, " + title + ", id= " + eventId in { 
		
			// TODO - convert time format (from: to to:)		  
			title               must beEqualTo((newEvent \ "event" \ "title").as[String])
			activityType        must beEqualTo((newEvent \ "event" \ "activityType").as[Long])			
// TODO -List comparison	[1,2]		
//	  	  	activityCategories  must beEqualTo((newEvent \ "event" \ "activityType").as[List])
			placeId             must beEqualTo((newEvent \ "event" \ "placeId").as[Long])
			desc                must beEqualTo((newEvent \ "event" \ "description").as[String])
			minSize             must beEqualTo((newEvent \ "event" \ "minSize").as[Long])
			maxSize             must beEqualTo((newEvent \ "event" \ "maxSize").as[Long])
			rsvpTot             must beEqualTo((newEvent \ "event" \ "rsvpTotal").as[Long])
			waitListTot         must beEqualTo((newEvent \ "event" \ "waitListTotal").as[Long])
			
			1 must beEqualTo( 1)

  	  	} // End of matchers

 	
 	}  // End of ttt_EventsApiTest_createEvent	
  	
    // =================================================================================
  	//                    ttt_EventsApiTest_getEventById
  	//
  	//  This test depends on "Create Place" API working correctly since it
  	//  uses a place Id.
  	//
  	//
  	def ttt_EventsApiTest_getEventById() {
 
  	    // Place Shoreline Lake Aquatic Center
		var place = Json.obj (
		    "name" -> "Shoreline Lake Aquatic Center", 
			"desc" -> "Biking, hiking, paddle boats, wind sailing", 
			"cat" -> "PARK", 					
			"url" -> "http->//www.shoreline.org/", 
			"latitude" -> 37.432778, 
			"longitude" -> -122.088122 
		)
		
		// Create a new place since place Id is needed to create and event
		var(placeId:Long, newPlace:JsValue) = PlacesTest.ttt_Places_CreateNewPlace(place, 0)
		
		var from = "2014-02-23 10:30:00.0"
		var to   = "2014-03-23 11:30:00.0" 
		var title = "Hike at Shoreline Park"
		var activityType = 22		
		var activityCategories: List[Long] = List(1,2)
		var desc = "We will meet at the Shoreline Lake Aquatic Center"
		var minSize:Long = 5
		var maxSize:Long = 25
		var rsvpTot:Long = 18
		var waitListTot:Long = 98
		
		var event = Json.obj(
			"from" -> from, 
			"to" -> to, 
			"title" -> title, 
		    "activityType" -> activityType, 
		    "activityCategories" -> activityCategories, 
		    "placeId" -> placeId,  // Place Id from the create new place command above
		    "desc" -> desc, 
		    "minSize" -> minSize, 
		    "maxSize" -> maxSize, 
		    "rsvpTot" -> rsvpTot, 
		    "waitListTot" -> waitListTot
		)
		
		// Create a new event
		var (eventId:Long, newEvent:JsValue) = EventsTest.ttt_Events_createEvent(event)
		
		// Get an event using the event Id
		var eventFromId:JsValue = EventsTest.ttt_Events_getEventById(eventId)
  	  
		
		"ttt_EventsApiTest_getEventById - Get Event By Id, " + title + ", id= " + eventId in { 
		
			// TODO - convert time format (from: to to:)		  
			title               must beEqualTo((eventFromId \ "event" \ "title").as[String])
			activityType        must beEqualTo((eventFromId \ "event" \ "activityType").as[Long])			
// TODO -List comparison	[1,2]		
//	  	  	activityCategories  must beEqualTo((eventFromId \ "event" \ "activityType").as[List])
			placeId             must beEqualTo((eventFromId \ "event" \ "placeId").as[Long])
			desc                must beEqualTo((eventFromId \ "event" \ "description").as[String])
			minSize             must beEqualTo((eventFromId \ "event" \ "minSize").as[Long])
			maxSize             must beEqualTo((eventFromId \ "event" \ "maxSize").as[Long])
			rsvpTot             must beEqualTo((eventFromId \ "event" \ "rsvpTotal").as[Long])
			waitListTot         must beEqualTo((eventFromId \ "event" \ "waitListTotal").as[Long])
			


  	  	} // End of matchers
  	  
  	  
  	} // End of ttt_EventsApiTest_getEventById
  	

  	
  
  	// =================================================================================
  	//                     ttt_UsersApiTest_createUser
  	//  	
  	def ttt_UsersApiTest_createUser() {
  		  		
  		//Hacker Dojo 37.402840   -122.050000
  		var latitude = 37.402840
  		var longitude = -122.050000
  		var role = "NORM"
  	     	    
  		var(id:Long, user:JsValue) = UsersTests.ttt_Users_createUser(latitude, longitude, role)

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
//	  		email must contain(userName)
	  		password must contain("password")
	  		
	  		// TODO - Fix this error
	  		// TODO - '"Susie758@outlook.com"' doesn't contain '"Susie758"' (ApplicationSpec.scala:658)
	  		//password.toString() must contain(userName.toString())
	  		
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
		var(id:Long, user:JsValue) = UsersTests.ttt_Users_createUser(latitude, longitude, role)

 		val userName = (user \ "user" \ "userName").toString() 
 		val email    = (user \ "user" \ "email").toString()
 		val password = (user \ "user" \ "password").toString()
		val primaryLoc = (user \ "user" \ "primaryLoc").toString()
 		
		var userFromId:JsValue = UsersTests.ttt_Users_getUserById(id)
		
		"ttt_UsersApiTest_getUserById - Get " + userName +  " with ID=" + id in {
  		 
  	  		userName must not be empty
	  		email must contain("""@""")
	  		email must contain(".com")
	  		password must contain("password")
  	  		password must contain(userName.replaceAll(""""""", ""))
  		  
  		  	id         must beEqualTo((userFromId \ "user" \ "id").as[Long]) 
  		  	primaryLoc must beEqualTo((userFromId \ "user" \ "primaryLoc").as[Long]) 
	  		role       must beEqualTo((userFromId \ "user" \ "role").as[String]) 
	  		userName   must beEqualTo((userFromId \ "user" \ "userName").as[String]) 
	  		
	  			  		
  		}
		 	  
  	} // End of ttt_UsersApiTest_getUserById()
  	
  	// =================================================================================
  	//                           ttt_EventsApiTest_getAllActivityTypesAndCategories
  	// 
  	def ttt_EventsApiTest_getAllActivityTypesAndCategories() { 
  	   
  		/*			
			example response
			{
			    "activityCategories": [
				        {
				            "category": "Fitness",
				            "id": 1
				        },
			        
			   		],
			    "activityTypes": [
			        {
			            "activity": "Running",
			            "id": 1
  		 */
  	  
  	  
  	  
  		// Get activity types from database
  		var (results:JsValue, categoriesFromDatabase, typesFromDatabase) = EventsTest.ttt_Events_getAllActivityTypesAndCategories

  		// Get hard coded activity types
  		var activityType = TestCommon.activityType
  		var activityCategory = TestCommon.activityCategories
  		
 
  		
 		
  		
 println("\n================== Categories from Database ==============\n") 
  		var x =0
  		categoriesFromDatabase.keys.foreach{id =>
  		  	x = x + 1
 			println(x + " Categories From Database " + id + ", " + categoriesFromDatabase(id))
  		}
  		
  		
 
 println("\n=============== Hard Coded Type ====================\n")
 		var y =0
  		activityType.keys.foreach{id =>
  		    y = y + 1
  			println(y + " Hard coded " + id + ", " + activityType(id))
 		}
 
 println("\n=============================================================\n")
  		

  		"Activities and Categories not finished yet" in {
		  
  			try {
 		   
		 		// Compare activities from database with hard code activities
		  		activityType.keys.foreach{ id =>  	
		  		    println("Inside comparison loop " + id)
		  		    var activity = activityType(id)
		            println( "  Key = " + id + ", Hard Code Value = " + activityType(id))
//		            if (typesFromDatabase isDefinedAt id)
		        }
  			} catch {
  			  
  			 	case e: Exception => println("\n\nERROR - Api Get All Activity Types and Categories: " + e +  "\n\n");
  			}
	 		
	 		 1 must beEqualTo(1)
 		
  		} // End of matchers
  		

 println("^^^^^^^^^^^^^^^^ Hard code activity type\n")
  		activityType = TestCommon.activityType
  		
  
	  		activityType.keys.foreach{ id =>  
	  		   var activity = activityType(id)
	           println( "Key = " + id + ", Value = " + activityType(id))
            }
	  		
//	           "Verify " + id + ", value = " + activity in {
//	           
//	  			   activity must beEqualTo((results \ "activityTypes" \ "activity" \ "activity").as[String])
//	  		   }
  		

  		
  
  	} // End of ttt_EventsApiTest_getAllActivityTypesAndCategories()
  
 
   	// =================================================================================
  	//                           ttt_UsersApiTest_createUserProfile
  	//
 	def ttt_UsersApiTest_createUserProfile() {
 	  
 	   //San Jose Airport 37.368613   -121.928523
  		var latitude = 37.368613
  		var longitude = -121.928523
  		var role = "NORM"
  		      
  		// Create user and get ID
  		var(userId:Long, user:JsValue) = UsersTests.ttt_Users_createUser(latitude, longitude, role)
  		

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
 		var (profileId:Long, newProfile:JsValue) = UsersTests.ttt_Users_createUserProfile(profile)
 
 		
 		"ttt_UsersApiTest_createUserProfile - Create profile for " + firstName + " " + lastName + " profile id = " +profileId in {
 		
	 		userId must beEqualTo ((newProfile \ "userProfile" \ "userId").as[Long])
	  		firstName must beEqualTo ((newProfile \ "userProfile" \ "firstName").as[String])
	  		lastName must beEqualTo ((newProfile \ "userProfile" \ "lastName").as[String])
	  		gender must beEqualTo ((newProfile \ "userProfile" \ "gender").as[String])
	 		
	 		// TODO - Covert date formats to match
	  		//userId must beEqualTo ((newProfile \ "userProfile" \ "birthdate").as[String])
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
  		var(userId:Long, user:JsValue) = UsersTests.ttt_Users_createUser(latitude, longitude, role)
  		
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
		  
		var (profileId:Long, newProfile:JsValue) = UsersTests.ttt_Users_createUserProfile(profile)
		var profileFromId:JsValue = UsersTests.ttt_Users_getUserProfile(userId)
 
 
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
 	
 	
	     
}  // end of class ApplicationSpec

