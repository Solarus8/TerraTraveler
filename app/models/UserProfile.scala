package models

import java.util.{Date}

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class UserProfile (
	id: 	    	Pk[Long] = NotAssigned,
	userId:			Long,
	firstName:		Option[String],
	lastName:		Option[String],
	gender:			Option[String],
	birthdate:		Option[Date],
	nationality:	Option[String],
	portraitUrl:	Option[String],
	bio:			Option[String],
	story:			Option[String]
)

object UserProfile {
  
	// -- Parsers
  
	/**
	 * Parse a UserProfile from a ResultSet
	 */
	val simple = {
	    get[Pk[Long]]("user_profile.id") ~
	    get[Long]("user_profile.user_id") ~
	    get[Option[String]]("user_profile.first_name") ~
	    get[Option[String]]("user_profile.last_name") ~
	    get[Option[String]]("user_profile.gender") ~
	    get[Option[Date]]("user_profile.birthdate") ~
	    get[Option[String]]("user_profile.nationality") ~
	    get[Option[String]]("user_profile.portrait_url") ~
	    get[Option[String]]("user_profile.bio") ~
	    get[Option[String]]("user_profile.story") map {
	        case id ~ userId ~ firstName ~ lastName ~ gender ~ birthdate ~ nationality ~ portraitUrl ~ bio ~ story =>
	            UserProfile(id, userId, firstName, lastName, gender, birthdate, nationality, portraitUrl, bio, story)
	    }
	}
	
	/**
	 * Retrieve a UserProfile from user_id.
	 */
	def findProfile(user_id: Long): Option[UserProfile] = {
		DB.withConnection { implicit connection =>
	      	SQL("""
	      	    select * from user_profile where user_id = {user_id}
	      	    """).on(
	  			'user_id -> user_id
			).as(UserProfile.simple.singleOpt)
	    }
	}
}