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
	        val placeId    	= (json \ "placeId").validate[Long]
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
	        
	        val newEvent  = Event(NotAssigned, from.get, to.get, title.get, activityType.get, placeId.asOpt, 
	                desc.get, minSize.get, maxSize.get, rsvpTot.get, waitListTot.get)
	                println("Events.createEvent - newEvent: " + newEvent)
	        val eventPK = Event.create(newEvent)
	        	println("Events.createEvent - eventPK: " + eventPK)
	        val persistedEvent = Event.findById(eventPK.get)
	        	println("Events.createEvent - persistedEvent: " + persistedEvent)
	        
	        persistedEvent match {
	            case Some(persistedEvent) => {
	                val jsonResp = Json.obj( "event" -> {
	                    Json.obj(
		                    "id"			-> persistedEvent.id.get,
                			"from"			-> persistedEvent.from,
                			"to"			-> persistedEvent.to,
                			"placeId"		-> persistedEvent.placeId,
                			"description"	-> persistedEvent.description,
                			"minSize"		-> persistedEvent.minSize,
                			"maxSize"		-> persistedEvent.maxSize,
                			"rsvpTotal"		-> persistedEvent.rsvpTotal,
                			"waitListTotal" -> persistedEvent.waitListTotal
			        	)
		            })
		            Ok(jsonResp)
	            }
	            case None => BadRequest("User not found")
	        }
		}.getOrElse {
			BadRequest("Expecting Json data")
		}
	}
		
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
	    val events = Event.findByUserId(userId)
        val eventsJson = Json.obj(
             "events"	-> {
            	 events.map(event 	=> Json.obj(
        			 "id"			-> event.id.get,
        			 "from"			-> event.from,
        			 "to"			-> event.to,
        			 "placeId"		-> event.placeId,
        			 "description"	-> event.description,
        			 "minSize"		-> event.minSize,
        			 "maxSize"		-> event.maxSize,
        			 "rsvpTotal"	-> event.rsvpTotal,
        			 "waitListTotal"-> event.waitListTotal
    			 ))
             }
        )
        Json.toJson(eventsJson)
	    Ok(eventsJson)
	}
	
	def byLocationRadius(locId: Long, radius: Int) = Action {
	    val events = Event.byLocationRadius(locId, radius)
	    val eventsJson = Json.obj(
             "events"	-> {
            	 events.map(event 	=> Json.obj(
        			 "id"			-> event.id.get,
        			 "from"			-> event.from,
        			 "to"			-> event.to,
        			 "placeId"		-> event.placeId,
        			 "description"	-> event.description,
        			 "minSize"		-> event.minSize,
        			 "maxSize"		-> event.maxSize,
        			 "rsvpTotal"	-> event.rsvpTotal,
        			 "waitListTotal"-> event.waitListTotal
    			 ))
             }
        )
        Json.toJson(eventsJson)
	    Ok(eventsJson)
	}
	
	// TODO: Factor out common code
	// TODO: Remove printlns
	def byLatLonRadius(lat: Double, lon: Double, radius: Int) = Action {
	    println("Events.byRadiusLatLon - TOP - lat: " + lat + " | lon: " + lon + " | radius: " + radius)
	    val events = Event.byLatLonRadius(lat, lon, radius)
	    val eventsJson = Json.obj(
             "events" -> {
            	 events.map(event => {
            	     val latlon:(Double, Double) = event.placeId match {
            	         case Some(pId) => {
            	             println("Events.byRadiusLatLon - inside Some(pId) - pId: " + pId)
            	             Place.byId(pId) match {
            	                 case Some(place) => {
            	                     println("Events.byRadiusLatLon - inside case Some(place) - place.locId: " + place.locId)
		            	             Location.byId(place.locId) match {
		            	                 case Some(loc) => {
		            	                     println("Events.byRadiusLatLon - inside Some(loc) - loc: " + loc)
		            	                     (loc.lat, loc.lon)
		            	                 }
		            	                 case None => {
		            	                     println("Events.byRadiusLatLon - Location.findById(pId) match = None/null")
		            	                     null // TODO: Not a good practice!
		            	                 }
		            	             }
            	                 }
            	                 case None => {
            	                     println("Events.byRadiusLatLon - Place.findById(pId) match = None/null")
            	                     null
            	                 }
            	             }
            	         }
            	         case None => {
            	             println("Events.byRadiusLatLon - Some(pId) match = None/null")
            	             null // TODO: Not a good practice!
            	         }
            	     } // end - latlon
            	     
            	     println("Events.byRadiusLatLon - latlon: " + latlon)

            	     Json.obj(
	        			 "id"			-> event.id.get,
	        			 "from"			-> event.from,
	        			 "to"			-> event.to,
	        			 "title"		-> event.title,
	        			 "activityType"	-> event.activityType,
	        			 "placeId"		-> event.placeId,
	        			 "lat"			-> latlon._1,
	        			 "lon"			-> latlon._2,
	        			 "description"	-> event.description,
	        			 "minSize"		-> event.minSize,
	        			 "maxSize"		-> event.maxSize,
	        			 "rsvpTotal"	-> event.rsvpTotal,
	        			 "waitListTotal"-> event.waitListTotal
	    			 ) // end - Json.obj for each event
             	 } // end - event =>
    			 ) // end - events.map
             } // end - "events" ->
        ) // end - eventsJson
        println("Events.byRadiusLatLon - BOTTOM - eventsJson: " + eventsJson)
        Json.toJson(eventsJson)
	    Ok(eventsJson)
	} // end - byRadiusLatLon
	
	// TODO: This should go away. Will not scale...
	def allEvents = Action {
	    val events = Event.findAll	    
	    NotFound // TEMP
	}
	
	// TODO: This should go away. Will not scale...
	def allEventsJson = Action {
	    val events = Event.findAll
	    val eventsJson = Json.obj(
	    	"events" -> {
  		    	events.map(event  => Json.obj(
	    	  	    "from" 		  -> event.from,
	    	  	    "to"		  -> event.to,
	        	    "placeId"     -> event.placeId,
	        	    "desc"		  -> event.description,
	        	    "minSize"	  -> event.minSize,
	        	    "maxSize" 	  -> event.maxSize,
	        	    "rsvpTot" 	  -> event.rsvpTotal,
	        	    "waitListTot" -> event.waitListTotal
  		    ))}
	    )
	    
	    Json.toJson(eventsJson)
	    Ok(eventsJson)
	}
	
	def byId(id: Long) = Action { implicit request =>
		Event.findById(id) match { 
		    case Some(persistedEvent) => {
		        val eventJson = Json.obj(
			         "event" -> Json.obj(
			         "id"			-> persistedEvent.id.get,
        			 "from"			-> persistedEvent.from,
        			 "to"			-> persistedEvent.to,
        			 "placeId"		-> persistedEvent.placeId,
        			 "description"	-> persistedEvent.description,
        			 "minSize"		-> persistedEvent.minSize,
        			 "maxSize"		-> persistedEvent.maxSize,
        			 "rsvpTotal"	-> persistedEvent.rsvpTotal,
        			 "waitListTotal"-> persistedEvent.waitListTotal
			         )
		         )
		         Ok(eventJson)
		    }
		    case _ =>  Ok(Json.obj("status" -> "None"))
		}
	}
}




