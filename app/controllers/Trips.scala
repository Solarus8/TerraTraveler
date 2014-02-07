package controllers

import java.util.Date

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

object Trips extends Controller {
    
	def createTrip = Action { implicit request =>
	    request.body.asJson.map { json =>
	        println("Trips.createTrip - json: " + json)
	        
	        val name 		= (json \ "name").validate[String]
	        		println("Trips.createTrip - name: " + name)
	        val desc 		= (json \ "desc").validate[String]
	        		println("Trips.createTrip - desc: " + desc)
	        val userId 		= (json \ "userId").validate[Long]
	        		println("Trips.createTrip - userId: " + userId)
	        val dateFrom 	= (json \ "dateFrom").validate[Date]
	        		println("Trips.createTrip - dateFrom: " + dateFrom)
	        val dateTo 		= (json \ "dateTo").validate[Date]
	        		println("Trips.createTrip - dateTo: " + dateTo)
	        val status 		= (json \ "status").validate[String]
	        		println("Trips.createTrip - status: " + status)
	        
	        val newTrip  = Trip(NotAssigned, name.get, desc.asOpt, userId.get, dateFrom.asOpt, dateTo.asOpt, status.asOpt)
	        val newTripPK = Trip.create(newTrip)
	        			        
	        val persistedTrip = Trip.byId(newTripPK.get)
	        
	        persistedTrip match {
	            case Some(persistedTrip) => {
	                val jsonResp = Json.obj( "trip" -> tripToJson(persistedTrip))
		            Ok(jsonResp)
	            }
	            case None => BadRequest("User not found")
	        }
	    }.getOrElse {
			BadRequest("Expecting Json data")
		}
	}
	
	def byId(id: Long) = Action { implicit request =>
	    Trip.byId(id) match { 
	        case Some(trip) => {
	             val tripJson = Json.obj(
	                 "trip" -> tripToJson(trip)   
	             )
	             Ok(tripJson)
	        }
	        case None => Ok(Json.obj("status" -> "None"))
	    }
	}
	
	def tripToJson(trip: Trip): JsObject = {
	    
	    Json.obj(
			"id"		-> trip.id.get,
			"name"   	-> trip.name,
			"desc"      -> trip.desc,
			"userId" 	-> trip.userId,
			"dateFrom"	-> trip.dateFrom,
			"dateTo"	-> trip.dateTo,
			"status"	-> trip.status
		)
	}
}




