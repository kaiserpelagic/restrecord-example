package code.lib

import net.liftweb._
import util._
import common._
import Helpers._
import record.{MetaRecord, Record}
import couchdb.{JSONMetaRecord}
import http.js.{JsExp, JE, JsObj}
import json.JsonAST._

import dispatch._


trait RestMetaRecord[BaseRecord <: RestRecord[BaseRecord]] extends JSONMetaRecord[BaseRecord] {
  self: BaseRecord =>
  
  val http: Http = Http 

  def find(query: (String, String)*): Box[BaseRecord] = 
    findFrom(webservice, buildUri, query: _*)
  
  def find(id: String, query: (String, String)*): Box[BaseRecord] = 
    findFrom(webservice, buildUri(id), query: _*)

  def findFrom(svc: WebService, path: List[String], query: (String, String)*) =
    tryo(withHttp(http, svc(path, Map(query: _*)) findJS)).flatMap(fromJValue)

  def create(inst: BaseRecord): Box[JValue] = 
    createFrom(inst, inst.webservice)

  def createFrom(inst: BaseRecord, svc: WebService): Box[JValue] = { 
    foreachCallback(inst, _.beforeCreate)
    try { 
      inst.handleResponse(withHttp(http, svc(inst.createEndpoint) createJS(inst.asJValue)))
    } finally {
      foreachCallback(inst, _.afterCreate)
    }
  }

  def save(inst: BaseRecord): Box[JValue] = {
    saveFrom(inst, inst.webservice)
  }

  def saveFrom(inst: BaseRecord, svc: WebService): Box[JValue] = {
    foreachCallback(inst, _.beforeSave)
    try {
      inst.handleResponse(withHttp(http, svc(inst.saveEndpoint) saveJS(inst.asJValue)))
    } finally {
      foreachCallback(inst, _.afterSave)
    }
  }

  def withHttp[T](h: Http, hand: Handler[T]): T = {
    h x (hand) {
      case(code, _, _, out) =>  out()
    }
  }
  
}
