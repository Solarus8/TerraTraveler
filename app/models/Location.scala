package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Location (
    id: 	    Pk[Long] = NotAssigned,
    city:		Option[String],
    country:	Option[String],
    addr1:		Option[String],
    addr2:		Option[String],
    addr3:		Option[String],
    postalCode: Option[String],
    lat:		Double,
    lon:		Double,
    alt:		Option[Int],
    desc:		Option[String],
    url:		Option[String]
)

object Location {
	
	// -- Parsers
  
	/**
	 * Parse a Location from a ResultSet
	 */
	val simple = {
		get[Pk[Long]]("location.id") ~
		get[Option[String]]("location.city") ~
		get[Option[String]]("location.country") ~
		get[Option[String]]("location.address_1") ~
		get[Option[String]]("location.address_2") ~
		get[Option[String]]("location.address_3") ~
		get[Option[String]]("location.postal_code") ~
		get[Double]("location.latitude") ~
		get[Double]("location.longitude") ~
		get[Option[Int]]("location.altitude") ~
		get[Option[String]]("location.desc") ~
		get[Option[String]]("location.url") map {
		  	case id ~ city ~ country ~ addr1 ~ addr2 ~ addr3 ~ postalCode ~ lat ~ lon ~ alt ~ desc ~ url =>
		  	  	Location(id, city, country, addr1, addr2, addr3, postalCode, lat, lon, alt, desc, url)
		}
	}
	
	def create(loc: Location): Pk[Long] = {
		DB.withConnection { implicit connection =>
	      	SQL(
	      		"""
	      	    insert into location (city, country, address_1, address_2, address_3, postal_code, latitude, longitude, altitude, "desc", url) values (
      				{city}, {country}, {addr1}, {addr2}, {addr3}, {postalCode}, {lat}, {lon}, {alt}, {desc}, {url}
      			)
	      	    """
	      	).on(
	      	      'city   		-> loc.city,
	      	      'country		-> loc.country,
	      	      'addr1		-> loc.addr1,
	      	      'addr2		-> loc.addr2,
	      	      'addr3		-> loc.addr3,
	      	      'postalCode	-> loc.postalCode,
	      	      'lat			-> loc.lat,
	      	      'lon			-> loc.lon,
	      	      'alt			-> loc.alt,
	      	      'desc			-> loc.desc,
	      	      'url			-> loc.url
	      	).executeInsert()
    	} match {
	        case Some(long) => new Id[Long](long) // The Primary Key
	        case None       => throw new Exception("SQL Error - Did not insert Location.")
    	}
	} // End - create
	
	// Read - yields single location per id
	def single(id: Long): Option[Location] = DB.withConnection { implicit c =>
	    SQL("select * from location where id = {id}").on(
	        'id -> id
	    ).as(Location.simple.singleOpt)
	}
	
	/**
	 * Retrieve all locations.
	 * TODO: This is an impractical function and WILL NOT SCALE ###############
	 */
	def findAll: List[Location] = {
	    DB.withConnection { implicit connection =>
	      	SQL(
	      	    """
	      	    select * from "user"
      			"""
	      	).as(Location.simple *)
	    }
	}
	
	/**
	 * Retrieve a Location by id.
	 */
	def findById(id: Long): Option[Location] = {
		DB.withConnection { implicit connection =>
	      	SQL("""
	      	    select * from Location where id = {id}
	      	    """).on(
	  			'id -> id
			).as(Location.simple.singleOpt)
	    }
	}
}




