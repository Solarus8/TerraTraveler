import play.api.libs.json._
import java.text.SimpleDateFormat
import java.util.Date

object TripsApiTests extends ApplicationSpec {
  
	def test1 {
		"Test1" in {1 must beEqualTo(1)}
	}
  
  
  	
 	// =================================================================================
 	//                       ttt_TripsApiTest_createNewTrip
 	//
 	def ttt_TripsApiTest_createNewTrip {
 	
 		// TODO - Add tests for PLANNING, ONGOING, ENDED, DEFAULT
 
"Beginning of trip test" in {1 must beEqualTo(1)} 	  
 		var latitude = 37.865348
  		var longitude = -119.538374
  		var role = "NORM"
  	    
  		// Create user
  		var(userId:Long, user:JsValue) = UsersApi.ttt_Users_createUser(latitude, longitude, role)

  		 var name  =  "Istanbul 2014"
  		 var desc  = """It's been too long since I visited my favorite city. I'm going to booking a flight this week!""" 
  		 var dateFrom = "2014-02-10"
  		 var dateTo = "2014-02-10"
  		 var status = "DEFAULT"
  		
  		 var newTrip = Json.obj(	  
  				"name"     -> "Istanbul 2014", 
  				"desc"     -> desc, 
  				"userId"   -> userId, 
  				"dateFrom" -> dateFrom,
  				"dateTo"   -> dateTo, 
  				"status"   -> status
 		)
 		
 		var (passFailStatus:Boolean, tripId:Long, resultsDefault:JsValue) = TripsApi.ttt_Trips_createNewTrip(newTrip)
 		var sdf = new SimpleDateFormat("yyyy-MM-dd");
 
 		
 		"ttt_TripsApiTest_createNewTrip" in {
 		
 			var (passFailStatus1:Boolean, tripId:Long, resultsDefault:JsValue) = TripsApi.ttt_Trips_createNewTrip(newTrip) 
 		  
 			name       must beEqualTo((resultsDefault \ "trip" \ "name").as[String])
 			desc 	   must beEqualTo((resultsDefault \ "trip" \ "desc").as[String])
 			userId 	   must beEqualTo((resultsDefault \ "trip" \ "userId").as[Long]) 
 			
   			var newDateFrom = (resultsDefault \ "trip" \ "dateFrom").as[Long]
 			var newDateTo = (resultsDefault \ "trip" \ "dateFrom").as[Long]
 
 			dateFrom must beEqualTo(sdf.format(new Date(newDateFrom)))
 			dateTo must beEqualTo(sdf.format(new Date(newDateTo)))
 			
 		} // End of matchers
 	  
 	} // End of ttt_TripsApiTest_createNewTrip
 	
 	
 	// =================================================================================
 	//                    ttt_TripsApiTest_getTripsById
 	
 	def ttt_TripsApiTest_getTripsById() {
 
 	  	var latitude = 37.865348
  		var longitude = -119.538374
  		var role = "NORM"
  	    
  		// Create user
  		var(userId:Long, user:JsValue) = UsersApi.ttt_Users_createUser(latitude, longitude, role)

  		 var name  =  "Istanbul 2015"
  		 var desc  = "I am going to visit again"
  		 var dateFrom = "2015-02-10"
  		 var dateTo = "2015-02-10"
  		 var status = "PLANNING"
  		
  		 var newTrip = Json.obj(	  
  				"name"     -> "Istanbul 2015", 
  				"desc"     -> desc, 
  				"userId"   -> userId, 
  				"dateFrom" -> dateFrom,
  				"dateTo"   -> dateTo, 
  				"status"   -> status
 		)
 
 		
 		var sdf = new SimpleDateFormat("yyyy-MM-dd");
 		
 		// Create new trip and get the tripId
 		var (passFailStatus:Boolean, tripId:Long, resultsDefault:JsValue) = TripsApi.ttt_Trips_createNewTrip(newTrip)
 		
 		var (passFailId:Boolean, results:JsValue) = TripsApi.ttt_Trips_getTripById(tripId)
 			
 		"ttt_TripsApiTest_getTripsById" in {
 		
 			var (passFailStatus:Boolean, tripId:Long, results:JsValue) = TripsApi.ttt_Trips_createNewTrip(newTrip)
 		  
 			name       must beEqualTo((results \ "trip" \ "name").as[String])
 			desc 	   must beEqualTo((results \ "trip" \ "desc").as[String])
 			userId 	   must beEqualTo((results \ "trip" \ "userId").as[Long]) 	 
 			
   			var newDateFrom = (results \ "trip" \ "dateFrom").as[Long]
 			var newDateTo = (results \ "trip" \ "dateFrom").as[Long]
 
 			dateFrom must beEqualTo(sdf.format(new Date(newDateFrom)))
 			dateTo must beEqualTo(sdf.format(new Date(newDateTo)))
 			
  			
 		} // End of matchers
 	  
 	  
 	  
 	}  // End of ttt_TripsApiTest_getTripsById
  
  
  
}

