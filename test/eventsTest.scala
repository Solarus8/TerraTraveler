import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._
import play.api.test.Helpers.await
import play.api.libs.json._
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
	def ttt_Events_createEvent(event: JsObject): String = {	  
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
	  
		var temp = ""
	  	
	    // Web services (WS) is failing on [1,2] in ("activityCategories" : [1,2], "placeId" : 1, ")
	    //      Web services will fail if id [1,2] doesn't have quotes
	    //      Terra Traveler Create Event will fail if [1,2] has quotes
		  
	    //var temp = Helpers.await(WS.url(serverLocation + "/api/v1/events").post(event)).body
	    
		// This a a temporary work around
	    // TODO - Find a better solution
    	//var path = "ttt_testFile.sh"
    	  
    	var path = "test/ttt_testFile.sh"
	
						    
		val writer = new PrintWriter(new File(path))
    		writer.write("""curl --header "Content-type: application/json" --request POST --data '{""" )
    		writer.write(""""from" : "2014-02-23 10:30:00.0",""")
    		writer.write(""""to" : "",""")
    		writer.write(""" "title" : "Outside Lands",""")
    		writer.write(""""activityType" : 22,""")
    		writer.write(""""activityCategories" : [1,2],""")
    		writer.write(""""placeId" : 1,""")
    		writer.write(""""desc" : "Outside Lands: best music festival in S.F.",""")
    		writer.write(""""minSize" : 2, """)
    		writer.write(""""maxSize" : 50, """)
    		writer.write(""""rsvpTot" : "", """)
    		writer.write(""""waitListTot" : """"")
    		writer.write("""}' """ + TestCommon.serverLocation + "/api/v1/events")		    
		writer.close()

		
		 try {
		     var tmp = "sh " + path !!	
		     
		     temp = tmp
		 }
		 catch {
		 	case e: Exception => println("ERROR - Sending Create Event curl commands: " + e);
		 }
	    	    	    
	    //println("============ Events JSON object ============\n" + event + "\n=========== Events after send curl command" + temp + "\n==End of Events =======\n")

	    temp
	} // End of ttt_Events_createEvent
	
	
	def ttt_Events_getEventById(id: String): String = {
		/*
		def ttt_Events_getEventById(eventId: String): String = {
		curl \
		--header "Content-type: application/json" \
		--request GET \
		--data '{}' \
		localhost:9998/api/v1/events/2 \
		| python -mjson.tool
		*/
	  
		var temp = Helpers.await(WS.url(TestCommon.serverLocation + "/api/v1/events/" + id).get()).body
		
		temp
	}
	
	
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
	  
		var temp = Helpers.await(WS.url(TestCommon.serverLocation + "api/v1/events/" + eventId.toString + "/user/" + userId.toString).get()).body

		println("\n-------- Associate User and event ------\n" + temp + "\n--------------")
		
		return temp
		
	}  // End of ttt_Events_associateUserAndvent()
	
	
	

} // End of ttt_Events_createEvent