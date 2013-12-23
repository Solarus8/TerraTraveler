package models

import java.util.{Date}

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class User(
    id: 	    Pk[Long] = NotAssigned,
    created:    Date,
    lastActive: Option[Date],
    lastLogin:	Option[Date],
    userName:   String, 
    email: 	    String, 
    password:   String, 
    role:       String,
    primaryLoc: Option[Long]
)

object User {
  
	// -- Parsers
  
	/**
	 * Parse a User from a ResultSet
	 */
	val simple = {
		get[Pk[Long]]("user.id") ~
		get[Date]("user.created") ~
		get[Option[Date]]("user.last_active") ~
		get[Option[Date]]("user.last_login") ~
	    get[String]("user.user_name") ~
	    get[String]("user.email") ~
	    get[String]("user.password") ~
	    get[String]("user.role") ~
	    get[Option[Long]]("user.primary_loc") map {
			case id ~ created ~ lastActive ~ lastLogin ~ userName ~ email ~ password ~ role ~ primaryLoc => 
			  	User(id, created, lastActive, lastLogin, userName, email, password, role, primaryLoc)
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
	 * Retrieve a User by id.
	 */
	def findById(id: Long): Option[User] = {
		DB.withConnection { implicit connection =>
	      	SQL("""
	      	    select * from "user" where id = {id}
	      	    """).on(
	  			'id -> id
			).as(User.simple.singleOpt)
	    }
	}
  
	/**
	 * Retrieve all users.
	 */
	def findAll: List[User] = {
	    DB.withConnection { implicit connection =>
	      	SQL(
	      	    """
	      	    select * from "user"
      			"""
	      	).as(User.simple *)
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
	def create(userName: String, email: String, password: String, role: String, primaryLoc: Long): Pk[Long] = {
	    DB.withConnection { implicit connection =>
	      	SQL(
	      		"""
      			insert into "user" (user_name, email, password, role, primary_loc) values (
      				{userName}, {email}, {password}, {role}, {primaryLoc}
      			)
	      		"""
      		).on(
      			'userName   -> userName,
      			'email 	    -> email,
      			'password   -> password,
      			'role       -> role,
      			'primaryLoc -> primaryLoc
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
      			insert into "user" (user_name, email, password, role, primary_loc) values (
      				{userName}, {email}, {password}, {role}, {primaryLoc}
      			)
	      		"""
      		).on(
      			'userName   -> user.userName,
      			'email 	    -> user.email,
      			'password   -> user.password,
      			'role	    -> user.role,
      			'primaryLoc -> user.primaryLoc
      		).executeInsert()
    	} match {
	        case Some(long) => new Id[Long](long) // The Primary Key
	        case None       => throw new Exception("SQL Error - Did not insert User.")
    	}
	} // End - create
	
}






