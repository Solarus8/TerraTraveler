package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
      println("Application.index - TOP")
	  Ok(views.html.index("Terra Traveler app is ready."))
  }

}