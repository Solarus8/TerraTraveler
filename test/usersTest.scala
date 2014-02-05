
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

class usersTests {
  
  	def test1 {
		println("Users - Made it to test1")
	  
	}
	
	def test2(test: String): String = {
		println("Users - Test2 should return: " + test)
			return test
	}
  

}



