import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._
import play.api.test.Helpers.await
import play.api.libs.json._
import play.api.libs.ws._

object PlacesTest extends ApplicationSpec {
 
  

  	def ttt_Places_CreateNewPlace(place: JsObject,radiusMeters:Long): (JsObject, String) = {
  	  
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
        
        var temp = ""
	   
        try {
        
        	temp = Helpers.await(WS.url(serverLocation + "/api/v1/locations/place").post(place)).body
        } catch {
        	       	
        	case e: Exception => println("Create New Place - exception caught: " + e);          
        }
	   	   
		//println("\n\n=========== Create place ================\n" + place + "\n======== Place Created =====\n" + temp + "\n========")


	   	return (place, temp)
	}
 	

  	def ttt_Places_getPlaceById(id: String): String = {

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
  	  
 		var user = Json.obj()
 
  
  		var temp = Helpers.await(WS.url(serverLocation + "/api/v1/locations/place/" + id).get()).body
  	  
  		return temp
  				
  	}
  
} // end of object PlacesTest
  



