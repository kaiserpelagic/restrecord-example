package net.liftmodules.restrecord

import net.liftweb.util._
import net.liftweb.common._
import Helpers._
import net.liftweb.json.JsonAST._
import net.liftweb.http.js.{JsExp, JE, JsObj}
import net.liftweb.record.{MetaRecord, Record}

import dispatch._
import com.ning.http.client.{RequestBuilder, Request}


trait RestMetaRecord[BaseRecord <: RestRecord[BaseRecord]] extends JSONMetaRecord[BaseRecord] {
  self: BaseRecord =>
  
  val http = Http 

  def find(query: (String, String)*): Promise[Box[BaseRecord]] =
    findFrom(webservice, buildUri, query: _*)

  def find(id: String, query: (String, String)*): Promise[Box[BaseRecord]] = 
    findFrom(webservice, buildUri(id), query: _*)

  def findFrom(svc: WebService, path: List[String], 
    query: (String, String)*): Promise[Box[BaseRecord]] = {
   
   withHttp(http, svc(path, Map(query: _*)) find, fromJValue)
  }

  def create(inst: BaseRecord): Promise[Box[JValue]] = 
    createFrom(inst, inst.webservice)

  def createFrom(inst: BaseRecord, svc: WebService): Promise[Box[JValue]] = { 
    foreachCallback(inst, _.beforeCreate)
    try {
      withHttp(http, svc(inst.createEndpoint) createJS(inst.asJValue), inst.handleResponse)
    } finally {
      foreachCallback(inst, _.afterCreate)
    }
  }

  def save(inst: BaseRecord): Promise[Box[JValue]] = 
    saveFrom(inst, inst.webservice)

  def saveFrom(inst: BaseRecord, svc: WebService): Promise[Box[JValue]] = {
    foreachCallback(inst, _.beforeSave)
    try {
      withHttp(http, svc(inst.saveEndpoint) saveJS(inst.asJValue), inst.handleResponse)
    } finally {
      foreachCallback(inst, _.afterSave)
    }
  }

  def withHttp[T](h: Http, body: (Request, OkFunctionHandler[JValue]), 
    handle: JValue => Box[T]): Promise[Box[T]] = {
    
    h(body).either map {
      case Right(v) => handle(v)
      case Left(e) => Failure("error", Full(e), Empty)
    }
  }
}
