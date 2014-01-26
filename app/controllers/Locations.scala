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
		
	// TODO: This will NOT SCALE ############
	def allLocations = Action {
	    val locations = Location.findAll	    
	    NotFound // TEMP
	}
	
	// TODO: implement
	def byId(locId: Long) = Action { implicit request =>
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
			        val url 	 	= (json \ "url").validate[Option[String]]
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
			        			        
			        val newPlace  = Place(NotAssigned, newLocPK.get, name.get, desc.asOpt, cat.asOpt, url.get)
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
	} // end - def createPlace
	
	def placesByLatLonRadius(lat: Double, lon: Double, radius: Int) = Action {
	    println("Locations.placeByLatLonRadius - TOP - lat: " + lat + " | lon: " + lon + " | radius: " + radius)
	    val places = Place.byLatLonRadius(lat, lon, radius)
	    val placesJson = Json.obj(
             "places" -> {
            	 places.map(persistedPlace => {
            	     Location.byId(persistedPlace.locId) match {
    	                 case Some(loc) => {
    	                     println("Locations.placeByLatLonRadius - inside Some(loc) - loc: " + loc)
    	                     Json.obj(
		                     	 "id"		-> persistedPlace.id.get,
				                 "name"		-> persistedPlace.name,
				                 "lat"		-> loc.lat,
				                 "lon"		-> loc.lon,
					    	  	 "locId" 	-> persistedPlace.locId,
					        	 "desc"  	-> persistedPlace.description,
					        	 "cat" 		-> persistedPlace.category,
					        	 "url"		-> persistedPlace.url
					         )
    	                 }
    	                 case None => {
    	                     println("Locations.placeByLatLonRadius - Location.byId(pId) match = None/null")
    	                     Json.obj() // TODO: I would rather just continue than return an empty json object
    	                 }
    	             } // end - Location.byId(place.locId)
            	 }) // - end - places.map(persistedPlace)
             } // end - places
         ) // end - placesJson
         println("Locations.byRadiusLatLon - BOTTOM - eventsJson: " + placesJson)
         Json.toJson(placesJson)
         Ok(placesJson)
	} // end - def placeByLatLonRadius
	
	def placeById(placeId: Long) = Action { implicit request =>
	    Place.findById(placeId) match {
             case Some(persistedPlace) => {
                 println("Locations.place - inside Some(place) - place.locId: " + persistedPlace.locId)
                 
	             Location.byId(persistedPlace.locId) match {
                     case None => {
	                     println("Events.byRadiusLatLon - Location.findById(pId) match = None/null")
	                     
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
				         println("Locations.place - None(loc) - jsonRespLatLon: " + jsonResp)
				         Ok(jsonResp) // Returning just the Place with no lon/lat
	                 }
	                 case Some(loc) => {
	                     println("Locations.place - inside Some(loc) - loc: " + loc)
                     	 val jsonRespLatLon = Json.obj( "place" -> {
                     	     Json.obj(
		                     	 "id"		-> persistedPlace.id.get,
				                 "name"		-> persistedPlace.name,
				                 "lat"		-> loc.lat,
				                 "lon"		-> loc.lon,
					    	  	 "locId" 	-> persistedPlace.locId,
					        	 "desc"  	-> persistedPlace.description,
					        	 "cat" 		-> persistedPlace.category,
					        	 "url"		-> persistedPlace.url
					         )
                     	 })
			             println("Locations.place - Some(loc) - jsonRespLatLon: " + jsonRespLatLon)
				         Ok(jsonRespLatLon)
	                 }
	             }
             } // end - Some(persistedPlace)
             case None => {
                 println("Events.byRadiusLatLon - Place.findById(pId) match = None/null")
                 Ok(Json.obj("status" -> "None"))
             }
         } // end - Place.findById(placeId)
	} // end - def place
} // end - object Locations


