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
		
	def createPlaceSynd = Action { implicit request =>
	    println("Locations.createPlaceSyn - TOP")
        request.body.asJson.map { json =>
            println("Locations.createPlaceSyn - json: " + json)
            
            val placeId = (json \ "placeId").validate[Option[Long]]
            		println("Locations.createPlaceSynd - placeId: " + placeId)
            val thirdPartyId = (json \ "thirdPartyId").validate[Long]
            		println("Locations.createPlaceSynd - thirdPartyId: " + thirdPartyId)
            val thirdPartyPlaceId = (json \ "thirdPartyPlaceId").validate[String]
            		println("Locations.createPlaceSynd - thirdPartyPlaceId: " + thirdPartyPlaceId)
            val thirdPartyRef = (json \ "thirdPartyRef").validate[Option[String]]
            		println("Locations.createPlaceSynd - thirdPartyRef: " + thirdPartyRef)
            
            val newPlaceThirdParty  = PlaceThirdParty(NotAssigned, placeId.get, thirdPartyId.get, thirdPartyPlaceId.get, thirdPartyRef.get)
	        val newPlaceThirdPartyPK = PlaceThirdParty.create(newPlaceThirdParty)
	        	println("Locations.createPlaceSynd - newPlaceThirdPartyPK: " + newPlaceThirdPartyPK)
	        
	        val persistedPlaceThirdParty = PlaceThirdParty.byId(newPlaceThirdPartyPK.get)
	        	println("Locations.createPlaceSynd - persistedPlaceThirdParty: " + persistedPlaceThirdParty)
	        
	        persistedPlaceThirdParty match {
	            case Some(persistedPlaceThirdParty) => {
	                val jsonResp = Json.obj( "placeThirdParty" -> {
	                    Json.obj(
		                    "id"				-> persistedPlaceThirdParty.id.get,
		                    "placeId"			-> persistedPlaceThirdParty.placeId,
			    	  	    "thirdPartyID" 		-> persistedPlaceThirdParty.thirdPartyID,
			        	    "thirdPartyPlaceId" -> persistedPlaceThirdParty.thirdPartyPlaceId,
			        	    "thirdPartyRef" 	-> persistedPlaceThirdParty.thirdPartyRef
			        	)
		            })
		            Ok(jsonResp)
	            }
	            case _ => BadRequest("Place not found")
	        }
	    }.getOrElse {
			BadRequest("Locations.createPlaceSynd - Expecting Json data")
		}
	}
	
	def createPlace = Action { implicit request =>
	    println("Locations.createPlace - TOP")
        request.body.asJson.map { json =>
            println("Locations.createPlace - json: " + json)
            
	        val name = (json \ "name").validate[String]
	        		println("Locations.createPlace - name: " + name)
	        val desc = (json \ "desc").validate[String]
	        		println("Locations.createPlace - desc: " + desc)
	        val cat = (json \ "cat").validate[String]
	        		println("Locations.createPlace - cat: " + cat)
	        val url = (json \ "url").validate[Option[String]]
	        		println("Locations.createPlace - url: " + url)
	        val latitude = (json \ "latitude").validate[Double]
	        		println("Locations.createPlace - latitude: " + latitude)
	        val longitude = (json \ "longitude").validate[Double]
	        		println("Locations.createPlace - longitude: " + longitude)
	        
	        val newPlaceLoc	= Location(NotAssigned, null, null, null, null, null, null, 
	                latitude.get, longitude.get, null, null, null)
	                println("Locations.createPlace - newPlaceLoc: " + newPlaceLoc)
	        val newLocPK = Location.create(newPlaceLoc)
	        	println("Locations.createPlace - newLocPK: " + newLocPK)
	        			        
	        val newPlace  = Place(NotAssigned, newLocPK.get, name.get, desc.asOpt, cat.asOpt, url.get)
	        val newPlacePK = Place.create(newPlace)
	        			        
	        val persistedPlace = Place.byId(newPlacePK.get)
	        			        
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
			BadRequest("Locations.createPlace - Expecting Json data")
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
	
	def placeThirdPartyById(placeThirdPartyId: Long) = Action {
	    val persistedPlaceThirdParty = PlaceThirdParty.byId(placeThirdPartyId)
	        	println("Locations.placeThirdPartyById - persistedPlaceThirdParty: " + persistedPlaceThirdParty)
	    
	    persistedPlaceThirdParty match {
            case Some(persistedPlaceThirdParty) => {
                val jsonResp = Json.obj( "placeThirdParty" -> {
                    Json.obj(
	                    "id"				-> persistedPlaceThirdParty.id.get,
	                    "placeId"			-> persistedPlaceThirdParty.placeId,
		    	  	    "thirdPartyID" 		-> persistedPlaceThirdParty.thirdPartyID,
		        	    "thirdPartyPlaceId" -> persistedPlaceThirdParty.thirdPartyPlaceId,
		        	    "thirdPartyRef" 	-> persistedPlaceThirdParty.thirdPartyRef
		        	)
	            })
	            Ok(jsonResp)
            }
            case _ => BadRequest("Place not found")
        }
	}
	
	def placeById(placeId: Long) = Action {
	    Place.byId(placeId) match {
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
         } // end - Place.byId(placeId)
	} // end - def placeById
	
	def placeThirdPartyByTTPlaceId(ttPlaceId: Long) = Action {
	    val placesThirdPartyRefs = PlaceThirdParty.byTTPlaceId(ttPlaceId) // returns List 3rd party refs
	    
	    val placesThirdPartyRefsJson = Json.obj(
    		"placesThirdPartyRefs"	-> {
    			placesThirdPartyRefs.map(p3pr => Json.obj(
    			    "id"				-> p3pr.id.get,
                    "placeId"			-> p3pr.placeId,
	    	  	    "thirdPartyID" 		-> p3pr.thirdPartyID,
	        	    "thirdPartyPlaceId" -> p3pr.thirdPartyPlaceId,
	        	    "thirdPartyRef" 	-> p3pr.thirdPartyRef
    			))
    		}
		)

	    Json.toJson(placesThirdPartyRefsJson)
	    Ok(placesThirdPartyRefsJson)
	}
} // end - object Locations


