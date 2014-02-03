
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
//  NOTE: tests run twice when functions are put in this file.
//
// ==================



//object usersTest extends ApplicationSpec {   // Tests run twice
object usersTest {  // Matcher errors
  
    val serverLocation = "http://localhost:9998"
      
    
/*  

	def testGetUserById(id: Long): String = {
	    println("Start - getUserById" + id)
  	  
  		var temp = Helpers.await(WS.url(serverLocation + "/api/v1/users/" + id.toString.trim()).get()).body
  		temp must contain(""""id":""" + id.toString.trim() )  
	    
	    println("ID: " + temp)
	    
	    temp    
  	}
*/  

}



