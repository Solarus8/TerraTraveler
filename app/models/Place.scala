package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Place (
	id:				Pk[Long] = NotAssigned,
	locId:			Long,
	name:			String,
	description: 	Option[String],
	category:		Option[String],
	tag:			Option[String],
	url:			Option[String]
)

object Place {
    // -- Parsers
  
	/**
	 * Parse a Place from a ResultSet
	 */
	val simple = {
		get[Pk[Long]]("place.id") ~
		get[Long]("place.loc_id") ~
		get[String]("place.name") ~
		get[Option[String]]("place.desc") ~
		get[Option[String]]("place.cat") ~
		get[Option[String]]("place.tag") ~
		get[Option[String]]("place.url") map {
		    case id ~ locId ~ name ~ description ~ category ~ tag ~ url =>
		        Place(id, locId, name, description, category, tag, url)
		}	
	}
	
	/**
	 * Retrieve an Place by id.
	 */
	def findById(id: Long): Option[Place] = {
		DB.withConnection { implicit connection =>
	      	SQL("""
	      	    select * from place where id = {id}
	      	    """).on(
	  			'id -> id
			).as(Place.simple.singleOpt)
	    }
	}
}