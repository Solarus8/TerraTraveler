package models

import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current

/*
 * THIS IS A TEMP CLASS AND WILL GO AWAY.
 * JUST HERE TO ESTABLISH PROJECT TEMPLATE.
 */

case class Task(id: Long, label: String) {}

object Task {
  
  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task").as(task *)
  }
  
  def create(label: String) {
    DB.withConnection { implicit c =>
      SQL("insert into task (label) values ({label})").on(
    	'label -> label
      ).executeUpdate()
    }
  }
  
  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from task where id = {id}").on(
    	'id -> id
      ).executeUpdate()
    }
  }
  
  val task = {
    get[Long]("id") ~
    get[String]("label") map {
      case id~label => Task(id, label)
    }
  }
  
}