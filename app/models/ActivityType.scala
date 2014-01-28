package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class ActivityType (
	id:			Pk[Long],
	activity:	String,
	category:	String
)

object ActivityType {
	
    // -- Parsers
  
	/**
	 * Parse an Event from a ResultSet
	 */
	val simple = {
	    println("ActivityType.simple - TOP")
		get[Pk[Long]]("activity_type.id") ~
		get[String]("activity_type.type") ~
		get[String]("activity_category.category") map {
		    case id ~ activity ~ category =>
		        ActivityType(id, activity, category)
		}
	}
	
	/**
	 * Retrieve all events.
	 * SELECT table1.column1, table2.column2 FROM table1, table2 WHERE table1.column1 = table2.column1;
	 */
	def all: List[ActivityType] = {
	    println("ActivityType.all - TOP")
	    val actypes = DB.withConnection { implicit connection =>
	      	SQL(
	      	    """
	      	    SELECT activity_type.id, activity_type.type, activity_category.category FROM
		        activity_type LEFT JOIN
		        (
		        activity_type_category JOIN activity_category
		             ON activity_type_category.cat_id = activity_category.id
		        )
		        ON activity_type.id = activity_type_category.type_id;
      			"""
	      	).as(ActivityType.simple *)
	    }
	    //println("ActivityType.all - BOTTOM - actypes.size: " + actypes.size)
	    actypes
	}
}