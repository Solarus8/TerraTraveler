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
		 ((event: Event) => Some(event.date, event.locationId, event.description, event.minSize, event.maxSize, event.rsvpTotal, event.waitListTotal))
	)
	
	def allEvents = Action {
	    val events = Event.findAll	    
	    Ok(views.html.protoEvents(events))
	}
	
	def byUser(userId: Long) = Action { implicit request =>
	    val events = Event.findByUserId(userId)
	    Ok(views.html.protoEvents(events))
	}
	
	def allEventsJson = Action {
	    val events = Event.findAll
	    val eventsJson = Json.obj(
	    	"events" -> {
  		    	events.map(event => Json.obj(
	    	  	    "date" 		  -> event.date,
	        	    "locId"    	  -> event.locationId,
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
	
	def event(eventId: Long) = Action { implicit request =>
	    Event.findById(eventId).map { event =>
	        val users = Event.attendies(eventId)
		  	Ok(html.protoEvent(event, users))
	    }.getOrElse(Forbidden)
	}
}




