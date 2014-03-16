import play.api.libs.json._
import play.api.test.Helpers._
import play.api.test.Helpers.await
import play.api.libs.ws._
import play.api.test.Helpers

trait UsersEdgeTests extends org.specs2.mutable.Specification {
 
	// =================================================================================
	//    Users Edge tests
	//        Get all Users - No test
	//        Get User by ID
	//        Create User
	//        Create UserProfile
	//        Get UserProfile
    //        Get User Contacts by User ID
    //        Associate User to Contact
	// =================================================================================
  
  
	// =================================================================================
    //                ttt_runAll_usersEdgeTests
    //
  	def ttt_runAll_usersEdgeTests {
  	  
  		"Users Edge Tests" in {
		  
			"Edge Tests - Create New User /api/v1/users" should {
				ttt_UsersEdgeTests_CreateUser
				"End of Edge Tests for Create User" in {"End" must beEqualTo("End")}
			}
					
			"Edge Tests - Get User By Id /api/v1/users/1:" should {		  
				ttt_UsersEdgeTests_getUserById		  				
				"End of Edge Tests for Get User By Id" in {"End" must beEqualTo("End")}								
			}
			
			"Edge Tests - Create User Profile /api/v1/users" should {
				ttt_UsersEdgeTests_CreateUserProfile
				"End of Edge Tests for Create User Profile" in {"End" must beEqualTo("End")}	
			 
			}
			"Edge Tests - Get User Profile" in {pending}				
			"Edge Tests - Get User Contacts by User ID" in {pending}
			"Edge Tests - Associate User to Contact" in {pending}
		
			"End of Users Edge Tests" in {"End" must beEqualTo("End")}
			
  		} // End of "Users Edge Tests" in
   
	} // End of ttt_runAll_usersEdgeTests
  
  
  	// =================================================================================
 	//                      ttt_UsersEdgeTests_getUserById
	//
 	def ttt_UsersEdgeTests_getUserById {
 	  
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
 	  
 	  	  
		ttt_getUserById_WithMatchers("dsfddf", "Get User By Id - Send text instead of a number", """{}""")
		
		// Send negative number
		ttt_getUserById_WithMatchers("-1", "Get User By Id - Send a negative number", """{"status":"None"}""")
		
		// Very large and very small integer sizes for ID
		ttt_getUserById_WithMatchers("0", "Get User By Id - Check user id zerro", """{"status":"None"}""")
		ttt_getUserById_WithMatchers("9223372036854775807", "Get User By Id - Send largest 'long' size available", """{"status":"None"}""")
		ttt_getUserById_WithMatchers("9223372036854775807", "Get User By Id - Send greater than largest 'long' size ", """{"status":"None"}""")
		ttt_getUserById_WithMatchers("-9223372036854775808", "Get User By Id - Send the smallest 'long' available", """{"status":"None"}""")
		ttt_getUserById_WithMatchers("-9223372036854775809", "Get User By Id - Send less than smallest 'long' available", """{"status":"None"}""")	  
 
 			
	  	def ttt_getUserById_WithMatchers(id: String, title: String,expectedErrorMessage: String): String = {	    
		    // expectedFailure - Blank if you expect test to pass
		    //                 - or error message you expect to receive
		    
		  
		  
	
			var (passFailStatus:Boolean, results:JsValue, error:String) = TestCommon.ttt_sendApiCommand(Json.obj(),
			    "users/" + id, title)
			    
			var temp = results.toString
						    
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
	  	} // End of ttt_getUserById_WithMatchers	
		
		
		"End of function ttt_EdgeTests_getUserById" in {1 must beEqualTo(1)}
		
 	 	
 	} // End of ttt_UsersEdgeTests_getUserById
  	
  
  	// =================================================================================
	//                      ttt_UsersEdgeTests_CreateUser
	//

	def ttt_UsersEdgeTests_CreateUser {
		
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
    		 temp must contain(userName + """!@#$%^&*()|,.""")
 	       	 temp must contain("primaryLoc")
		}
	
		
		"Create User - Use latitude and longitude greater than 360" in {
	       	var temp = ttt_sendUserApiCommand (apiPath, userName + "LonLat", email, password, role, 370.123, 370.456)       	    		
			temp must contain(userName + "LonLat")
 	       	temp must contain("primaryLoc")
	       	// TODO - Verify latitude and longitude are around 10 degrees
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

 	}  // End of ttt_UsersEdgeTests_CreateUser
  
  
  

	// ==============================================================================
	//                     ttt_UsersEdgeTests_CreateUserProfile
	//
	def ttt_UsersEdgeTests_CreateUserProfile () {
	  


/*	  
curl \
	--header "Content-type: application/json" \
	--request POST \
	--data '{"userId" : 1, "firstName" : "Raymond", "lastName" : "Zamora", "gender" : "M", "birthdate" : "1990-02-23 10:30:00.0", "nationality" : "Brazilian", "portraitUrl" : "http://www.me.jpg", "bio" : "I began life as a small child.", "story" : "I am a very tough guy."}' \
	ec2-54-193-80-119.us-west-1.compute.amazonaws.com:9000/api/v1/users/profile \
	| python -mjson.tool
*/	  
	
		var prefix = "Create Profile - "
	  
		// Greenwater Lake Provincial Park, Canada 
  		var latitude = 52.528699
  		var longitude = -103.476105
  		var role = "NORM"
 	    
  		// Create user and get the user Id for creating  a profile
  		var(userId, userResults:JsValue) = UsersApi.ttt_Users_createUser(latitude, longitude, role)

  		if (userId < 1 ) {  
  			"Create User Profile - TEST ABORTED, invalid User Id prevents creating profile, Id=" + userId in {		  
  				failure
  			}
  			
  		} else {
  		
			var firstName   = "Mike"
			var lastName    = "Thomas"
			var gender      = "M"
			var birthdate   = "1990-02-23 10:30:00.0"   // 635769000 635797800000
			var nationality = "Brazilian"
			var portraitUrl = "http://www.me.jpg"
			var bio         = "I was born"
			var story       = "It was a dark and stormy night"
			  
			var profile = Json.obj(
	    		 "userId"      ->  userId,		
				 "firstName"   ->  firstName,
				 "lastName"    ->  lastName, 
				 "gender"      ->  gender,
				 "birthdate"   ->  birthdate,
				 "nationality" ->  nationality,
				 "portraitUrl" ->  portraitUrl,
				 "role"        ->  role,
				 "bio"         ->  bio,
				 "story"       ->  story 	    
			)

			
			prefix + "Verify a new profile is created" in {



			
				var (profileId:Long, profileResults:JsValue) = 
					UsersApi.ttt_Users_createUserProfile(profile)
println("Create User Profile = " + profileResults)					
				firstName    must beEqualTo((profileResults \ "userProfile" \ "firstName").as[String]) 
				lastName     must beEqualTo((profileResults \ "userProfile" \ "lastName").as[String]) 
// TODO - Github issue 2 filed for gender
				//				gender       must beEqualTo((profileResults \ "userProfile" \ "gender").as[String]) 
// TODO - Github issue 4 filed for missing hour. minute and second
//				TestCommon.ttt_convertDateTimeToMilleconds(birthdate, "")    must beEqualTo((profileResults \ "userProfile" \ "birthdate").as[Long])
				nationality  must beEqualTo((profileResults \ "userProfile" \ "nationality").as[String])
				portraitUrl  must beEqualTo((profileResults \ "userProfile" \ "portraitUrl").as[String])
				bio          must beEqualTo((profileResults \ "userProfile" \ "bio").as[String])
				story        must beEqualTo((profileResults \ "userProfile" \ "story").as[String])	
				story        must not(beEqualTo("dddd"))					
			}
			
			prefix + "Github issue 2 - gender has extra spaces on string" in {failure}
			prefix + "Github issue 4 - should birthday contain Mon, day, year only, not time?" in {failure}
			prefix + "Github issue 6 -can't revise profile after profile created" in {failure}

			
			prefix + "Profile with name longer than database field" in {
				var longName = "x" + 1000
	
				val updatedJson = profile.as[JsObject] ++ Json.obj("firstName" -> longName)	
	
				
	println("Profile long name" + updatedJson)
	//			firstName  must beEqualTo((profileResults \ "userProfile" \ "firstName").as[String])
				
				1 must beEqualTo(1)
			}	
			
			"Test with missing or extra keys in Json" should {
			  
			
			    var (userId1, userResults1:JsValue) = UsersApi.ttt_Users_createUser(latitude, longitude, role)			  
				var (profile_id1, results1) = ttt_Edge_createUserProfileJson(TestCommon.EMPTY_FIELD_LONG, 
				    firstName, lastName, gender, birthdate, nationality, portraitUrl, role, bio,story, 
				    TestCommon.EMPTY_FIELD_STRING, TestCommon.EMPTY_FIELD_LONG,TestCommon.EMPTY_FIELD_DOUBLE)
				"Detect missing userId" in {userResults1.toString must beEqualTo("{}")}
				
				    
				    
println("\n\n************** Missing Json fields ****************")
println(results1)
println("*" * 30)
				    
				    
				"End of Json missing field tests" in {1 must beEqualTo(1)}
			  
			}
			

			
 //  	 	val updatedJson = temp.as[JsObject] ++ Json.obj("name" -> "Ron") ++ Json.obj("new" -> "item") 
 //   	println("Json with new item" + update
			
			
  		} // End of if statement to check for profile id is 1 or greater  
	} // End of ttt_UsersEdgeTests_CreateUserProfile
		
 	
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
 	  
 
		var temp = Helpers.await(WS.url(TestCommon.serverLocation + "/api/v1/" + apiPath).post(user)).body 
 	  
		temp
 	}
 	
 	def ttt_Edge_createUserProfileJson(userId:Long, firstName:String, lastName:String, gender:String, 
 	    birthdate:String, nationality:String, portraitUrl:String, role:String, bio:String,story:String,
 	    extraString:String, extraLong:Long, extraDouble:Double): (Long, JsValue) = {
 	   	  
			var temp = Json.obj()
			
			var profile = (temp.as[JsObject] 
				++ useJsonFieldLong("userId", userId)
 			    ++ useJsonFieldString("firstName", firstName) 
 			    ++ useJsonFieldString("lastNamee", lastName)
 			    ++ useJsonFieldString("gender", gender)
 			    ++ useJsonFieldString("birthdate", birthdate)
 			    ++ useJsonFieldString("nationality", nationality)
 			    ++ useJsonFieldString("portraitUrl", portraitUrl)
 			    ++ useJsonFieldString("role", role)
 			    ++ useJsonFieldString("bio", bio)
 			    ++ useJsonFieldString("story", story)

   	 	    )
 
   	 	println("\n\n\nCreateUserProfileJson" + profile + "\n\n")
  	  
   	 	var (profileId:Long, results:JsValue) =  UsersApi.ttt_Users_createUserProfile(profile)
   	 	
   	 	return (profileId, results)
 	}
 	
 	
 	def useJsonFieldString(key:String, value:String): JsObject = {
 
 		var temp:JsObject = null
 
 		if (TestCommon.EMPTY_FIELD_STRING == value) { 
 			temp = null
 		} else {
 			temp = Json.obj(key -> value)
 		}
 		  
    	println("String, Key= " + key + ", Value = " + value + ", " + temp)
    	
    	return temp
    }
 	
 	def useJsonFieldLong(key:String, value:Long): JsObject = {
      
		var temp:JsObject = null
 		  
 		if (TestCommon.EMPTY_FIELD_LONG == value) { 
 			temp = Json.obj()
 		} else {
 			temp = Json.obj(key -> value)
 		}
   
    	println("Long, Key= " + key + ", Value = " + value + ", " + temp)
    	
    	return temp
    }
 	

  
} // End of object UsersEdgeTests
