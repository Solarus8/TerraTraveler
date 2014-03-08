import org.specs2.mutable._
import org.specs2.mutable.Specification
import org.specs2.runner._
import org.junit.runner._

import play.api.libs.json._

import play.api.test._
import play.api.test.Helpers._
import play.api.test.Helpers.await


import java.lang.Object

object TripsApi  {
  
	// =================================================================================
    //                          ttt_Trips_createNewTrip
	// 
	def ttt_Trips_createNewTrip(newTrip:JsValue): (Boolean, Long, JsValue) = {
	  
		// API Documentation version 20
		/*
				example request
				curl \
					--header "Content-type: application/json" \
					--request POST \
					--data '{"name" : "Istanbul 2014", "desc" : "It's been too long since I visited my favorite city. I'm going to booking a flight this week!", "userId" : 1, "dateFrom" : "2014-02-10", "dateTo" : "2014-02-10", "status" : "DEFAULT"}' \
					ec2-54-193-80-119.us-west-1.compute.amazonaws.com:9000/api/v1/trips \
					| python -mjson.tool
				
				example response
				{
				    "trip": {
				        "dateFrom": 1392019200000,
				        "dateTo": 1392019200000,
				        "desc": "It has been too long since I visited my favorite city. I am going to booking a flight this week!",
				        "id": 4,
				        "name": "Istanbul 2014",
				        "status": "DEFAULT",
				        "userId": 1
				    }
				}
		*/
	  
		var tripId:Long = 0
	  
		var (passFailStatus:Boolean, results:JsValue) = TestCommon.ttt_sendApiCommand(newTrip, 
		    "trips", "Create New Trip")
		    
		if (passFailStatus == true) {(tripId = (results \ "trip" \ "id").as[Long])}
		    
	  
		    return(passFailStatus, tripId, results)
	} // End of ttt_Trips_createNewTrip
	
	
	// =================================================================================
	//                         ttt_Trips_getTripById
	//
	def ttt_Trips_getTripById(tripId:Long):(Boolean, JsValue) = {
	  
		// API Documentation version 20
		/*
			example request
			curl \
				--header "Content-type: application/json" \
				--request GET \
				--data '{}' \
				ec2-54-193-80-119.us-west-1.compute.amazonaws.com:9000/api/v1/trips/1 \
				| python -mjson.tool
			
			example response
			{
			    "trip": {
			        "dateFrom": 1392019200000,
			        "dateTo": 1392019200000,
			        "desc": "It has been too long since I visited my favorite city. I am going to booking a flight this week!",
			        "id": 4,
			        "name": "Istanbul 2014",
			        "status": "DEFAULT",
			        "userId": 1
			    }
			}
		*/	
	  
		var (passFailStatus:Boolean, results:JsValue) = TestCommon.ttt_sendApiCommand(Json.obj(), 
		    "trips/" + tripId.toString.trim(), "Get Trip by Id")
		    
		return (passFailStatus, results)
	  
	} // End of ttt_Trips_getTripById
	
	
  
  
} // End of object TripsTests
