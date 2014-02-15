
import scala.io.Source
import scalax.io._

import java.io._
import java.io.PrintWriter
import java.io.File


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
		println("(((((( User id = " + userId)
	  
		userId = id
	}
	
	def getPlaceId: Long = {
		
		return placeId
	}
	
	def setPlaceId(id: Long) {
	  
		println("(((((( Place id = " + placeId)
		placeId = id		
	}
	

	// Each user name has a unique number in the name to prevent 
	// duplicate user name errors.  This function increments the counter and 
	// saves the count in a file.
	//
	// If you decide to erase the database then you can delete the file
	// ttt_testData.txt at the same time to start over with the count at one.	
	def getUserCounter: Long = {
	  
		val fileName = "ttt_testData.txt"
		  		
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

	    println ("========== Counter = " + userCounter)
	    
	    userCounter = userCounter + 1
	    
		val writer = new PrintWriter(new File(fileName))
		writer.write(userCounter.toString)
		writer.close()
				
		return userCounter
	}
	
	
	

  
} 
