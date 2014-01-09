package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json._
import play.api.libs.json._

import anorm.NotAssigned

import models._
import views._

object Locations extends Controller { // with Secured {
  
	/*def index = IsAuthenticated { username => _ =>
	    User.findByEmail(username).map { user =>
	      	user.primaryLoc match {
	      	  	case Some(locId) => 
	      	  	  	Ok(html.protoLocation(
      	  	  			Location.single(locId))(user)
  	  			)
	      	  	case _ => 
	      	  	  	Ok(html.protoLocation(None)(user)
  	  	  		)
	      	}
	    }.getOrElse(Forbidden)
	}*/
		
	// TODO: This is an impractical functions and will NOT SCALE ############
	def allLocations = Action {
	    val locations = Location.findAll	    
	    NotFound // TEMP
	}
	
	def location(locId: Long) = Action { implicit request =>
	    NotFound // TEMP
	}
	
	def createPlace = Action { implicit request =>
	    println("Locations.createPlace - TOP")
	    render {
	        case Accepts.Json() => {
	            request.body.asJson.map { json =>
	                
	                println("Locations.createPlace - json: " + json)
	                
			        val name    	= (json \ "name").validate[String]
			        println("Place.createPlace - name: " + name)
			        val desc 	= (json \ "desc").validate[String]
			        println("Place.createPlace - desc: " + desc)
			        val cat 	 	= (json \ "cat").validate[String]
			        println("Place.createPlace - cat: " + cat)
			        val url 	 	= (json \ "url").validate[String]
			        println("Place.createPlace - url: " + url)
			        val latitude	= (json \ "latitude").validate[Double]
			        println("Place.createPlace - latitude: " + latitude)
			        val longitude	= (json \ "longitude").validate[Double]
			        println("Place.createPlace - longitude: " + longitude)
			        
			        val newPlaceLoc	= Location(NotAssigned, null, null, null, null, null, null, 
			                latitude.get, longitude.get, null, null, null)
			        println("Place.createPlace - newPlaceLoc: " + newPlaceLoc)
			        val newLocPK = Location.create(newPlaceLoc)
			        println("Place.createPlace - newLocPK: " + newLocPK)
			        			        
			        val newPlace  = Place(NotAssigned, newLocPK.get, name.get, desc.asOpt, cat.asOpt, url.asOpt)
			        val newPlacePK = Place.create(newPlace)
			        			        
			        val persistedPlace = Place.findById(newPlacePK.get)
			        			        
			        persistedPlace match {
			            case Some(persistedPlace) => {
			                val jsonResp = Json.obj( "place" -> {
			                    Json.obj(
				                    "id"	-> persistedPlace.id.get,
				                    "name"	-> persistedPlace.name,
					    	  	    "locId" -> persistedPlace.locId,
					        	    "desc"  -> persistedPlace.description,
					        	    "cat" 	-> persistedPlace.category,
					        	    "url"	-> persistedPlace.url
					        	)
				            })
				            Ok(jsonResp)
			            }
			            case _ => BadRequest("Place not found")
			        }
				}.getOrElse {
					BadRequest("Expecting Json data")
				}
	        } // End - case Accpts.Json()
	    }
	}
	
	def place(placeId: Long) = Action { implicit request =>
	    val p = Place.findById(placeId) match {
	        case Some(p) => { 
	            p
			    val l = Location.findById(p.locId) match {
			        case Some(l) => { l }
			        case _ => Ok(Json.obj("status" -> "Error: Location not found"))
			    }
	        }
	        case _ => Ok(Json.obj("status" -> "Error: Place not found"))
	    }
	    
	    NotFound // TEMP
	}
}


