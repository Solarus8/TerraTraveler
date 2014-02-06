package models

import java.util.{Date}

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Journal (
    id:			Pk[Long] = NotAssigned,
	tripId:		Long,
	title:		String,
	desc:		Option[String],
	index:		Option[Int]
)

object Journal {
    
    // -- Parsers
  
	/**
	 * Parse a Trip from a ResultSet
	 */
	val simple = {
		get[Pk[Long]]("journal.id") ~
		get[Long]("journal.trip_id") ~
		get[String]("journal.title") ~
		get[Option[String]]("journal.desc") ~
	    get[Option[Int]]("journal.index") map {
			case id ~ tripId ~ title ~ desc ~ index => 
			  	Journal(id, tripId, title, desc, index)
	    }
	}
	
	// Create a Journal with a Journal object
	def create(journal: Journal): Pk[Long] = {
		DB.withConnection { implicit connection =>
	      	SQL(
	      		"""
      			insert into journal (tripId, title, desc, index) values (
      				{tripId}, {title}, {desc}, {index}
      			)
	      		"""
      		).on(
      			'tripId   	-> journal.tripId,
      			'title 	    -> journal.title,
      			'desc   	-> journal.desc,
      			'index	-> journal.index
      		).executeInsert()
    	} match {
	        case Some(long) => new Id[Long](long) // The Primary Key
	        case None       => throw new Exception("SQL Error - Did not insert Journal.")
    	}
	} // End - create
	
	/**
	 * Retrieve a Journal by id.
	 */
	def byId(id: Long): Option[Journal] = {
		DB.withConnection { implicit connection =>
	      	SQL("""
	      	    select * from journal where id = {id}
	      	    """).on(
	  			'id -> id
			).as(Journal.simple.singleOpt)
	    }
	}
}