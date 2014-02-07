package models

import java.util.{Date}

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Trip (
	id:			Pk[Long] = NotAssigned,
	name:		String,
	desc:		Option[String],
	userId:		Long,
	dateFrom:	Option[Date],
	dateTo:		Option[Date],
	status:		Option[String]
)

object Trip {
	// -- Parsers
  
	/**
	 * Parse a Trip from a ResultSet
	 */
	val simple = {
		get[Pk[Long]]("trip.id") ~
		get[String]("trip.name") ~
		get[Option[String]]("trip.desc") ~
		get[Long]("trip.user_id") ~
	    get[Option[Date]]("trip.date_from") ~
	    get[Option[Date]]("trip.date_to") ~
	    get[Option[String]]("trip.status") map {
			case id ~ name ~ desc ~ userId ~ dateFrom ~ dateTo ~ status => 
			  	Trip(id, name, desc,  userId, dateFrom, dateTo, status)
	    }
	}
	
	// Create a Trip with a Trip object
	def create(trip: Trip): Pk[Long] = {
		DB.withConnection { implicit connection =>
	      	SQL(
	      		"""
      			insert into trip (name, "desc", user_id, date_from, date_to, status) values (
      				{name}, {desc}, {userId}, {dateFrom}, {dateTo}, {status}
      			)
	      		"""
      		).on(
      			'name   	-> trip.name,
      			'desc 	    -> trip.desc,
      			'userId   	-> trip.userId,
      			'dateFrom	-> trip.dateFrom,
      			'dateTo 	-> trip.dateTo,
      			'status		-> trip.status
      		).executeInsert()
    	} match {
	        case Some(long) => new Id[Long](long) // The Primary Key
	        case None       => throw new Exception("SQL Error - Did not insert Trip.")
    	}
	} // End - create
	
	/**
	 * Retrieve a Trip by id.
	 */
	def byId(id: Long): Option[Trip] = {
		DB.withConnection { implicit connection =>
	      	SQL("""
	      	    select * from trip where id = {id}
	      	    """).on(
	  			'id -> id
			).as(Trip.simple.singleOpt)
	    }
	}
}



