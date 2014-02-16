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
    		writer.write("""}' """ + serverLocation + "/api/v1/events")		    
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
	  
		var temp = Helpers.await(WS.url(serverLocation + "/api/v1/events/" + id).get()).body
		
		temp
	}
	

} // End of ttt_Events_createEvent