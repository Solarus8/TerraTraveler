
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

import scala.util.Random

object PlacesTest {
 
  
	// =================================================================================
    //                ttt_Places_CreateNewPlace
    //
    //     If radiusMeters is greater than zero then generate random latitude and
    //     longitude within radiusMeters
    //
    //     If name, url or description are blank then include random text"
    //
  	def ttt_Places_CreateNewPlace(place:JsObject,radiusMeters:Long): (Long, JsValue) = {
  	  
		/*	  
			curl \
				--header "Content-type: application/json" \
				--request POST \
				--data '{ "name" : "Stern Grove", "desc" : "Free Summer concerts!", "cat" : "PARK", 
					"url" : "http://www.sterngrove.org/", "latitude" : 37.735681, "longitude" : -122.476959 }' \
				localhost:9998/api/v1/locations/place \
				| python -mjson.tool
	
			{
			    "place": {
			        "cat": "PARK",
			        "desc": "Free Summer concerts!",
			        "id": 3,
			        "locId": 3,
			        "name": "Stern Grove",
			        "url": "http://www.sterngrove.org/"
			    }
			}	
	    */	
	
  		// TODO - If name, url or description are blank then include random text"	  
        
        // If radius greater than zero then use random latitude and longitude
        if (radiusMeters > 0)
        {
        	// TODO - Add random latitude and longitude if radius greater than zero
        }
        
        var temp:JsValue = Json.obj()
        var placeId:Long = 0
        var locId:Long =0

        try {
 
        	temp = Json.parse(Helpers.await(WS.url(TestCommon.serverLocation +  "/api/v1/locations/place").post(place)).body)
        	placeId = (temp \ "place" \ "id").as[Long]
        	locId   = (temp \ "place" \ "locId").as[Long] 
        	         	
        } catch {
        	       	
        	case e: Exception => println("\n\nERROR - Create New Place - exception caught: " + e + "\nJson sent " + place + "\n\n");          
        }
       
	   	return (placeId, temp)
	}  // End function ttt_Places_CreateNewPlace
 	

	// =================================================================================
  	//                    ttt_Places_getPlaceById
  	//
  	def ttt_Places_getPlaceById(id: Long): JsValue = {

  		/*
		  	  curl \
			--header "Content-type: application/json" \
			--request GET \
			--data '{}' \
			ec2-54-193-80-119.us-west-1.compute.amazonaws.com:9000/api/v1/locations/place/3 \
			| python -mjson.tool
			
			example response
			{
			    "place": {
			        "cat": "PARK",
			        "desc": "In Golden Gate Park",
			        "id": 3,
			        "lat": 37.768395,
			        "locId": 5,
			        "lon": -122.492259,
			        "name": "Polo Fields",
			        "url": "http://sfrecpark.org/destination/golden-gate-park/"
			    }
			}			
			
		*/
  	  
 		var temp = Json.obj()
 
 		
        try {
        	var temp = Json.parse(Helpers.await(WS.url(TestCommon.serverLocation + "/api/v1/locations/place/" + id.toString.trim()).get()).body)
       
        } catch {
          
          	case e: Exception => println("ERROR - Get Place By Id - exception caught: " + e);
        }
  	  
  		return temp
  				
  	}
  	
  	
  	
  	 def testFunction(someJson:JsObject) {
  	   	  
  	    var result = Helpers.await(WS.url("http://localhost:9998/api/v1/locations/place").post(someJson)).body
  	  
  	    println("\n==========================================================================")
 		println("\n\n************* Json object inside function"  + someJson)
		println("\n**************** Place Json Result inside function \n" + result + "\n*************\n")  
  	     
  	}
  	
  
} // end of object PlacesTest
  



