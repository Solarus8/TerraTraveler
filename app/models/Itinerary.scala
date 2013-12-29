package models

import java.util.{Date}

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Itinerary (
    id:				Pk[Long] = NotAssigned,
    userId:			Long,
    name:			Option[String],
	description:	Option[String],
	dateFrom:		Option[Date],
	dateTo:			Option[Date]
)

object Itinerary {
	    
    // -- Parsers
  
	/**
	 * Parse an Itinerary from a ResultSet
	 */
	val simple = {
		get[Pk[Long]]("itinerary.id") ~
		get[Long]("itinerary.user_id") ~
		get[Option[String]]("itinerary.name") ~
		get[Option[String]]("itinerary.desc") ~
		get[Option[Date]]("itinerary.date_from") ~
		get[Option[Date]]("itinerary.date_to") map {
		    case id ~ userId ~ name ~ descripton ~ dateFrom ~ dateTo =>
		        Itinerary(id, userId, name, descripton, dateFrom, dateTo)
		}	
	}
	
	/**
	 * Retrieve an Itinerary by user_id.
	 */
	def findByUserId(user_id: Long): List[Itinerary] = {
		DB.withConnection { implicit connection =>
	      	SQL("""
	      	    select * from itinerary where user_id = {user_id}
	      	    """).on(
	  			'user_id -> user_id
			).as(Itinerary.simple *)
	    }
	}

}