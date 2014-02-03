package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class ActivityCategory (
	id:			Pk[Long],
	category:	String
)

case class EventAssocCategory (
    eventId:		Long,
    activityCatId:	Long
)

object ActivityCategory {
        
    // -- Parsers
  
	/**
	 * Parse an ActivityCategory from a ResultSet
	 */
	val simpleActivityCategory = {
	    println("ActivityCategory.simpleActivityCategory - TOP")
		get[Pk[Long]]("activity_category.id") ~
		get[String]("activity_category.category") map {
		    case id ~ category =>
		        ActivityCategory(id, category)
		}
	}
	
	/**
	 * Parse an EventAssocCategory from a ResultSet
	 */
	val simpleEventAssocCategory = {
	    println("ActivityCategory.simpleEventAssocCategory - TOP")
	    get[Long]("event_assoc_category.event_id") ~
	    get[Long]("event_assoc_category.activity_cat_id") map {
	        case eventId ~ activityCatId =>
	            EventAssocCategory(eventId, activityCatId)
	    }
	}
	
	/**
	 * Retrieve all ActivityCategories.
	 */
	def allActivityCategories: List[ActivityCategory] = {
	    println("ActivityCategory.all - TOP")
	    val actcats = DB.withConnection { implicit connection =>
	      	SQL(
	      	    """
	      	    SELECT * from activity_category;
      			"""
	      	).as(ActivityCategory.simpleActivityCategory *)
	    }
	    //println("ActivityCategory.all - BOTTOM - actcats.size: " + actcats.size)
	    actcats
	}
	
	def findAssocActivityCatByEventId(eventId: Long): List[ActivityCategory] = {
	    println("ActivityCategory.findAssocActivityCatByEventId - TOP - eventId: " + eventId)
	    val actcats = DB.withConnection { implicit connection =>
	      	SQL(
	      	    """
	      	    SELECT * FROM activity_category WHERE id IN (
	      			SELECT activity_cat_id FROM event_assoc_category WHERE event_id = {eventId}
      	        );
      			"""
	      	)
	      	.on('eventId -> eventId)
	      	.as(ActivityCategory.simpleActivityCategory *)
	    }
	    println("ActivityCategory.findAssocActivityCatByEventId - BOTTOM - actcats.size: " + actcats.size)
	    actcats 
	}
}






