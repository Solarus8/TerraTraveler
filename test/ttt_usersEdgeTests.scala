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
			
			"Edge Tests - Create User Profile" in {pending}
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
		    
		  
		  
			var (passFailStatus:Boolean, results:JsValue) = TestCommon.ttt_sendApiCommand(Json.obj(),
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
 	

  
} // End of object UsersEdgeTests
