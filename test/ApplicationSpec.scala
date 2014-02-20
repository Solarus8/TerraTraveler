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
			
			"Create User Profile" in {pending}
			"Get User Profile" in {pending}
			
			// TODO - Create user profile
			// TODO
						
			// At least one test must be directly inside of "should {}" for test results to 
			// work properly. Tests in a function work but tests in another file or class don't print
			// the results correctly.				
			"End of users API test" in {"End" must contain("End")			

				
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
		  
			ttt_eventsTest
				
			"Get Events by User ID" in {pending}		
			"Get Events by location radius using location ID" in {pending}
			"Get Events by latitude, longitude, radius, activity, and category" in {pending}
			
			"End of Events API test" in {			
				"End" must beEqualTo("End")
			}
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
		
			
		} // End of Edge Tests
		
	} // End of Terra Traveler Test should

  } // End of "Application" should 
  

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

		

		println("========= Place json sent to function" + createPlace + " ==========")
		
		
		var (placeId:Long, newPlace:JsObject) =  PlacesTest.ttt_Places_CreateNewPlace(createPlace, 0)
 
		
		
		
		println (" ========== create user place = " + placeId + " \n" + newPlace + "\n======")
		

		
  		"ttt_placesApiTest_createPlace called " + name in {
 		  
 			name      must beEqualTo((newPlace \ "place" \ "name").as[String])
 			desc      must beEqualTo((newPlace \ "place" \ "desc").as[String])
			cat       must beEqualTo((newPlace \ "place" \ "cat").as[String])
 			url       must beEqualTo((newPlace \ "place" \ "url").as[String])
//			latitude  must beEqualTo((newPlace \ "place" \ "lat").as[Double])
//	  		longitude must beEqualTo((newPlace \ "place" \ "lon").as[Double])
// TODO - Fix number comparisons
  
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
 			//latitude  must beEqualTo((newPlace \ "place" \ "lat").as[Double])
	  		//longitude must beEqualTo((newPlace \ "place" \ "lon").as[Double])  
 // TODO - Fix number comparisons
 		}
 	  
 	  
 	}
 	

 	
  	// =================================================================================
  	//                     
  	//  	
  	def ttt_eventsTest() {
 
  	  
 // NOTE - This test not finished
 // NOTE - This test not finished
 // NOTE - This test not finished
	  
  	  	var from = "2014-02-23 10:30:00.0"
		var to   = "" 
		var title = "Outside Lands"
		var activityType = 22
		var activityCategories = "[1,2]"
		var placeId = TestCommon.getPlaceId
		var desc = "Outside Lands: best music festival in S.F."
		var minSize = 2
		var maxSize= 50
		var rsvpTot= ""
		var waitListTot = ""

	 	var event = Json.obj(
			"from" -> from, 
			"to" -> to, 
			"title" -> title, 
		    "activityType" -> activityType, 
		    "activityCategories" -> activityCategories, 
		    "placeId" -> placeId, 
		    "desc" -> desc, 
		    "minSize" -> minSize, 
		    "maxSize" -> maxSize, 
		    "rsvpTot" -> 5, 
		    "waitListTot" -> 0
		)
		
		var newEvent = EventsTest.ttt_Events_createEvent(event)
		
		var id = TestCommon.ttt_getValue(newEvent, "id")

		var eventFromId = EventsTest.ttt_Events_getEventById(id)
		
		
// TODO - convert Linux time to "2014-02-23 10:30:00.0"
 //		eventFromId must contain(""""from":"""" + from + """"""")
//  	eventFromId must contain(""""to":"""" + to + """"""")
  	  	eventFromId must contain(""""title":"""" + title + """"""")
  	  	eventFromId must contain(""""activityType":""" + activityType)
//  	  	eventFromId must contain(""""activityCategories":"""" + activityCategories + """"""")
  	  	eventFromId must contain(""""description":"""" + desc + """"""")
  	  	eventFromId must contain(""""minSize":""" + minSize)
  	  	eventFromId must contain(""""maxSize":""" + maxSize)
  	  	eventFromId must contain(""""rsvpTotal":""" + rsvpTot)
  	  	eventFromId must contain(""""waitListTotal":""" + waitListTot)
		
		//println("--------- New event response -----\n" + newEvent + "\n-----------Get event from ID -----\n" + eventFromId)
 	}	
  	
  	
  
  	// =================================================================================
  	//                     ttt_UsersApiTest_createUser
  	//  	
  	def ttt_UsersApiTest_createUser() {
  		  		
  		//Hacker Dojo 37.402840   -122.050000
  		var latitude = 37.402840
  		var longitude = -122.050000
  		var role = "NORM"
  	     	    
  		var(id:Long, user:JsValue) = UsersTests.ttt_Users_createUser(latitude, longitude, role)

 //println("======== Create User Return Values ====\n" + id + "\n========= User =====\n" + user) 		
  		  		
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
	  		
	  		
	  		// The Json matchers on this document work on Strings, not Json
	  		// http://etorreborre.github.io/specs2/guide/org.specs2.guide.Matchers.html#Matchers  		
			//  		var userString = user.toString  		
			//  		userString must */("lat" -> latitude)
			//  		userString must */("lat" -> longitude)

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
  	  		password must contain(userName)
  		  
  		  	id         must beEqualTo((userFromId \ "user" \ "id").as[Long]) 
  		  	primaryLoc must beEqualTo((userFromId \ "user" \ "primaryLoc").as[Long]) 
	  		role       must beEqualTo((userFromId \ "user" \ "role").as[String]) 
	  		userName   must beEqualTo((userFromId \ "user" \ "userName").as[String]) 
	  		
	  			  		
  		}
		 	  
  	} // End of ttt_UsersApiTest_getUserById()
  	

	     
}  // end of class ApplicationSpec

