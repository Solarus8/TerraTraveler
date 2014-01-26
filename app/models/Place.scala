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
		get[Option[String]]("place.url") map {
		    case id ~ locId ~ name ~ description ~ category ~ url =>
		        Place(id, locId, name, description, category, url)
		}	
	}
	
	// Create a Place with a Place object
	def create(place: Place): Pk[Long] = {
		DB.withConnection { implicit connection =>
	      	SQL(
	      		"""
      			insert into place (loc_id, name, "desc", cat, url) values (
      				{locId}, {name}, {desc}, {cat}, {url}
      			)
	      		"""
      		).on(
      			'locId   	-> place.locId,
      			'name 	    -> place.name,
      			'desc	   	-> place.description,
      			'cat	    -> place.category,
      			'url 		-> place.url
      		).executeInsert()
    	} match {
	        case Some(long) => new Id[Long](long) // The Primary Key
	        case None       => throw new Exception("SQL Error - Did not insert Place.")
    	}
	} // End - create
	
	/**
	 * Retrieve an Place by id.
	 */
	def byId(id: Long): Option[Place] = {
		DB.withConnection { implicit connection =>
	      	SQL("""
	      	    select * from place where id = {id}
	      	    """).on(
	  			'id -> id
			).as(Place.simple.singleOpt)
	    }
	}
	
	def byLatLonRadius(lat: Double, lon: Double, radius: Int) : List[Place] = {
	    println("Place.byLatLonRadius - TOP - lat: " + lat + " | lon: " + lon + " | radius: " + radius)
	    
	    DB.withConnection { implicit connection =>
	      	val sql = SQL(
	      		"""
				SELECT * from place
				where place.loc_id in
				(
					SELECT id from location
					where ST_DWithin(
		    			geoloc,
		    			(
		        			ST_GeographyFromText('SRID=4326;POINT(""" + lon + """ """ + lat + """)')
		    			),
  	        			{radius}
					)
				)   
	      	    """
	      	).on(
      			"radius" -> radius
	      	)
	      	println("Place.byLatLonRadius - sql: " + sql)
	      	val result = sql.as(Place.simple *)
	      	println("Place.byLatLonRadius - result: " + result)
	      	result
	    }
	}
}