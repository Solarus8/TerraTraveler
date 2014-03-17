import play.api.libs.json._

import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.functional.syntax._
import play.api.data.validation.ValidationError


trait LocationsApiTests extends org.specs2.mutable.Specification {
  
   	// ================================================================================
 	//                       ttt_placesApiTest_createPlace
 	//
 	def ttt_placesApiTest_createPlace() {
 	  
 		var name = "Hacker Dojo"
 		var desc = "The place to be"
 		var cat  = "PARK"
 		var url  = "spam@spam.com"
 		var latitude:Double  =  37.386052
 		var longitude:Double =  -122.083851
 		
 		var createPlace = Json.obj(
			"name" -> name,
			"desc" -> desc, 
			"cat"  -> cat,
			"url" -> url, 
			"latitude" -> latitude, 
			"longitude" -> longitude	
		)

		var (placeId:Long, newPlace:JsObject) =  LocationsApi.ttt_Places_CreatePlace(createPlace, 0)
 
  		"ttt_placesApiTest_createPlace - Create a place and verify the place was created" in {
 		  
 		    cat       must beEqualTo((newPlace \ "place" \ "cat").as[String])
 		    desc      must beEqualTo((newPlace \ "place" \ "desc").as[String])
 			name      must beEqualTo((newPlace \ "place" \ "name").as[String])			
 			url       must beEqualTo((newPlace \ "place" \ "url").as[String])

  
 		}
 

 	} // End of ttt_placesApiTest_createPlace
 
 	
 	// =================================================================================
 	//                       ttt_placesApiTest_getPlaceById
 	//
 	// Create a place then get the id number.  User the place id number to
 	// verify the place was created
 	def ttt_placesApiTest_getPlaceById() {
 		var name = "Computer History Museum2"
 		var desc = "History of the computer"
 		var cat  = "PARK"
 		var url  = "james@hackerdojo.com"
 		var latitude  =  37.414452
 		var longitude =  -122.077581
 		
 		var place = Json.obj(
			"name" -> name,
			"desc" -> desc, 
			"cat"  -> cat,
			"url" -> url, 
			"latitude" -> latitude, 
			"longitude" -> longitude	
		)
		
		var (placeId:Long, newPlace:JsValue) =  LocationsApi.ttt_Places_CreatePlace (place, 0)
		var placeById:JsValue = LocationsApi.ttt_Places_getPlaceById(placeId)
		

 	  
		"ttt_placesApiTest_getPlaceById - Verify the place Id matches the corrent place" in {
 		  
 			name      must beEqualTo((newPlace \ "place" \ "name").as[String])
 			desc      must beEqualTo((newPlace \ "place" \ "desc").as[String])
			cat       must beEqualTo((newPlace \ "place" \ "cat").as[String])
 			url       must beEqualTo((newPlace \ "place" \ "url").as[String])
 			

 		}
 	  
 	  
 	} // End of ttt_placesApiTest_getPlaceById
 	
 	// =================================================================================
 	//                   ttt_placesApiTest_getPlacesByLatitudeLongitudeAndRadius
 	//
 	def ttt_placesApiTest_getPlacesByLatitudeLongitudeAndRadius () {
 
 
 		var radiusSearch = 1000  // Get latitude and longitude within this radius in meters
 		var numberPlacesInRange = 5 
 		var distanceStatus = true
 		var distanceErrorMsg = ""
 
 	  
 		var name = "Puerto Rico"
 		var desc = "Visit Puerto Rico"
 		var cat  = "COUNTRY"
 		var url  = "www.puertorico.com"
 		var latitude:Double  =  18.265696
 		var longitude:Double =  -66.485825
	
  			
 		 var place = Json.obj(
			"name" -> name,
			"desc" -> desc, 
			"cat"  -> cat,
			"url" -> url, 
			"latitude" -> latitude, 
			"longitude" -> longitude	
		)


		// Create first place at center of radius
		var (placeId:Long, placeResults:JsValue) = LocationsApi.ttt_Places_CreatePlace(place, 0)

		var lat = new Array[Double](6)
		var lon = new Array[Double](6)
		var places = new Array[JsValue](6)	// Json for new places that get created	
		var range = Array(.25, 0.5, 0.75, 0.95, 1.05, 1.2) // Distance in 
				
		// Create new places using the distance from the first place created
 		for(i <- 0 to 5) { 	
 		  
 		    //  Get latitude and longitude after moving x meters north of original place
 			var (lat1, lon1) = TestCommon.ttt_distanceAndBearing(latitude, longitude, range(i), 0)
 			lat(i) = lat1
 			lon(i)  = lon1
 			
 			
 			
 			// Modify the latitude and longitude in the Json object for the place
 			var tempPlace = place.as[JsObject] ++ Json.obj("latitude" -> lat1) ++ Json.obj("longitude" -> lon1) 			

 			// Create the new place
 			var(placeId, newPlace) = LocationsApi.ttt_Places_CreatePlace(tempPlace, 0) 			
 			places(i) = newPlace
 						
 		}
				
		var (passFailStatus:Boolean, results:JsValue) = LocationsApi.ttt_Places_getPlacesByLatitudeLongitudeAndRadius(latitude, longitude, radiusSearch)

	  
 		println("\n--------------------Places within radius --------")
 		println(results)
 
		 
 		 // Get the latitude and longitude
 		 implicit val personReader: Reads[(Double, Double)] = (
			 (__ \ "lat").read[Double] and 
			 (__ \ "lon").read[Double]
		 ).tupled
		 		 
		 val locations = (results \ "places").as[List[(Double, Double)]]
		 locations.foreach(value => 
			{
				println("the value2 is " + value)

				var a = value.toString.replace("(","").replace(")","").split(",")
				
				// Array must contain a latitude and longitude
				if (a.length == 2) {
					var distance = TestCommon.ttt_calculateDistanceBetweenTwoPoints(latitude, longitude, a(0).toDouble, a(1).toDouble)

					if ((distance * 100) > radiusSearch ) {
						distanceStatus = false
						distanceErrorMsg = "\nDistance of " + (distance * 100) + " meters greater than " 
							+ radiusSearch + " for latutude " + latitude + " and longitude " + longitude 					
					}

				} else {
					distanceStatus = false // Found unknown latitude and longitude
					distanceErrorMsg = "\nArray is missing parameters for latitude and longitude"
				}			  	
			}
		) 
		var count = locations.size
		
		if (count != numberPlacesInRange) {
			distanceStatus = false
			distanceErrorMsg = "\nFound " + count + " places in range instead of " + numberPlacesInRange		  
		}
		
		if (distanceStatus == true) {
			"ttt_placesApiTest_getPlacesByLatitudeLongitudeAndRadius, Verifed " + numberPlacesInRange + "were in range" in {success}		  
		} else {
			"ttt_placesApiTest_getPlacesByLatitudeLongitudeAndRadius, Unable to verify " + numberPlacesInRange + "were in range" in {failure}
		}
		  

 		
 	} // End of ttt_placesApiTest_getPlacesByLatitudeLongitudeAndRadius

  
  
}  // End of trait LocationsApiTests
