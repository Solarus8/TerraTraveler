import play.api.libs.json._

trait TripsApiTests extends org.specs2.mutable.Specification {
  	  	
 	// =================================================================================
 	//                       ttt_TripsApiTest_createNewTrip
 	//
 	def ttt_TripsApiTest_createNewTrip {
 	
 		// TODO - Add tests for PLANNING, ONGOING, ENDED, DEFAULT
 
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
 		
		
 		"ttt_TripsApiTest_createNewTrip - Verify new trip was created" in {
 		  
 		   var dateFormat = "yyyy-MM-dd"
 		
 			var (passFailStatus1:Boolean, tripId:Long, resultsDefault:JsValue) = TripsApi.ttt_Trips_createNewTrip(newTrip) 
 		  
 			name       must beEqualTo((resultsDefault \ "trip" \ "name").as[String])
 			desc 	   must beEqualTo((resultsDefault \ "trip" \ "desc").as[String])
 			userId 	   must beEqualTo((resultsDefault \ "trip" \ "userId").as[Long]) 
 			
  			var timeFrom = TestCommon.ttt_convertDateTimeToMilleconds(dateFrom, dateFormat)
 			var timeTo   = TestCommon.ttt_convertDateTimeToMilleconds(dateTo, dateFormat)

 			timeFrom must beEqualTo((resultsDefault \ "trip" \ "dateFrom").as[Long])
 			timeTo   must beEqualTo((resultsDefault \ "trip" \ "dateTo").as[Long])
 			
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
  		 var dateTo = "2015-02-15"
  		 var status = "PLANNING"
  		
  		 var newTrip = Json.obj(	  
  				"name"     -> "Istanbul 2015", 
  				"desc"     -> desc, 
  				"userId"   -> userId, 
  				"dateFrom" -> dateFrom,
  				"dateTo"   -> dateTo, 
  				"status"   -> status
 		)
  		
 		var dateFormat = "yyyy-MM-dd"
 		
 		// Create new trip and get the tripId
 		var (passFailStatus:Boolean, tripId:Long, resultsDefault:JsValue) = TripsApi.ttt_Trips_createNewTrip(newTrip)
 		
 		var (passFailId:Boolean, results:JsValue) = TripsApi.ttt_Trips_getTripById(tripId)
 			
 		"ttt_TripsApiTest_getTripsById - Verify a trip was created and the trip can be verified by trip Id" in {
 		
 			var (passFailStatus:Boolean, tripId:Long, results:JsValue) = TripsApi.ttt_Trips_createNewTrip(newTrip)
 		  
 			name       must beEqualTo((results \ "trip" \ "name").as[String])
 			desc 	   must beEqualTo((results \ "trip" \ "desc").as[String])
 			userId 	   must beEqualTo((results \ "trip" \ "userId").as[Long]) 	 

 			var timeFrom = TestCommon.ttt_convertDateTimeToMilleconds(dateFrom, dateFormat)
 			var timeTo   = TestCommon.ttt_convertDateTimeToMilleconds(dateTo, dateFormat)

 			timeFrom must beEqualTo((results \ "trip" \ "dateFrom").as[Long])
 			timeTo   must beEqualTo((results \ "trip" \ "dateTo").as[Long])
		
 			 			
 		} // End of matchers
 	  
	  
 	}  // End of ttt_TripsApiTest_getTripsById
  
 
} // End of TripsApiTests

