package controllers

import models.Task // This will go away

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._

object Application extends Controller {

	/* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 * All code between these lines is TEMP (from ToDo tutorial) used to establish 
	 * project template. It will be replaced by TerraTraveler code.
	 */
	 
	def index = Action {
		Redirect(routes.Application.tasks)
	}
  
	def tasks = Action {
		Ok(views.html.index(Task.all(), taskForm))
	}

	def newTask = Action { implicit request =>
	    taskForm.bindFromRequest.fold(
			errors => BadRequest(views.html.index(Task.all(), errors)),
			label  => {
				Task.create(label)
				Redirect(routes.Application.tasks)
			}
	    )
	}
  
	def deleteTask(id: Long) = Action {
		Task.delete(id)
		Redirect(routes.Application.tasks)
	}
  
	val taskForm = Form {
		"label" -> nonEmptyText
	}
	
	/* <<<<<<<<<<<<<<<<<<<<<<<< End 'ToDo' TEMP code <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

}