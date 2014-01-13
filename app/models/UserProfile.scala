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
	
	def create(profile: UserProfile): Pk[Long] = {
	    println("UserProfile.create - TOP")
		DB.withConnection { implicit connection =>
	      	val sql = SQL(
	      		"""
      			insert into user_profile(
	      	        user_id, first_name, last_name, gender, birthdate, nationality, portrait_url, bio, story
	      	    ) 
	      	    values (
	      	        {userId}, {firstName}, {lastName}, {gender}, {birthdate}, 
	      	        	{nationality}, {portraitUrl}, {bio}, {story}
	      	    """
      	    ).on (
      	        'userId			-> profile.userId,
      	        'firstName		-> profile.firstName,
      	        'lastName		-> profile.lastName,
      	        'gender			-> profile.gender,
      	        'birthdate		-> profile.birthdate,
      	        'nationality	-> profile.nationality,
      	        'portraitUrl	-> profile.portraitUrl,
      	        'bio			-> profile.bio,
      	        'story			-> profile.story
      	    )
      	    println("UserProfile.create - sql: " + sql)
      	    sql.executeInsert()
		} match {
	        case Some(long) => {
	            println("UserProfile.create - Some(long): " + long)
	            new Id[Long](long) // The Primary Key
	        }
	        case _       => {
	            println("UserProfile.create - None")
	            throw new Exception("SQL Error - Did not insert UserProfile")
	        }
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
	
	/**
	 * Retrieve a UserProfile by id.
	 */
	def findById(id: Long): Option[UserProfile] = {
		DB.withConnection { implicit connection =>
	      	SQL("""
	      	    select * from user_profile where id = {id}
	      	    """).on(
	  			'id -> id
			).as(UserProfile.simple.singleOpt)
	    }
	}
}






