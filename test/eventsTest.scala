import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._
import play.api.test.Helpers.await
import play.api.libs.json._
import play.api.libs.json.Reads
import play.api.libs.json.{Json, JsValue}
import play.api.libs.functional.syntax._
import play.api.libs.ws._

import java.io._
import java.io.PrintWriter
import java.io.File

import play.api.Play
import play.api.Play.current
import play.api.Application

import scala.sys.process._

object EventsTest {

	
	// =================================================================================
	//                 ttt_Events_createEvent
	//
	def ttt_Events_createEvent(event: JsObject): (Long, JsValue) = {	
	    // API Documentation version 20
		/*
			curl \
				--header "Content-type: application/json" \
				--request POST \
				--data '{ "from" : "2014-02-23 10:30:00.0", "to" : "", "title" : "Outside Lands", 
				    "activityType" : 22, "activityCategories" : [1,2], "placeId" : 1, 
				    "desc" : "Outside Lands: best music festival in S.F.", "minSize" : 2, 
				    "maxSize" : 50, "rsvpTot" : "", "waitListTot" : ""}' \
				localhost:9998/api/v1/events \
				| python -mjson.tool
		 */
	  
	  // No events in database {"events":[]}	
	  
  

		var temp = Json.parse(Helpers.await(WS.url(TestCommon.serverLocation + "/api/v1/events").post(event)).body)		
		var eventId = (temp \ "event" \ "id").as[Long]
		  
	    return (eventId, temp)
	} // End of ttt_Events_createEvent

	
	// =================================================================================
	//                          ttt_Events_getEventById
	//
	def ttt_Events_getEventById(id: Long): JsValue = {
		/*
		def ttt_Events_getEventById(eventId: String): String = {
		curl \
		--header "Content-type: application/json" \
		--request GET \
		--data '{}' \
		localhost:9998/api/v1/events/2 \
		| python -mjson.tool
		*/
	  
		var temp = Json.parse(Helpers.await(WS.url(TestCommon.serverLocation + "/api/v1/events/" + id.toString.trim()).get()).body)
		
		temp
	}  // End of ttt_Events_getEventById
	
	
	def ttt_Events_getEventsByLocationRadiusUsingLocationId(locationId:Long, radius:Long, 
	    activityType:Long, activityCategory:Long ) {

	  // TODO - Not finished, creating us
	  
		/*
			example request
			curl \
				--header "Content-type: application/json" \
				--request GET \
				--data '{}' \
				ec2-54-193-80-119.us-west-1.compute.amazonaws.com:9000/api/v1/events/location/5/5100 \
				| python -mjson.tool
						
			xample response   [MODIFIED]
			{
			    "events": [
			        {
			            "activityCategories": [
			                [
			                    1,
			                    2
			                ]
			            ],
			            "activityType": 22,
			            "description": "Outside Lands: best music festival in S.F.",
			            "from": 1393142400000,
			            "id": 1,
			            "lat": 37.768395,
			            "lon": -122.492259,
			            "maxSize": 50,
			            "minSize": 2,
			            "placeId": 1,
			            "rsvpTotal": null,
			            "title": "Outside Lands",
			            "to": null,
			            "waitListTotal": null
			        },
			        {
			            "activityCategories": [
			                [
			                    2,
			                    4
			                ]
			            ],
			            "activityType": 22,
			            "description": "Outdoor fun under the eucalyptus trees for the whole family",
			            "from": 1393142400000,
			            "id": 2,
			            "lat": 37.735681,
			            "lon": -122.492065,
			            "maxSize": 50,
			            "minSize": 2,
			            "placeId": 2,
			            "rsvpTotal": null,
			            "title": "Stern Grove Music Festival",
			            "to": null,
			            "waitListTotal": null
			        }
			    ]
			}
	 
	    */
	  
	  
		var events = Json.obj(
			"activityType" -> activityType,
			"activityCategory" -> activityCategory 
		)
	  
		var temp = Helpers.await(WS.url(TestCommon.serverLocation + "api/v1/events/location/" + locationId + "/" + radius).post(events)).body
		
	  
	} // End of function ttt_Events_getEventsByLocationRadiusUsingLocationId
	  

	
	def ttt_Events_getEventsByLatitudeLongitudeRadiusActivityAndCategory () {
	   // TODO - Not finished yet
	  
	}

	def ttt_Events_associateUserAndvent(userId:Long, eventId:Long): String ={
	
		/*
			curl \
				--header "Content-type: application/json" \
				--request POST \
				--data '{}' \
				localhost:9998/api/v1/events/2/user/1 \
				| python -mjson.tool
		*/	
	  
		var temp = Helpers.await(WS.url(TestCommon.serverLocation + "/api/v1/events/" + eventId.toString + "/user/" + userId.toString).get()).body

		println("\n-------- Associate User and event ------\n" + temp + "\n--------------")
		
		return temp
		
	}  // End of ttt_Events_associateUserAndvent()
	
	
	// =================================================================================
	//               ttt_EventsApi_getAllActivityTypesAndCategories
	def ttt_Events_getAllActivityTypesAndCategories(): (JsValue, Map[Long, String], Map[Long, String]) = {
	   
		  /*
		  curl \
				--header "Content-type: application/json" \
				--request GET \
				--data '{}' \
				localhost:9000/api/v1/activity-types-cats/all \
				| python -mjson.tool
		  }

		  */
	  
 
		var temp:JsValue = Json.obj()
	  	  
		try
		
			temp = Json.parse(Helpers.await(WS.url(TestCommon.serverLocation + "/api/v1/activity-types-cats/all").get()).body)
  
		catch {
        	       	
        	case e: Exception => println("\n\nERROR - Api Get All Activity Types and Categories: " + e +  "\n\n");          
        }
		println("-----------------" + temp)
  
println("**************** ttt_getActivityCategories ******\n")		
		var category:Map[Long, String] = ttt_getActivityCategories(temp)
		
println(category)
		
println("***************** After ttt_getActivityCategories ****\n")		

		var types:Map[Long, String] = ttt_getActivityCategories(temp)
		
		return (temp, category, types)
					
	} // End of ttt_EventsApi_getAllActivityTypesAndCategories
  
	// =================================================================================
	//                           ttt_getActivityCategories
	//
	// This function takes the Json returned from ttt_Events_getAllActivityTypesAndCategories
	// and returns the activity categories as a map.
	//
	def ttt_getActivityCategories (activityTypesJson:JsValue): Map[Long, String] = {
	  
  		var categoriesFromDatabaseMap:Map[Long, String] = Map();
  	  	
  		implicit val categories: Reads[(Long, String)] = (
  		  (__ \ "id").read[Long] and
		  (__ \ "category").read[String] 		  
		).tupled
		
		val cats = (activityTypesJson \ "activityCategories").as[List[(Long, String)]] 				
		cats.foreach({keyVal => categoriesFromDatabaseMap += (keyVal._1 -> keyVal._2)})
				
		return categoriesFromDatabaseMap
	} // End of ttt_getActivityCategories
	
	
	// =================================================================================
	//                           ttt_getActivityTypes
	//
	// This function takes the Json returned from ttt_Events_getAllActivityTypesAndCategories
	// and returns the activity types as a Map.
	//
	def ttt_getActivityTypes (activityTypesJson:JsValue): Map[Long, String] = {
	  
  		var typesFromDatabaseMap:Map[Long, String] = Map();
  	  	
  		implicit val types: Reads[(Long, String)] = (
  		  (__ \ "id").read[Long] and
		  (__ \ "type").read[String] 		  
		).tupled
		
		val cats = (activityTypesJson \ "activityTypes").as[List[(Long, String)]] 				
		cats.foreach({keyVal => typesFromDatabaseMap += (keyVal._1 -> keyVal._2)})
				
		return typesFromDatabaseMap
	} // End of ttt_getActivityCategories
	
	



} // End of object EventsTest