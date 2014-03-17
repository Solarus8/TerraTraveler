
import scala.io.Source
import scalax.io._

import java.io._
import java.io.PrintWriter
import java.io.File

import scala.math

import scala.util.Random

import play.api.libs.json.Json
import play.api.libs.json._

import play.api.test._
import play.api.test.Helpers._
import play.api.test.Helpers.await

import play.api.libs.ws._
import play.api.mvc.Results._

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar



object TestCommon {

	val EMPTY_FIELD_LONG:Long = 999999
	val EMPTY_FIELD_DOUBLE:Double = 999999.99
	val EMPTY_FIELD_STRING:String = "EMPTY_FIELD"
  
	val serverLocation = "http://localhost:9998"
	
	// Each user name consists of text and a number.  This counter increments
	// the number to avoid getting duplicate user errors
	var userCounter:Long = 1
		  
	// Save a user Id to be used in multiple locations in the test
	private var userId = ""	
	  	
	def getUserId: String = {
		return userId
	}
	
	def setUserId(id: String) {
	  
		userId = id
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
	def ttt_distanceAndBearing(latitude:Double, longitude:Double, d:Double, brng:Double): (Double, Double) = {
	  	  
		var R = 6371.0  // Radius of the earth 6371.0
		
		var lat1 = latitude.toRadians
		var lon1 = longitude.toRadians
				
		var lat2 = Math.asin( Math.sin(lat1)*Math.cos(d/R) + 
          Math.cos(lat1)*Math.sin(d/R)*Math.cos(brng) );
		var lon2 = lon1 + Math.atan2(Math.sin(brng)*Math.sin(d/R)*Math.cos(lat1), 
                 Math.cos(d/R)-Math.sin(lat1)*Math.sin(lat2));
	
		println("\n************* New longitude and latitude *****************")
		println("Latitude  = " + lat2.toDegrees)
		println("Longitude = " + lon2.toDegrees)
		
		return (lat2.toDegrees, lon2.toDegrees)
	
	} // End of ttt_distanceAndBearing

	
	// =================================================================================
	//                   ttt_calculateDistanceBetweenTwoPoints
	//
	//    Uses the Haversine formula to calculate the distance between two
	//    coordinates and returns the distance in kilometers.
	//
	def ttt_calculateDistanceBetweenTwoPoints(latitude1:Double, longitude1:Double, latitude2:Double, longitude2:Double): Double = {
 	  
		val earthRadius = 6371.0
	    
	    // convert to radians
	    var lat1 = latitude1.toRadians
	    var lng1 = longitude1.toRadians;
	    var lat2 = latitude2.toRadians
	    var lng2 = longitude2.toRadians

	    
	    var dlon:Double = lng2 - lng1;
	    var dlat:Double = lat2 - lat1;
	
	    var a:Double = Math.pow((Math.sin(dlat/2)),2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon/2),2);
	
	    var c:Double = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

	    return earthRadius * c;

	} // End of ttt_calculateDistanceBetweenTwoPoints
	
	
	// =================================================================================
	//                   ttt_generateUserName()
	//	
	// Generate a random user name, email and password.  
	def ttt_generateUserNameEmailAndPassword(): (String, String, String) = {

	  
		var names = Array("John", "Tom", "Jon", "Tim", "Jun", "James", "Rick", "Casey", "Mark", "Jack", "Sam", 
		    "Sally", "Pam", "Jan", "Sarah", "Susie", "Mary", "Marie", "Sue", "Jones", "Johnson", 
		    "Smith", "Voelm", "Anderson", "Gibson", "Thompson")
		    
		var emails = Array("gmail", "yahoo", "aol", "outlook", "mail", "spam")
		
		var userName = names(Random.nextInt(names.length)) + TestCommon.getUserCounter.toString.trim()
		var email =  userName + "@" + emails(Random.nextInt(emails.length)) + ".com"
		var password = "password" + userName

		return(userName, email, password)
	} // End of ttt_generateUserNameEmailAndPassword
		
 	
	val activityType = Map(
		"1"->"Running",
		"2"->"Gym",
		"3"->"Walk",
		"4"->"Clubbing",
		"5"->"Cafe",
		"6"->"Dog walk",
		"7"->"Dog park",
		"8"->"Child park",
		"9"->"Site-see",
		"10"->"Drinks",
		"11"->"Lunch",
		"12"->"Dinner",
		"13"->"Breakfast",
		"14"->"Theater",
		"15"->"Attend Game",
		"16"->"Play game sport court or field",
		"17"->"Museum or Gallery",
		"18"->"Shopping",
		"19"->"Bicycling",
		"20"->"Dancing",
		"21"->"Movie",
		"22"->"Concert",
		"23"->"Event",
		"24"->"Ride-share",
		"26"->"Hike",
		"27"->"Picnic",
		"28"->"Park",
		"29"->"Night Sky",
		"30"->"Games board or computer",
		"31"->"Hacking",
		"32"->"Comedy",
		"33"->"Apartment",
		"34"->"Art Photography",
		"35"->"Mall",
		"37"->"Beach",
		"39"->"Live music",
		"40"->"Demonstration",
		"41"->"Rally"
	)
  
	var activityCategories = Map (
		"1"->"Fitness",
		"2"->"Outdoors",
		"3"->"Nightlife",
		"4"->"Eating",
		"5"->"Pets",
		"6"->"Kids",
		"7"->"Sports",
		"8"->"Entertainment",
		"9"->"Tech",
		"10"->"Transportation",
		"11"->"Housing share",
		"12"->"Maker",
		"13"->"Performance",
		"14"->"Hangout",
		"15"->"Cultural",
		"16"->"Traveler",
		"17"->"Activism",
		"18"->"Shop",
		"19"->"Professional",
		"20"->"Create",
		"21"->"After work"
	)
 	
	
	// =================================================================================
	//                        ttt_convertDateTimeToMilleconds
	//
	def ttt_convertDateTimeToMilleconds(timeDate:String, format:String):Long = {

	
	  
		var timeFormat = ""
	  
		if (format == "".trim()) { timeFormat  = "yyyy-MM-dd hh:mm:ss.S"} else {timeFormat = format}

	  	var cal = Calendar.getInstance();
	  	var sdf = new SimpleDateFormat(timeFormat);
	  	cal.setTime(sdf.parse(timeDate));// all done
	  	var milliseconds = cal.getTimeInMillis()

	
	  	return milliseconds
	} // End of ttt_convertDateTimeToMilleconds
	
	

	
	
	// =================================================================================
	//                        ttt_sendApiCommand
	//
	def ttt_sendApiCommand(jsonToSend:JsValue, apiString:String, description:String): (Boolean, JsValue, String) = {
	
		var tempJson:JsValue = Json.obj()
		var tempText = ""
		var passFailStatus:Boolean = false
		var apiUrl = TestCommon.serverLocation +  "/api/v1/" + apiString

		try {
		  
			if (jsonToSend == Json.obj()) {
//println("++++++++++ Get - " + apiUrl + " - " + description)			  
				//temp = Json.parse(Helpers.await(WS.url(apiUrl).get()).body)
				tempText = Helpers.await(WS.url(apiUrl).get()).body
				tempJson = Json.parse(tempText)
				passFailStatus = true
				
			} else {
//println("+++++++++ Post - " + apiUrl + " - " + description)
			    var tempText = Helpers.await(WS.url(apiUrl).post(jsonToSend)).body
			    tempJson = Json.parse(tempText)
			    
				passFailStatus = true
			}
					
	
       } catch {
        	       	
        	case e: Exception => 
        	  println("\n\n======== ERROR - " + description )
			  println("    ** Exception - " + e)
        	  println("    ** Failed on " + apiUrl)
        	  println("    ** Json object that was sent \n" + jsonToSend)
        	  println("    ** Json object recieved" + tempJson)
        	  println("    ** Text recieved" + tempText)
        	  println("==================================\n\n")
        	  
        	  passFailStatus = false
        	        
        }
       
		return (passFailStatus, tempJson, tempText)
	} // End of ttt_sendApiCommand

 
  
} // End of object TestCommon()


// =================================================================================
//    
//
object Places {
  
	def placeSternGrove(): JsObject = {

	  	var place = Json.obj (
	  	    "name" -> "Stern Grove",
	  	    "desc" -> "Free Summer concerts!", 
	  	    "cat" -> "PARK", 
			"url" -> "http->//www.sterngrove.org/", 
			"latitude" -> 37.735681, 
			"longitude" -> -122.476959
		)
		return place
	}
	
	def placeYosimiteNationalPark(): JsObject =  {
	  	var place = Json.obj (
	  	    "name" -> "Yosemite National Park",
	  	    "desc" -> "Hiking, camping and skiing", 
	  	    "cat" -> "PARK", 
			"url" -> "http->//www.yosemit.org/", 
			"latitude" -> 37.865348, 
			"longitude" -> -119.538374
		)
		return place
	}
	
	def placeCentralParkNewYorCity(): JsObject =  {
	  	var place = Json.obj (
	  	    "name" -> "Central Park, New York City",
	  	    "desc" -> "Large park in Manhattan", 
	  	    "cat" -> "PARK", 
			"url" -> "http->//www.centralpark.org/", 
			"latitude" -> 40.781953, 
			"longitude" -> -73.965787
		)
		return place
	}
	
	def placeGreatSandHillsCanada(): JsObject =  {

		var place = Json.obj (
	  	    "name" -> "Great Sand Hills, Canada",
	  	    "desc" -> "I clicked a random spot on the map", 
	  	    "cat" -> "PARK", 
			"url" -> "http->//www.greatsandhills.org/", 
			"latitude" -> 50.531645, 
			"longitude" -> -109.037247
		)
		return place
	  
	}
	
	def placePuertoRico(): JsObject = {
	  
  		var place = Json.obj (
	  	    "name" -> "Puerto Rico",
	  	    "desc" -> "Visit Puerto Roce", 
	  	    "cat" -> "COUNTRY", 
			"url" -> "www.puertoroco.com", 
			"latitude" -> 18.265696, 
			"longitude" -> -66.485825
		)
		return place
	  
	}
	
  	
} // End of object Places
