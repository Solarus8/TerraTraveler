
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

// =================
//
//  NOTE: Users tests will be moved to this file
//
// ==================

object UsersTests extends ApplicationSpec {
  
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
	
	def test2() {
		"Test2" in {
		  
			2 must beEqualTo(2)
		}	
		
	}

/*	
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
	
		
		
		println("((((((((((((" + temp)
	    temp
 	} 
 	*  
 	*/	
}



