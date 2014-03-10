
import play.api.libs.json._

trait EventsApiTests extends org.specs2.mutable.Specification {
  
    	// =================================================================================
  	//                               ttt_EventsApiTest_createEvent 
  	// 
 	//  This test depends on "Create Place" API working correctly.
 	//
  	def ttt_EventsApiTest_createEvent() {
 	 	  
  	  	// Cuesta Park in Mountain View
		var place = Json.obj (
		    "name" -> "Cuesta Park", 
			"desc" -> "Mountin View city park", 
			"cat" -> "PARK", 					
			"url" -> "http->//www.cuestapark.org/", 
			"latitude" -> 37.371897, 
			"longitude" -> -122.080207 
		)
  	  
		// Event details
  	  	var from = "2014-02-23 10:30:00.0"
		var to   = "2014-03-23 11:30:00.0" 
		var title = "Outside Lands"
		var activityType = 22		
		var activityCategories: List[Long] = List(1,2)
		var desc = "Outside Lands: best music festival in S.F."
		var minSize:Long = 2
		var maxSize:Long = 50
		var rsvpTot:Long = 6
		var waitListTot:Long = 3
		
		// Create a new place since place Id is needed to create and event
		var(placeId:Long, newPlace:JsValue) = LocationsApi.ttt_Places_CreatePlace(place, 0)
		
		
		  	  
	 	var event = Json.obj(
			"from" -> from, 
			"to" -> to, 
			"title" -> title, 
		    "activityType" -> activityType, 
		    "activityCategories" -> activityCategories, 
		    "placeId" -> placeId,  // Place Id from the create new place command above
		    "desc" -> desc, 
		    "minSize" -> minSize, 
		    "maxSize" -> maxSize, 
		    "rsvpTot" -> rsvpTot, 
		    "waitListTot" -> waitListTot
		)
		
		// Create a new event
		var (eventId:Long, newEvent:JsValue) = EventsApi.ttt_Events_createEvent(event)

		
		"ttt_EventsApiTest_createEvent - Create Event, " + title + ", id= " + eventId in { 
		
			// TODO - convert time format (from: to to:)		  
			title               must beEqualTo((newEvent \ "event" \ "title").as[String])
			activityType        must beEqualTo((newEvent \ "event" \ "activityType").as[Long])			
// TODO -List comparison	[1,2]		
//	  	  	activityCategories  must beEqualTo((newEvent \ "event" \ "activityType").as[List[Long]])
			placeId             must beEqualTo((newEvent \ "event" \ "placeId").as[Long])
			desc                must beEqualTo((newEvent \ "event" \ "description").as[String])
			minSize             must beEqualTo((newEvent \ "event" \ "minSize").as[Long])
			maxSize             must beEqualTo((newEvent \ "event" \ "maxSize").as[Long])
			rsvpTot             must beEqualTo((newEvent \ "event" \ "rsvpTotal").as[Long])
			waitListTot         must beEqualTo((newEvent \ "event" \ "waitListTotal").as[Long])
			
			1 must beEqualTo( 1)

  	  	} // End of matchers

 	
 	}  // End of ttt_EventsApiTest_createEvent	
  	
  	
    // =================================================================================
  	//                    ttt_EventsApiTest_getEventById
  	//
  	//  This test depends on "Create Place" API working correctly since it
  	//  uses a place Id.
  	//
  	//
  	def ttt_EventsApiTest_getEventById() {
 
  	    // Place Shoreline Lake Aquatic Center
		var place = Json.obj (
		    "name" -> "Shoreline Lake Aquatic Center", 
			"desc" -> "Biking, hiking, paddle boats, wind sailing", 
			"cat" -> "PARK", 					
			"url" -> "http->//www.shoreline.org/", 
			"latitude" -> 37.432778, 
			"longitude" -> -122.088122 
		)
		
		// Create a new place since place Id is needed to create and event
		var(placeId:Long, newPlace:JsValue) = LocationsApi.ttt_Places_CreatePlace(place, 0)
		
		var from = "2014-02-23 10:30:00.0"
		var to   = "2014-03-23 11:30:00.0" 
		var title = "Hike at Shoreline Park"
		var activityType = 22		
		var activityCategories: List[Long] = List(1,2)
		var desc = "We will meet at the Shoreline Lake Aquatic Center"
		var minSize:Long = 5
		var maxSize:Long = 25
		var rsvpTot:Long = 18
		var waitListTot:Long = 98
		
		var event = Json.obj(
			"from" -> from, 
			"to" -> to, 
			"title" -> title, 
		    "activityType" -> activityType, 
		    "activityCategories" -> activityCategories, 
		    "placeId" -> placeId,  // Place Id from the create new place command above
		    "desc" -> desc, 
		    "minSize" -> minSize, 
		    "maxSize" -> maxSize, 
		    "rsvpTot" -> rsvpTot, 
		    "waitListTot" -> waitListTot
		)
		
		// Create a new event
		var (eventId:Long, newEvent:JsValue) = EventsApi.ttt_Events_createEvent(event)
		
		// Get an event using the event Id
		var eventFromId:JsValue = EventsApi.ttt_Events_getEventById(eventId)
  	  
		
		"ttt_EventsApiTest_getEventById - Get Event By Id, " + title + ", id= " + eventId in { 
		
			// TODO - convert time format (from: to to:)		  
			title               must beEqualTo((eventFromId \ "event" \ "title").as[String])
			activityType        must beEqualTo((eventFromId \ "event" \ "activityType").as[Long])			
// TODO -List comparison	[1,2]		
//	  	  	activityCategories  must beEqualTo((eventFromId \ "event" \ "activityType").as[List])
			placeId             must beEqualTo((eventFromId \ "event" \ "placeId").as[Long])
			desc                must beEqualTo((eventFromId \ "event" \ "description").as[String])
			minSize             must beEqualTo((eventFromId \ "event" \ "minSize").as[Long])
			maxSize             must beEqualTo((eventFromId \ "event" \ "maxSize").as[Long])
			rsvpTot             must beEqualTo((eventFromId \ "event" \ "rsvpTotal").as[Long])
			waitListTot         must beEqualTo((eventFromId \ "event" \ "waitListTotal").as[Long])
			


  	  	} // End of matchers
  	  
  	  
  	} // End of ttt_EventsApiTest_getEventById
  	
  
    	// =================================================================================
  	//                           ttt_EventsApiTest_getAllActivityTypesAndCategories
  	// 
  	def ttt_EventsApiTest_getAllActivityTypesAndCategories() { 
  	   
  		/*			
			example response
			{
			    "activityCategories": [
				        {
				            "category": "Fitness",
				            "id": 1
				        },
			        
			   		],
			    "activityTypes": [
			        {
			            "activity": "Running",
			            "id": 1
  		 */
  	  
  	  
  	  
  		// Get activity types from database
  		var (results:JsValue, categoriesFromDatabase, typesFromDatabase) = EventsApi.ttt_Events_getAllActivityTypesAndCategories

  		// Get hard coded activity types
  		var activityType = TestCommon.activityType
  		var activityCategory = TestCommon.activityCategories
  		
  		"ttt_EventsApiTest_getAllActivityTypesAndCategories - Activity Categories" in {
   
	 		var oneCategoryHc:String = "" 
	 		var oneCategoryFromDatabase:String = ""
	 		var passFail:Boolean = true
		  
	 		// Compare activities from database with hard code activities
	  		activityCategory.keys.foreach{ id =>  	
	  		    oneCategoryHc = activityCategory(id)
	  		    
		        if (categoriesFromDatabase.isDefinedAt(id.toLong)) {
	              oneCategoryFromDatabase = categoriesFromDatabase(id.toLong)
		            } else {
	              
	            	passFail = false  // Activity Category missing from database              
	            }	  		    
	  	  		oneCategoryHc must beEqualTo(oneCategoryFromDatabase)	              	    
	  		}
	 		
	 		// Number of hard coded activity Categories must match number in database
	 		activityCategory.size must beEqualTo(categoriesFromDatabase.size)	 		
	 		passFail must beEqualTo(true)
	 		
  		} // End of matchers for Category Types
 
	  		
  		"ttt_EventsApiTest_getAllActivityTypesAndCategories - Activity Types" in {
   
	 		var oneTypeHc:String = "" 
	 		var oneTypeFromDatabase:String = ""
	 		var passFail:Boolean = true
		  
	 		// Compare activities from database with hard code activities
	  		activityType.keys.foreach{ id =>  	
	  		    oneTypeHc = activityType(id)
	  		    
		        if (typesFromDatabase.isDefinedAt(id.toLong)) {
	              oneTypeFromDatabase = typesFromDatabase(id.toLong)
		            } else {
	              
	            	passFail = false  // Activity type missing from database              
	            }	  		    
	  	  		oneTypeHc must beEqualTo(oneTypeFromDatabase)	              	    
	  		}
	 		
	 		// Number of hard coded activity types must match number in database
	 		activityType.size must beEqualTo(typesFromDatabase.size)	 		
	 		passFail must beEqualTo(true)
	 		
  		} // End of matchers for Activity Types
	  		

  	} // End of ttt_EventsApiTest_getAllActivityTypesAndCategories()
  
  
} // End of trait UsersApiTests
