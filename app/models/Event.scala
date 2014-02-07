package models

import java.util.{Date}

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Event (
    id:					Pk[Long] = NotAssigned,
    from:				Date,
    to:					Option[Date],
    title:				Option[String],
    activityType:		Int,
    placeId:			Option[Long],
    description:		String,
    minSize:			Int,
    maxSize:			Int,
    rsvpTotal:			Option[Int],
    waitListTotal:		Option[Int]   
)

object Event {
	
    // -- Parsers
  
	/**
	 * Parse an Event from a ResultSet
	 */
	val simple = {
		get[Pk[Long]]("event.id") ~
		get[Date]("event.from") ~
		get[Option[Date]]("event.to") ~
		get[Option[String]]("event.title") ~
		get[Int]("event.activity_type_id") ~
		get[Option[Long]]("event.place_id") ~
		get[String]("event.desc") ~
		get[Int]("event.min_size") ~
		get[Int]("event.max_size") ~
		get[Option[Int]]("event.rsvp_tot") ~
		get[Option[Int]]("event.wait_list_tot") map {
		    case id ~ from ~ to ~ title ~ activityType ~ placeId ~ description ~ minSize ~ maxSize ~ rsvpTotal ~ waitListTotal =>
		        Event(id, from, to, title, activityType, placeId, description, minSize, maxSize, rsvpTotal, waitListTotal)
		}	
	}
	
	/**
	 * Retrieve an Event by id.
	 */
	def byId(id: Long): Option[Event] = {
	    println("Event.findById - TOP")
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
	def all: List[Event] = {
	    DB.withConnection { implicit connection =>
	      	SQL(
	      	    """
	      	    select * from event
      			"""
	      	).as(Event.simple *)
	    }
	}
	
	def byUserId(userId: Long): List[Event] = {
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
	def create(from: Date, to: Date, title: String, activityType: Int, placeId: Long, 
	    desc: String, minSize: Int, maxSize: Int, rsvpTot: Int, waitListTot: Int): Pk[Long] = {
	    
	    DB.withConnection { implicit connection =>
	      	SQL(
	      		"""
      			insert into event (from, to, title, activity_type_id, place_id, desc, min_size, max_size, rsvp_tot, wait_list_tot) values (
      				{from}, {to}, {title}, {activityType}, {placeId}, {desc}, {minSize}, {maxSize}, {rsvpTot}, {waitListTot}
      			)
	      		"""
      		).on(
      			'from   	 	-> from,
      			'to			 	-> to,
      			'title		 	-> title,
      			'activityType	-> activityType,
      			'placeId 	 	-> placeId,
      			'desc   	 	-> desc,
      			'minSize     	-> minSize,
      			'maxSize	 	-> maxSize,
      			'rsvpTot	 	-> rsvpTot,
      			'waitListTot 	-> waitListTot
      		).executeInsert()
    	} match {
	        case Some(pk) => new Id[Long](pk) // The Primary Key
	        case None     => throw new Exception("SQL Error - Did not insert Event.")
    	}
	}
	
	// Create an Event with an Event object
	def create(event: Event): Pk[Long] = {
	    println("Event.create - TOP")
		DB.withConnection { implicit connection =>
	      	val sql = SQL(
	      		"""
      			insert into event ("from", "to", title, activity_type_id, place_id, 
	      	        "desc", min_size, max_size, rsvp_tot, wait_list_tot) values (
      				{from}, {to}, {title}, {activityType}, {placeId}, 
	      	        {desc}, {minSize}, {maxSize}, {rsvpTot}, {waitListTot}
      			)
	      		"""
      		).on(
      			'from   	 	-> event.from,
      			'to			 	-> event.to,
      			'title			-> event.title,
      			'activityType	-> event.activityType,
      			'placeId 	 	-> event.placeId,
      			'desc   	 	-> event.description,
      			'minSize     	-> event.minSize,
      			'maxSize	 	-> event.maxSize,
      			'rsvpTot	 	-> event.rsvpTotal,
      			'waitListTot 	-> event.waitListTotal
      		)
      		println("Event.create - sql: " + sql)
      		sql.executeInsert()
    	} match {
	        case Some(long) => new Id[Long](long) // The Primary Key
	        case None       => throw new Exception("SQL Error - Did not insert User.")
    	}
	} // End - create
	
	def byLocationRadius(locId: Long, radius: Int): List[Event] = {
	    println("Event.byRadiuslocation(locId: Long, radius: Int) - TOP")
		DB.withConnection { implicit connection =>
	      	val sql = SQL(
	      		"""
	      	    SELECT * from event
				where event.place_id in
				(
					SELECT id from place
					where place.loc_id in
					(
						SELECT id from location
						where ST_DWithin(
			    			geoloc,
			    			(
			        			SELECT geoloc
			        			FROM location
			        			WHERE id = {locId}
			    			),
      	        			{radius}
						)
					)
				)  
	      	    """
	      	).on(
      			"locId"  -> locId,
      			"radius" -> radius
	      	)
	      	sql.as(Event.simple *)
	    }
	}
	
	def byLatLonRadiusTypeCat(lat: Double, lon: Double, radius: Int, 
	    activityType: Int, activityCategory: Int): List[Event] = {
	    
	    println("Event.byLatLonRadiusTypeCat - TOP - lat: " + lat + " | lon: " + lon + " | radius: " + radius)
	    	    
	    DB.withConnection { implicit connection =>
	      	val sql = SQL(
	      		"""
	      	    SELECT * FROM event
				WHERE event.place_id in
				(
					SELECT id FROM place
					WHERE place.loc_id in
					(
						SELECT id FROM location
						WHERE ST_DWithin(
			    			geoloc,
			    			(
			        			ST_GeographyFromText('SRID=4326;POINT(""" + lon + """ """ + lat + """)')
			    			),
      	        			{radius}
						)
					)
				) 
			    AND event.activity_type_id = {activityType}
			    AND event.id IN (
			        SELECT event_id FROM event_assoc_category WHERE activity_cat_id = {activityCategory}
			    )
	      	    """
	      	).on(
      			"radius" 			-> radius,
      			"activityType" 		-> activityType,
      			"activityCategory" 	-> activityCategory 
	      	)
	      	//println("Event.byRadiusLatLon - sql: " + sql)
	      	val result = sql.as(Event.simple *)
	      	println("Event.byRadiusLatLon - result: " + result)
	      	result
	    }
	}
	
	/**
	 * TODO: Checks against max number of attendies
	 */ 
	def associateEventUser(eventId: Long, userId: Long): Pk[Long] = {
	    println("Event.associateUserEvent - TOP - userId: " + userId + " | eventId: " + eventId)
		
	    /*val max_size: Option[Int] = DB.withConnection { implicit connection =>
		    SQL(
		        """
		        select max_size from event where id = {eventId}    
		        """
		    ).on (
		    	'eventId -> eventId
		    ).as(scalar[Int].singleOpt)
	    }
	    max_size match {
	        case _ : Some[Int] => {
	            
	        }
	    }*/
		    
		DB.withConnection { implicit connection =>
	      	SQL(
	      		"""
      			insert into user_event ("user_id", "event_id") 
	      	    values ({userId}, {eventId})
	      	    """
	      	).on(
      			'userId		-> userId,
      			'eventId	-> eventId
      		).executeInsert()
	    } match {
	        case Some(pk) => {
	            println("Event.associateUserEvent - Some(pk): " + pk)
	            new Id[Long](pk) // The Primary Key
	        }
	        case None     => throw new Exception("SQL Error - Did not insert user_event.")
    	}
	}
	
	def associateEventCategory(eventId: Long, activityCategoryIds: List[Int]): List[Pk[Long]] = {
	    println("Event.associateEventCategory - TOP - eventId: " + eventId + " | cats.length: " + activityCategoryIds.length)
	    val pkList = activityCategoryIds.map(
	        catId => DB.withConnection { implicit connection =>
	            SQL(
	      		"""
      			insert into event_assoc_category("event_id", "activity_cat_id")
                values ({eventId}, {catId})
                """
	            ).on(
            		'eventId	-> eventId,
            		'catId		-> catId
                ).executeInsert()
	        } match {
		        case Some(pk) => new Id[Long](pk) // The Primary Key
		        case None     => throw new Exception("SQL Error - Did not insert event_assoc_category.")
	    	}
	    )
	    println("Event.associateEventCategory - BOTTOM - pkList: " + pkList)
	    pkList
	}
	
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
	
	
