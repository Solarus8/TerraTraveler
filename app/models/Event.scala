package models

import java.util.{Date}

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Event (
    id:				Pk[Long] = NotAssigned,
    date:			Date,
    placeId:		Long,
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
		get[Long]("event.place_id") ~
		get[String]("event.desc") ~
		get[Int]("event.min_size") ~
		get[Int]("event.max_size") ~
		get[Option[Int]]("event.rsvp_tot") ~
		get[Option[Int]]("event.wait_list_tot") map {
		    case id ~ date ~ placeId ~ description ~ minSize ~ maxSize ~ rsvpTotal ~ waitListTotal =>
		        Event(id, date, placeId, description, minSize, maxSize, rsvpTotal, waitListTotal)
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
	
	def findByUserId(userId: Long): List[Event] = {
	    DB.withConnection { implicit connection =>
	      	SQL(
	      	    """
	      	    SELECT * FROM event e
	      	    JOIN user_event ue ON ue.event_id = e.id
	      	    where ue.user_id = {userId}
      			"""
	      	)
	      	.on("userId" -> userId)
	      	.as(Event.simple *)
	    }
	}
	
	/**
	 * Create an Event with atomic Event fields.
	 */
	def create(date: Date, placeId: Long, desc: String, minSize: Int, maxSize: Int, rsvpTot: Int, waitListTot: Int): Pk[Long] = {
	    DB.withConnection { implicit connection =>
	      	SQL(
	      		"""
      			insert into event (date, place_id, desc, min_size, max_size, rsvp_tot, wait_list_tot) values (
      				{date}, {placeId}, {desc}, {minSize}, {maxSize}, {rsvpTot}, {waitListTot}
      			)
	      		"""
      		).on(
      			'date   	 -> date,
      			'placeId 	 -> placeId,
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
      			'locId 	     -> event.placeId,
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
	
	def attendies(eventId: Long): List[User] = {
	    DB.withConnection { implicit connection =>
	      	SQL(
	      	    """
	      	    SELECT * FROM "user" u
	      	    JOIN user_event ue ON ue.user_id = u.id
	      	    where ue.event_id = {eventId}
      			"""
	      	)
	      	.on("eventId" -> eventId)
	      	.as(User.simple *)
	    }
	}
}
	
	
