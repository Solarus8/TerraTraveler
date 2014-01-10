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
	        
	        val newEvent  = Event(NotAssigned, from.get, to.get, placeId.asOpt, 
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
			"placeId"    	-> optional(of[Long]),
			"description"  	-> text,
			"minSize"	   	-> number,
			"maxSize" 		-> number,
			"rsvpTotal"		-> optional(of[Int]),
			"waitListTotal"	-> optional(of[Int])
		)((from, to, placeId, description, minSize, maxSize, rsvpTotal, waitListTotal) => 
		    Event(NotAssigned, from, to, placeId, description, minSize, maxSize, rsvpTotal, waitListTotal))
		 ((event: Event) => Some(event.from, event.to, event.placeId, event.description, event.minSize, event.maxSize, event.rsvpTotal, event.waitListTotal))
	)
	
	def byUser(userId: Long) = Action { 
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
	
	def byRadius(locId: Long, radius: Int) = Action {
	    val events = Event.byRadius(locId, radius)
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
	
	def event(id: Long) = Action { implicit request =>
		Event.findById(id) match { 
		    case Some(persistedEvent) => {
		        render {
		            case Accepts.Json() => {
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
		        }
		    }
		    
		    case _ => render {
		        case Accepts.Json() => Ok(Json.obj("status" -> "None"))
		    }
		}
	}
}




