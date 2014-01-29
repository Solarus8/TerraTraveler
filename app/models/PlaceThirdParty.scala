package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class PlaceThirdParty (
	id:					Pk[Long] = NotAssigned,
	placeId:			Option[Long],
	thirdPartyID:		Long,
	thirdPartyPlaceId:	String,
	thirdPartyRef:		Option[String]
)

object PlaceThirdParty {

    // -- Parsers
  
	/**
	 * Parse a PlaceThirdParty from a ResultSet
	 */
	val simple = {
		get[Pk[Long]]("place_thirdparty.id") ~
		get[Option[Long]]("place.id") ~
		get[Long]("place_thirdparty.third_party_id") ~
		get[String]("place_thirdparty.third_party_place_id") ~ 
		get[Option[String]]("place_thirdparty.third_party_place_ref") map {
		    case id ~ placeId ~ thirdPartyID ~ thirdPartyPlaceId ~ thirdPartyRef =>
		        PlaceThirdParty(id, placeId, thirdPartyID, thirdPartyPlaceId, thirdPartyRef)
		}	
	}
	
	/**
	 * Create a PlaceThirdParty with a PlaceThirdParty object
	 */
	def create(place: PlaceThirdParty): Pk[Long] = {
		DB.withConnection { implicit connection =>
	      	SQL(
	      		"""
      			insert into place_thirdparty (placeId, thirdPartyID, thirdPartyPlaceId, thirdPartyRef) values (
      				{placeId}, {thirdPartyID}, {thirdPartyPlaceId}, {thirdPartyRef}}
      			)
	      		"""
      		).on(
      			'placeId   			-> place.placeId,
      			'thirdPartyID 	    -> place.thirdPartyID,
      			'thirdPartyPlaceId	-> place.thirdPartyPlaceId,
      			'thirdPartyRef	    -> place.thirdPartyRef
      		).executeInsert()
    	} match {
	        case Some(long) => new Id[Long](long) // The Primary Key
	        case None       => throw new Exception("SQL Error - Did not insert PlaceThirdParty.")
    	}
	} // End - create
	
	/**
	 * Retrieve a PlaceThirdParty by id.
	 */
	def byId(id: Long): Option[PlaceThirdParty] = {
		DB.withConnection { implicit connection =>
	      	SQL("""
	      	    select * from place_thirdparty where id = {id}
	      	    """).on(
	  			'id -> id
			).as(PlaceThirdParty.simple.singleOpt)
	    }
	}
	
	def byTTPlaceId(ttPlaceId: Long): List[PlaceThirdParty] = {
		DB.withConnection { implicit connection =>
	      	SQL("""
	      	    select * from place_thirdparty where place_id = {ttPlaceId}
	      	    """).on(
	      	    'ttPlaceId -> ttPlaceId       
	      	).as(PlaceThirdParty.simple *)
		}
	}
}



