package controllers

import java.util.Date

import models._
import views._
import anorm.NotAssigned
import anorm._
import play.api.db._
import play.api.mvc._
import play.api._
import play.api.libs.json.Json._
import play.api.libs.json._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

object Journals extends Controller {
	
    def createJournal = Action { implicit request =>
	    request.body.asJson.map { json =>
	        println("Journals.createJournal - json: " + json)
	        
	        val tripId 		= (json \ "tripId").validate[Long]
	        val title 		= (json \ "title").validate[String]
	        val desc 		= (json \ "desc").validate[String]
	        val index 		= (json \ "index").validate[Int]
	        
	        val newJournal  = Journal(NotAssigned, tripId.get, title.get, desc.asOpt, index.asOpt)
	        val newJournalPK = Journal.create(newJournal)
	        			        
	        val persistedJournal = Journal.byId(newJournalPK.get)
	        
	        persistedJournal match {
	            case Some(persistedJournal) => {
	                val jsonResp = Json.obj( "journal" -> journalToJson(persistedJournal))
		            Ok(jsonResp)
	            }
	            case None => BadRequest("Journal not found")
	        }
	    }.getOrElse {
			BadRequest("Expecting Json data")
		}
	}
	
	def byId(id: Long) = Action { implicit request =>
	    Journal.byId(id) match { 
	        case Some(journal) => {
	             val journalJson = Json.obj(
	                 "journal" -> journalToJson(journal)   
	             )
	             Ok(journalJson)
	        }
	        case None => Ok(Json.obj("status" -> "None"))
	    }
	}
	
	def journalToJson(journal: Journal): JsObject = {
	    
	    Json.obj(
			"id"		-> journal.id.get,
			"tripId"   	-> journal.tripId,
			"title"     -> journal.title,
			"desc" 		-> journal.desc
		)
	}

}