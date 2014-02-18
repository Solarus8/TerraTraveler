
import scala.io.Source
import scalax.io._

import java.io._
import java.io.PrintWriter
import java.io.File

import scala.math

import scala.util.Random



object TestCommon {

	val server = "http://localhost:9998"
	
	// Each user name consists of text and a number.  This counter increments
	// the number to avoid getting duplicate user errors
	var userCounter:Long = 1
		  
	// Save a user Id to be used in multiple locations in the test
	private var userId = ""	
	  
	// When place API is tested save Place ID to be used with other API tests
	private var placeId:Long = 1
	   
	// Location of the server where this test will be run
	def getServerLocation: String = {	  
		return server
	}
	
	
	def getCurrentDirectory = new java.io.File( "." ).getCanonicalPath
	

	
	def getUserId: String = {
		return userId
	}
	
	def setUserId(id: String) {
	  
		userId = id
	}
	
	def getPlaceId: Long = {
		
		return placeId
	}
	
	def setPlaceId(id: Long) {
	  
		placeId = id		
	}
	

	
	
	// =================================================================================
	//                                getUserCounter
	// Each user name has a unique number in the name to prevent 
	// duplicate user name errors.  This function increments the counter and 
	// saves the count in a file.
	//
	// If you decide to erase the database then you can delete the file
	// ttt_testData.txt at the same time to start over with the count at one.	
	def getUserCounter: Long = {
	  
		val fileName = "test/ttt_testData.txt"
		  		
		 // Get value of counter from file or create file
		try {
			var count = Source.fromFile(fileName).getLines.next().toLong
			userCounter = count
		}
		catch {
         case ex: FileNotFoundException =>{
            println("Missing " + fileName + " exception")
            println("Creating file with User Name Counter starting at 1")
            userCounter = 1
         }
         case ex: IOException => {
            println("IO Exception")
         }
		}

	    //println ("========== Counter = " + userCounter)
	    
	    userCounter = userCounter + 1
	    
		val writer = new PrintWriter(new File(fileName))
		writer.write(userCounter.toString)
		writer.close()
				
		return userCounter
	}

	// =================================================================================
	//	                     distanceAndBearing
	//
	//  Pass a longitude and latitude to this function and it will calculate a
	//  new longitude and latitude based on the distance and compass bearing.
	//
	def ttt_distanceAndBearing(latitude:Double, longitude:Double, d:Double, brng:Double) {
	  	  
		var R = 6371.0  // Radius of the earth 6371.0
		
		var lat1 = latitude  * (Math.PI / 180);
		var lon1 = longitude * (Math.PI / 180);
		
		var lat2 = Math.asin( Math.sin(lat1)*Math.cos(d/R) + 
          Math.cos(lat1)*Math.sin(d/R)*Math.cos(brng) );
		var lon2 = lon1 + Math.atan2(Math.sin(brng)*Math.sin(d/R)*Math.cos(lat1), 
                 Math.cos(d/R)-Math.sin(lat1)*Math.sin(lat2));

		
		
		
		println("\n************* New longitude and latitude *****************")
		println("Latitude  = " + lat2 * (180 / Math.PI))
		println("Longitude = " + lon2 * (180 / Math.PI))	
	
	}
	
	
	// =================================================================================
	//                   ttt_generateUserName()
	//	
	// Generate a random user name, email and password.  
	def ttt_generateUserNameEmailAndPassword(): (String, String, String) = {

	  
		var names = Array("John", "Tom", "Jon", "Tim", "Jun", "James", "Rick", "Casey", "Mark", "Jack", "Sam", 
		    "Sally", "Pam", "Jan", "Sarah", "Susie", "Mary", "Marie", "Sue", "Jones", "Johnson", 
		    "Smith", "Voelm", "Anderson", "Walker", "Gibson", "Thompson")
		    
		var emails = Array("gmail", "yahoo", "aol", "outlook", "mail")
		    
		
		var userName = names(Random.nextInt(names.length)) + TestCommon.getUserCounter.toString.trim()
		var email =  userName + "@" + emails(Random.nextInt(emails.length)) + ".com"
		var password = "password" + userName

		return(userName, email, password)
	}
	
	
 	def ttt_getValue(str: String, name: String): String = {
 	  
  	   var value = ""
 	  			
 	   if (str.length > 0) {
 		   var x = str.indexOf(""""""" + name + """"""") + name.length + 3
 		   value = str.slice(x,str.indexOf(",", x))
 	   }
 		
  		return value 
 	}
 	
 	
	

 	def temp() : (String, String, String) = {
 	  
 		var one = "one"
 		var two = "Two"
 		var three= "Three"
 		  
 		return (one, two, three)
 	}
 	
  
} // End of object TestCommon()
