package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._
import views._

object Locations extends Controller with Secured {
  
	def index = IsAuthenticated { username => _ =>
	    User.findByEmail(username).map { user =>
	      	user.primaryLoc match {
	      	  	case Some(locId) => 
	      	  	  	Ok(html.protoLocation(
      	  	  			Location.single(locId),
      	  	  			user
  	  	  			)
  	  			)
	      	  	case _ => 
	      	  	  	Ok(html.protoLocation(
      	  	  			None,
      	  	  			user
  	  	  			)
  	  	  		)
	      	}
	    }.getOrElse(Forbidden)
	}
}


