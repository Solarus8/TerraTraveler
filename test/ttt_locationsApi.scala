
import org.specs2.mutable._
import org.specs2.mutable.Specification
import org.specs2.runner._
import org.junit.runner._

import play.api.libs.json._

import play.api.test._
import play.api.test.Helpers._
import play.api.test.Helpers.await


import java.lang.Object

import controllers._

import scala.util.Random

object LocationsApi{
 
  
	// =================================================================================
    //                ttt_Places_CreateNewPlace
    //
    //     If radiusMeters is greater than zero then generate random latitude and
    //     longitude within radiusMeters
    //
    //     If name, url or description are blank then include random text"
    //
  	def ttt_Places_CreatePlace(place:JsValue,radiusMeters:Long): (Long, JsValue) = {
  	  
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
                
        var placeId:Long = 0
        var locId:Long =0

        var(passFailStatus:Boolean, temp:JsValue, error:String) = TestCommon.ttt_sendApiCommand(place, "locations/place", "Create Place")
        if (passFailStatus == true)
        {
	        placeId = (temp \ "place" \ "id").as[Long]
	    	locId   = (temp \ "place" \ "locId").as[Long] 
        }
 
       
	   	return (placeId, temp)
	}  // End function ttt_Places_CreateNewPlace
 	
  	
  	// =================================================================================
  	//                      ttt_Places_createPlace3rdPartyReference
  	//
  	def ttt_Places_createPlace3rdPartyReference (placeId:Long, thirdPartyID:Long, thirdPartyPlaceId:String, thirdPartyRef:String): (Boolean, JsValue) =  {
 
  		// TODO - Not tested
  	  
  		/*
  			curl \
				--header "Content-type: application/json" \
				--request POST \
				--data '{ "placeId" : "", "thirdPartyID" : 1, "thirdPartyPlaceId" : "375H216T938572PQ810", "thirdPartyRef" : "8572PQ810375H216" }' \
				ec2-54-193-80-119.us-west-1.compute.amazonaws.com:9000/api/v1/locations/place-synd \
				| python -mjson.tool
  		 */
  	  
  		var temp = Json.obj(
			"placeId" -> placeId,
			"thirdPartyID" -> thirdPartyID,
			"thirdPartyPlaceId" -> thirdPartyPlaceId,
			"thirdPartyRef" -> thirdPartyRef
		
		)
  		  	  
  		var (passFailStatus:Boolean, results:JsValue, error:String) = TestCommon.ttt_sendApiCommand(temp, "locations/place-synd", "Create 3rd party reference")
    		  	
  		return (passFailStatus, results)
  	} // End of ttt_Places_createPlace3rdPartyReference
  	
  	
    // =================================================================================
  	//               ttt_Places_get3rdPartyReferenceById
  	// 
  	def ttt_Places_get3rdPartyReferenceById (id:Long): (Boolean, JsValue) = {
  		/*
			curl \
				--header "Content-type: application/json" \
				--request GET \
				--data '{}' \
				ec2-54-193-80-119.us-west-1.compute.amazonaws.com:9000/api/v1/locations/place-synd/3 \
				| python -mjson.tool
		*/
  	  
   		var (passFailStatus:Boolean, results:JsValue, error:String) = TestCommon.ttt_sendApiCommand(Json.obj(), 
   				"locations/place-synd/" + id.toString, "Get Place 3rd Party Reference by ID")
 	  
  	  
		return (passFailStatus, results)
  	}  // End of ttt_Places_get3rdPartyReferenceById

  	
  	
   	// =================================================================================
  	//             ttt_Places_getPlace3rdPartyReferenceByTerraTravelerPlaceId 	
  	//
  	def ttt_Places_getPlace3rdPartyReferenceByTerraTravelerPlaceId(placeId:Long): (Boolean, JsValue) = {
  		/*
		  	curl \
				--header "Content-type: application/json" \
				--request GET \
				--data '{}' \
				ec2-54-193-80-119.us-west-1.compute.amazonaws.com:9000/api/v1/locations/place/2/place-synd \
				| python -mjson.tool
  		*/
  	  
  		var (passFailStatus:Boolean, results:JsValue, error:String) = TestCommon.ttt_sendApiCommand(Json.obj(), 
   				"locations/place/" + placeId.toString.trim() + "/place-synd/" ,
   				"Get Place 3rd Party Reference by TerraTraveler Place ID")
  	  
 				
		return (passFailStatus, results)
  	} // End of ttt_Places_getPlace3rdPartyReferenceByTerraTravelerPlaceId
  	
	
  	// =================================================================================
	//                ttt_Places_getPlacesByLatitudeLongitudeAndRadius
  	//
  	def ttt_Places_getPlacesByLatitudeLongitudeAndRadius(latitude:Double, longitude:Double, radius:Long): (Boolean, JsValue) = {
 
  		// Api documentaion version 20
		/*
			curl \
				--header "Content-type: application/json" \
				--request GET \
				--data '{}' \
				localhost:9000/api/v1/locations/place/37.774929/-122.419416/9500 \
				| python -mjson.tool
			
			example response
			{
			    "places": [
			        {
			            "cat": "PARK",
			            "desc": "In Golden Gate Park",
			            "id": 3,
			            "lat": 37.768395,
			            "locId": 5,
			            "lon": -122.492259,
			            "name": "Polo Fields",
			            "url": "http://sfrecpark.org/destination/golden-gate-park/"
			        },
		*/
  	  
		//  If no events in range
		//	{
		//	    "events": []
		//	}
   
	  	var apiString = "locations/place/" + latitude.toString + "/" + longitude.toString + "/" + radius.toString
	  	var description = "Get places by Latitude Longitude and Range"
	  	
	  	var (passFailStatus:Boolean, results:JsValue, error:String) = TestCommon.ttt_sendApiCommand(Json.obj(), apiString, description)
  
  		  	
	  	return (passFailStatus, results)
  	  
  	}  // End of ttt_Places_getPlacesByLatitudeLongitudeAndRadius
  	
  	
  	// =================================================================================
  	//                    ttt_Places_getPlaceById
  	//
  	def ttt_Places_getPlaceById(id: Long): JsValue = {

  		/*
		  	  curl \
			--header "Content-type: application/json" \
			--request GET \
			--data '{}' \
			localhost:9000/api/v1/locations/place/3 \
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
 	
 		
        var(passFailStatus:Boolean, temp:JsValue, error:String) = TestCommon.ttt_sendApiCommand(Json.obj(), 
            "locations/place/" + id.toString.trim(), "Get Plce By Id")
	   	  
  		return temp
  				
  	} // End of ttt_Places_getPlaceById
  	
 
} // end of object PlacesTest
  



