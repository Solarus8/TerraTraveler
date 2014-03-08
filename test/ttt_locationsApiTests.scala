import play.api.libs.json._


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

		var (placeId:Long, newPlace:JsObject) =  LocationsApi.ttt_Places_CreateNewPlace(createPlace, 0)
 
  		"ttt_placesApiTest_createPlace called " + name in {
 		  
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
		
		var (placeId:Long, newPlace:JsValue) =  LocationsApi.ttt_Places_CreateNewPlace (place, 0)
		var placeById:JsValue = LocationsApi.ttt_Places_getPlaceById(placeId)
		

 	  
		"ttt_placesApiTest_getPlaceById - " + name + " with id = " + placeId + " " in {
 		  
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
 	  
 		var name = "Santa Cruz Beach Boardwalk"
 		var desc = "Beach and amusement park"
 		var cat  = "PARK"
 		var url  = "boardwalk@santacruz.com"
 		var latitude:Double  =  36.964207
 		var longitude:Double =  -122.018237
 
 		
 		var radiusSearch = 1000
 
 			
 		 var place = Json.obj(
			"name" -> name,
			"desc" -> desc, 
			"cat"  -> cat,
			"url" -> url, 
			"latitude" -> latitude, 
			"longitude" -> longitude	
		)
		
 		
	 	  
// 		var(placeId:Long, newPlace:JsValue) = PlacesTest.ttt_Places_CreateNewPlace(place, 0)
//println("<<<<<<<<<<< Place id=" + placeId + ">>>>>>>>>>>>") 	  
 	  
 		var (passFailStatus:Boolean, results:JsValue) = LocationsApi.ttt_Places_getPlacesByLatitudeLongitudeAndRadius(latitude, longitude, radiusSearch)
 	  
 		println("\n--------------------Santa Cruz with on place in radius --------")
 		println(results)
 		println("\n-------------------------End places in radius ---------")
 
 		
 	

 		
var(passFailStatus2:Boolean, results2:JsValue) = LocationsApi.ttt_Places_getPlacesByLatitudeLongitudeAndRadius(37.386052, -122.083851, radiusSearch)

 
println("\n----------------Hacker Dojo with 1200 places in radius --------")
println(results2)
println("\n-------------------------End places in radius ---------")
 
 		
 		
 		"ttt_placesApiTest_getPlacesByLatitudeLongitudeAndRadius - Lots of places cause timeout" in {
 			failure
 		}

 		
 	} // End of ttt_placesApiTest_getPlacesByLatitudeLongitudeAndRadius

  
  
}  // End of trait LocationsApiTests
