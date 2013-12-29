package models

import java.util.{Date}

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class ItineraryItem (
    id:				Pk[Long] = NotAssigned,
    itineraryId:	Long,
    locId:			Option[Long],
    placeId:		Option[Long],
    contactId:		Option[Long],
    groupId:		Option[Long],
    eventId:		Option[Long],
    tagId:			Option[Long],
    dateFrom:		Option[Date],
    dateTo:			Option[Date],
	index:			Option[Int]
)

object ItineraryItem {
	    
    // -- Parsers
  
	/**
	 * Parse an Itinerary from a ResultSet
	 */
	val simple = {
		get[Pk[Long]]("itinerary_item.id") ~
		get[Long]("itinerary_item.itinerary_id") ~
		get[Option[Long]]("itinerary_item.loc_id") ~
		get[Option[Long]]("itinerary_item.place_id") ~
		get[Option[Long]]("itinerary_item.contact_id") ~
		get[Option[Long]]("itinerary_item.group_id") ~
		get[Option[Long]]("itinerary_item.event_id") ~
		get[Option[Long]]("itinerary_item.tag_id") ~
		get[Option[Date]]("itinerary_item.date_from") ~
		get[Option[Date]]("itinerary_item.date_to") ~
		get[Option[Int]]("itinerary_item.index") map {
		    case id ~ itineraryId ~ locId ~ placeId ~ contactId ~ groupId ~ eventId ~ tagId ~ dateFrom ~ dateTo ~ index =>
		        ItineraryItem(id, itineraryId, locId, placeId, contactId, groupId, eventId, tagId, dateFrom, dateTo, index)
		}	
	}
	
	/**
	 * Retrieve an Itinerary by user_id.
	 */
	def findById(id: Long): List[ItineraryItem] = {
		DB.withConnection { implicit connection =>
	      	SQL("""
	      	    select * from itinerary_item where id = {id}
	      	    """).on(
	  			'id -> id
			).as(ItineraryItem.simple *)
	    }
	}

}