package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class ActivityType (
	id:			Pk[Long],
	activity:	String
)

object ActivityType {
	
    // -- Parsers
  
	/**
	 * Parse an Event from a ResultSet
	 */
	val simple = {
	    println("ActivityType.simple - TOP")
		get[Pk[Long]]("activity_type.id") ~
		get[String]("activity_type.type") map {
		    case id ~ activity =>
		        ActivityType(id, activity)
		}
	}
	
	/**
	 * Retrieve all ActivityTypes.
	 */
	def all: List[ActivityType] = {
	    println("ActivityType.all - TOP")
	    val actypes = DB.withConnection { implicit connection =>
	      	SQL(
	      	    """
	      	    SELECT * from activity_type;
      			"""
	      	).as(ActivityType.simple *)
	    }
	    //println("ActivityType.all - BOTTOM - actypes.size: " + actypes.size)
	    actypes
	}
}




