import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._
import play.api.test.Helpers.await
import play.api.libs.json._
import play.api.libs.ws._

object EventsTest {
	val serverLocation = TestCommon.server
	
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
	  	    
	    var temp = Helpers.await(WS.url(serverLocation + "/api/v1/events").post(event)).body
	    
	    println("============ Events JSON object ============\n" + event + "\n=========== Events after send" + temp + "\n=========\n")

	    temp
	}
	
	
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
	  
		var temp = Helpers.await(WS.url(serverLocation + "/api/v1/locations/place/" + id).get()).body
		
		temp
	}
	

} // End of ttt_Events_createEvent