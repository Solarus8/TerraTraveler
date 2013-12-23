package models

import java.util.{Date}

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Event (
    id:				Pk[Long] = NotAssigned,
    date:			Date,
    locationId:		Long,
    description:	String,
    minSize:		Int,
    maxSize:		Int,
    rsvpTotal:		Option[Int],
    waitListTotal:	Option[Int]   
)

object Event {
	
    // -- Parsers
  
	/**
	 * Parse an Event from a ResultSet
	 */
	val simple = {
		get[Pk[Long]]("event.id") ~
		get[Date]("event.date") ~
		get[Long]("event.loc_id") ~
		get[String]("event.desc") ~
		get[Int]("event.min_size") ~
		get[Int]("event.max_size") ~
		get[Option[Int]]("event.rsvp_tot") ~
		get[Option[Int]]("event.wait_list_tot") map {
		    case id ~ date ~ locationId ~ description ~ minSize ~ maxSize ~ rsvpTotal ~ waitListTotal =>
		        Event(id, date, locationId, description, minSize, maxSize, rsvpTotal, waitListTotal)
		}	
	}
	
	/**
	 * Retrieve an Event by id.
	 */
	def findById(id: Long): Option[Event] = {
		DB.withConnection { implicit connection =>
	      	SQL("""
	      	    select * from event where id = {id}
	      	    """).on(
	  			'id -> id
			).as(Event.simple.singleOpt)
	    }
	}
	
	/**
	 * Retrieve all events.
	 */
	def findAll: List[Event] = {
	    DB.withConnection { implicit connection =>
	      	SQL(
	      	    """
	      	    select * from event
      			"""
	      	).as(Event.simple *)
	    }
	}
	
	/**
	 * Create an Event with atomic Event fields.
	 */
	def create(date: Date, locId: Long, desc: String, minSize: Int, maxSize: Int, rsvpTot: Int, waitListTot: Int): Pk[Long] = {
	    DB.withConnection { implicit connection =>
	      	SQL(
	      		"""
      			insert into event (date, loc_id, desc, min_size, max_size, rsvp_tot, wait_list_tot) values (
      				{date}, {locId}, {desc}, {minSize}, {maxSize}, {rsvpTot}, {waitListTot}
      			)
	      		"""
      		).on(
      			'date   	 -> date,
      			'locId 	     -> locId,
      			'desc   	 -> desc,
      			'minSize     -> minSize,
      			'maxSize	 -> maxSize,
      			'rsvpTot	 -> rsvpTot,
      			'waitListTot -> waitListTot
      		).executeInsert()
    	} match {
	        case Some(long) => new Id[Long](long) // The Primary Key
	        case None       => throw new Exception("SQL Error - Did not insert Event.")
    	}
	}
	
	// Create an Event with an Event object
	def create(event: Event): Pk[Long] = {
		DB.withConnection { implicit connection =>
	      	SQL(
	      		"""
      			insert into event (date, loc_id, desc, min_size, max_size, rsvp_tot, wait_list_tot) values (
      				{date}, {locId}, {desc}, {minSize}, {maxSize}, {rsvpTot}, {waitListTot}
      			)
	      		"""
      		).on(
      			'date   	 -> event.date,
      			'locId 	     -> event.locationId,
      			'desc   	 -> event.description,
      			'minSize     -> event.minSize,
      			'maxSize	 -> event.maxSize,
      			'rsvpTot	 -> event.rsvpTotal,
      			'waitListTot -> event.waitListTotal
      		).executeInsert()
    	} match {
	        case Some(long) => new Id[Long](long) // The Primary Key
	        case None       => throw new Exception("SQL Error - Did not insert User.")
    	}
	} // End - create
}
	
	
