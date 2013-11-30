package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class User(
    id: 	   Pk[Long] = NotAssigned,
    userName:  String,  
    email: 	   String, 
    password:  String
)

object User {
  
  // -- Parsers
  
	/**
	 * Parse a User from a ResultSet
	 */
	val simple = {
		get[Pk[Long]]("user.id") ~
	    get[String]("user.user_name") ~
	    get[String]("user.email") ~
	    get[String]("user.password") map {
			case id~userName~email~password => 
			  	User(id, userName, email, password)
	    }
	}
  
  // -- Queries
  
  /**
   * Retrieve a User from email.
   */
	def findByEmail(email: String): Option[User] = {
	    DB.withConnection { implicit connection =>
	      	SQL("""
	      		select * from "user" where email = {email}
	      	    """).on(
	      		'email -> email
	      	).as(User.simple.singleOpt)
	    }
	}
  
	/**
	 * Retrieve all users.
	 */
	def findAll: List[User] = {
	    DB.withConnection { implicit connection =>
	      	SQL("""
	      	    select * from "user"
	      	""").as(User.simple *)
	    }
	}
  
	/**
	 * Authenticate a User.
	 */
	def authenticate(email: String, password: String): Option[User] = {
		println("User - authenticate")
	    DB.withConnection { implicit connection =>
	      	SQL(
	      		"""
	      		select * from "user" where email = {email} and password = {password}
	      		"""
	      	).on(
	    		 'email -> email,
	    		 'password -> password
	      	).as(User.simple.singleOpt)
	    }
	}
   
	/**
	 * Create a User with atomic User fields.
	 */
	def create(userName: String, email: String, password: String): Pk[Long] = {
	    DB.withConnection { implicit connection =>
	      	SQL(
	      		"""
      			insert into "user" (user_name, email, password) values (
      				{userName}, {email}, {password}
      			)
	      		"""
      		).on(
      			'userName  -> userName,
      			'email 	   -> email,
      			'password  -> password
      		).executeInsert()
    	} match {
	        case Some(long) => new Id[Long](long) // The Primary Key
	        case None       => throw new Exception("SQL Error - Did not insert User.")
    	}
	}
	
	// Create a User with a User object
	def create(user: User): Pk[Long] = {
		DB.withConnection { implicit connection =>
	      	SQL(
	      		"""
      			insert into "user" (user_name, email, password) values (
      				{userName}, {email}, {password}
      			)
	      		"""
      		).on(
      			'userName  -> user.userName,
      			'email 	   -> user.email,
      			'password  -> user.password
      		).executeInsert()
    	} match {
	        case Some(long) => new Id[Long](long) // The Primary Key
	        case None       => throw new Exception("SQL Error - Did not insert User.")
    	}
	} // End - create
}






