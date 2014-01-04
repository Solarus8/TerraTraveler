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

    def createEventJson = Action { implicit request =>
	    request.body.asJson.map { json =>
	        val date 		= (json \ "date").as[Date]
	        val locId    	= (json \ "locId").as[Long]
	        val desc 		= (json \ "desc").as[String]
	        val minSize	 	= (json \ "minSize").as[Int]
	        val maxSize 	= (json \ "maxSize").as[Int]
	        val rsvpTot		= (json \ "rsvpTot").as[Option[Int]]
	        val waitListTot = (json \ "waitListTot").as[Option[Int]]
	        
	        val newEvent  = Event(NotAssigned, date, locId, desc, minSize, maxSize, rsvpTot, waitListTot)
	        Event.create(newEvent)
			
			Redirect(routes.Events.allEvents)
		}.getOrElse {
			BadRequest("Expecting Json data")
		}
	}
    
    def createEvent = Action { implicit request =>
	    val newEvent:Event = eventForm.bindFromRequest.get
	    Event.create(newEvent)
	    Redirect(routes.Events.allEvents)
	}
		
	val eventForm = Form(
		mapping(
			"date"   		-> date,
			"locationId"    -> of[Long],
			"description"  	-> text,
			"minSize"	   	-> number,
			"maxSize" 		-> number,
			"rsvpTotal"		-> optional(of[Int]),
			"waitListTotal"	-> optional(of[Int])
		)((date, locationId, description, minSize, maxSize, rsvpTotal, waitListTotal) => 
		    Event(NotAssigned, date, locationId, description, minSize, maxSize, rsvpTotal, waitListTotal))
		 ((event: Event) => Some(event.date, event.placeId, event.description, event.minSize, event.maxSize, event.rsvpTotal, event.waitListTotal))
	)
	
	def byUser(userId: Long) = Action { implicit request =>
	    val events = Event.findByUserId(userId)
	    render {
	        case Accepts.Json() => {
	            val eventsJson = Json.obj(
	                 "events"	-> {
	                	 events.map(event 	=> Json.obj(
                			 "id"			-> event.id.get,
                			 "date"			-> event.date,
                			 "placeId"		-> event.placeId,
                			 "description"	-> event.description,
                			 "minSize"		-> event.minSize,
                			 "maxSize"		-> event.minSize,
                			 "rsvpTotal"	-> event.rsvpTotal,
                			 "waitListTotal"-> event.waitListTotal
            			 ))
	                 }
	            )
	            Json.toJson(eventsJson)
			    Ok(eventsJson)
	        }
	        
	        case Accepts.Html() => Ok(views.html.protoEvents(events))
	    }	    
	}
	
	def allEvents = Action {
	    val events = Event.findAll	    
	    Ok(views.html.protoEvents(events))
	}
	
	def allEventsJson = Action {
	    val events = Event.findAll
	    val eventsJson = Json.obj(
	    	"events" -> {
  		    	events.map(event  => Json.obj(
	    	  	    "date" 		  -> event.date,
	        	    "locId"    	  -> event.placeId,
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
                			 "date"			-> persistedEvent.date,
                			 "placeId"		-> persistedEvent.placeId,
                			 "description"	-> persistedEvent.description,
                			 "minSize"		-> persistedEvent.minSize,
                			 "maxSize"		-> persistedEvent.minSize,
                			 "rsvpTotal"	-> persistedEvent.rsvpTotal,
                			 "waitListTotal"-> persistedEvent.waitListTotal
					         )
				         )
				         Ok(eventJson)
		            }
				         
		            case Accepts.Html() => Ok(html.protoEvent(persistedEvent))
		        }
		    }
		    
		    case _ => render {
		        case Accepts.Json() => Ok(Json.obj("status" -> "None"))
		        case Accepts.Html() => NotFound
		    }
		}
	}
}




