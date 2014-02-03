package controllers

import models._
import views._
import anorm.NotAssigned
import anorm._
import play.api.db._
import play.api.mvc._
import play.api._
import play.api.libs.json.Json._
import play.api.libs.json._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import java.util.{Date}

object Events extends Controller {

    def createEvent = Action { implicit request =>
        println("Events.createEvent - TOP")
	    request.body.asJson.map { json =>
	        val from 		= (json \ "from").validate[Date]
	        		println("Events.createEvent - from: " + from)
	        val to			= (json \ "to").validate[Option[Date]]
	        		println("Events.createEvent - to: " + to)
	        val title		= (json \ "title").validate[Option[String]]
	        		println("Events.createEvent - title: " + title)
	        val activityType		= (json \ "activityType").validate[Int]
	        		println("Events.createEvent - activityType: " + activityType)
	        val activityCategories	= (json \ "activityCategories").validate[List[Int]]
	        		println("Events.createEvent - activityType: " + activityType)
	        val placeId    	= (json \ "placeId").validate[Option[Long]]
	        		println("Events.createEvent - placeId: " + placeId)
	        val desc 		= (json \ "desc").validate[String]
	        		println("Events.createEvent - desc: " + desc)
	        val minSize	 	= (json \ "minSize").validate[Int]
	        		println("Events.createEvent - minSize: " + minSize)
	        val maxSize 	= (json \ "maxSize").validate[Int]
	        		println("Events.createEvent - maxSize: " + maxSize)
	        val rsvpTot		= (json \ "rsvpTot").validate[Option[Int]]
	        		println("Events.createEvent - rsvpTot:" + rsvpTot)
	        val waitListTot = (json \ "waitListTot").validate[Option[Int]]
	        		println("Events.createEvent - waitListTot: " + waitListTot)
	        
	        val newEvent  = Event(NotAssigned, from.get, to.get, title.get, activityType.get, placeId.get, 
	                desc.get, minSize.get, maxSize.get, rsvpTot.get, waitListTot.get)
	                println("Events.createEvent - newEvent: " + newEvent)
	        val eventPK = Event.create(newEvent)
	        	println("Events.createEvent - eventPK: " + eventPK)
	        
	        val assocEventCatPKs = Event.associateEventCategory(eventPK.get, activityCategories.get)
	        
	        val persistedEvent = Event.byId(eventPK.get)
	        	println("Events.createEvent - persistedEvent: " + persistedEvent)
	        
	        persistedEvent match {
	            case Some(persistedEvent) => {
	                val jsonResp = Json.obj( "event" -> eventToJson(persistedEvent))
		            Ok(jsonResp)
	            }
	            case None => BadRequest("User not found")
	        }
		}.getOrElse {
			BadRequest("Expecting Json data")
		}
	} // end - createEvent
		
	val eventForm = Form(
		mapping(
			"from"   		-> date,
			"to"			-> optional(date),
			"title"			-> optional(text),
			"activityType"	-> number,
			"placeId"    	-> optional(of[Long]),
			"description"  	-> text,
			"minSize"	   	-> number,
			"maxSize" 		-> number,
			"rsvpTotal"		-> optional(of[Int]),
			"waitListTotal"	-> optional(of[Int])
		)((from, to, title, activityType, placeId, description, minSize, maxSize, rsvpTotal, waitListTotal) => 
		    Event(NotAssigned, from, to, title, activityType, placeId, description, minSize, maxSize, rsvpTotal, waitListTotal))
		 ((event: Event) => Some(event.from, event.to, event.title, event.activityType, event.placeId, event.description, event.minSize, event.maxSize, event.rsvpTotal, event.waitListTotal))
	)
	
	def byUserId(userId: Long) = Action { 
	    val events = Event.byUserId(userId)
        val eventsJson = Json.obj(
             "events"	-> {
            	 events.map(event => eventToJson(event))
             }
        )
        Json.toJson(eventsJson)
	    Ok(eventsJson)
	}
	
	def byLocationRadius(locId: Long, radius: Int) = Action {
	    val events = Event.byLocationRadius(locId, radius)
	    val eventsJson = Json.obj(
             "events"	-> {
            	 events.map(event 	=> eventToJson(event))
             }
        )
        Json.toJson(eventsJson)
	    Ok(eventsJson)
	}
	
	def eventToJson(event: Event): JsObject = {
		 val latlon:(Double, Double) = event.placeId match {
		     case Some(placeId) => {
		         println("Events.eventToJson - inside Some(pId) - placeId: " + placeId)
		         Place.byId(placeId) match {
		             case Some(place) => {
		                 println("Events.eventToJson - inside case Some(place) - place.locId: " + place.locId)
			             Location.byId(place.locId) match {
			                 case Some(loc) => {
			                     println("Events.eventToJson - inside Some(loc) - loc: " + loc)
			                     (loc.lat, loc.lon)
			                 }
			                 case None => {
			                     println("Events.eventToJson - Location.findById(pId) match = None/null")
			                     null // TODO: Not a good practice!
			                 }
			             }
		             }
		             case None => {
		                 println("Events.eventToJson - Place.findById(pId) match = None/null")
		                 null
		             }
		         }
		     }
		     case None => {
		         println("Events.eventToJson - Some(pId) match = None/null")
		         null // TODO: Not a good practice!
		     }
		 } // end - latlon
		 
		 println("Events.eventToJson - latlon: " + latlon)
		 
		 val actCats = ActivityCategory.findAssocActivityCatByEventId(event.id.get)
		
		 Json.obj(
			 "id"					-> event.id.get,
			 "from"					-> event.from,
			 "to"					-> event.to,
			 "title"				-> event.title,
			 "activityType"			-> event.activityType,
			 "activityCategories"	-> Json.arr(actCats.map{ cat => cat.id.get }),
			 "placeId"				-> event.placeId,
			 "lat"					-> latlon._1,
			 "lon"					-> latlon._2,
			 "description"			-> event.description,
			 "minSize"				-> event.minSize,
			 "maxSize"				-> event.maxSize,
			 "rsvpTotal"			-> event.rsvpTotal,
			 "waitListTotal"		-> event.waitListTotal
		 ) // end - Json.obj for each event
	} // end - eventToJson
	
	// TODO: Factor out common code
	// TODO: Remove printlns
	def byLatLonRadius(lat: Double, lon: Double, radius: Int) = Action {
	    println("Events.byRadiusLatLon - TOP - lat: " + lat + " | lon: " + lon + " | radius: " + radius)
	    val events = Event.byLatLonRadius(lat, lon, radius)
	    val eventsJson = Json.obj(
             "events" -> {
            	 events.map(event => eventToJson(event))
             }
        )
        println("Events.byRadiusLatLon - BOTTOM - eventsJson: " + eventsJson)
        Json.toJson(eventsJson)
	    Ok(eventsJson)
	} // end - byRadiusLatLon
	
	// TODO: This should go away. Will not scale...
	def allEvents = Action {
	    val events = Event.all
	    val eventsJson = Json.obj(
	    	"events" -> {
  		    	events.map(event  => eventToJson(event))
	    	}
	    )
	    
	    Json.toJson(eventsJson)
	    Ok(eventsJson)
	}
	
	def byId(id: Long) = Action {
		Event.byId(id) match { 
		    case Some(persistedEvent) => {
		        val eventJson = Json.obj("event" -> eventToJson(persistedEvent))
		        Ok(eventJson)
		    }
		    case _ =>  Ok(Json.obj("status" -> "None"))
		}
	}
	
	/**
	 * Makes sure that User and Event both exist first.
	 */
	// TODO: NEEDS AUTHENTICATION AND AUTHORIZATION
	def associateEventUser(eventId: Long, userId: Long) = Action {
	    User.byId(userId) match {
	        case Some(persistedUser) => {
	            Event.byId(eventId) match {
	                case Some(persistedEvent) => {
	                    val userEventPK = Event.associateEventUser(eventId, userId)
	                    Ok(
                    		Json.obj(
                		        "status" 		-> "success", 
                		        "user_event-PK" -> userEventPK.get,
                		        "event" 		-> eventToJson(persistedEvent),
                		        "userId"		-> userId
                		    )
	                    ) // end - Ok result
	                }
	                case None => NotFound(Json.obj("status" -> "Event not found.")) // TODO: Change all Ok(status -> None) to NotFound(status -> Not found)
	            }
	        }
	        case None => NotFound(Json.obj("status" -> "User not found.")) // TODO: Change all Ok(status -> None) to NotFound(status -> Not found)
	    }
	}
	
	// val loc = Location.byId(user.primaryLoc)
	def usersByEventId(eventId: Long) = Action {
	    val attendies = Event.attendies(eventId)
	    val usersJson = Json.obj(
	        "users"	-> {
	            attendies.map( user => {
	                val latlon: (Double, Double) = Location.byId(user.primaryLoc) match {
	                    case Some(loc) => (loc.lat, loc.lon)
	                    case None => (0,0)
	                }
	                Json.obj(
	    			    "id"			-> user.id.get,
		    	  	    "userName"		-> user.userName,
		        	    "email"			-> user.email,
		        	    "password"		-> user.password,
		        	    "role"			-> user.role,
		        	    "primaryLoc"   	-> user.primaryLoc,
		        	    "lat"   		-> latlon._1,
		        	    "lon"			-> latlon._2
	    			)
	            })
	        }
	    ) // end - usersJson

		Json.toJson(usersJson)
		Ok(usersJson)
	}
	
	def allActivityTypesAndCats() = Action {
	    println("Events.allActivityTypesAndCats - TOP")
	    val activityTypes = ActivityType.all
	    val activityCategories = ActivityCategory.allActivityCategories
	    
	    val activityTypesAndCatsJson = Json.obj( 
	        "activityTypes"   -> activityTypes.map{ 
	            actype 		  => Json.obj(
            		"id"	  -> actype.id.get,
            		"activity"-> actype.activity
        		)
		    }, // end - activityTypes.map
	    
	        "activityCategories" -> activityCategories.map{ 
	            actcat => Json.obj(
            		"id" -> actcat.id.get,
            		"category" -> actcat.category
        		)
		    } // end - activityCategories.map
	    ) // end - activityTypesAndCatsJson
	    
	    Json.toJson(activityTypesAndCatsJson)
	    //println("Events.allActivityTypesAndCats - activityTypesJson: " + activityTypesJson)
		Ok(activityTypesAndCatsJson)
	}
}




