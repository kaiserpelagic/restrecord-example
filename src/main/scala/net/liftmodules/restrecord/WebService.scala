package net.liftmodules.restrecord

import net.liftweb.common.{Box, Empty}
import net.liftweb.json.JsonAST.{JValue, JObject, render}
import net.liftweb.json.{Printer}

import dispatch._
import scala.xml._
import WebServiceHelpers._
import com.ning.http.client.{RequestBuilder}

object WebService {
  def apply(url: String) = new WebService(host(url))
}

class WebService(request: RequestBuilder) {

  def apply(path: List[String]) = 
    new WebService(request / buildPath(path))
  
  def apply(path: List[String], params: Map[String, String]) = 
    new WebService(request / buildPath(path) <<? params)

  /** JSON Handlers */

  def find = request.GET OK LiftJson.As
  
  def createJS(body: JObject) = request.POST.setBody(jobjectToString(body)) OK LiftJson.As 
  
  def saveJS(body: JObject) = request.PUT.setBody(jobjectToString(body)) OK LiftJson.As

  def deleteJS = request.DELETE OK LiftJson.As
  
  /** Convert a JObject into a String */
  private def jobjectToString(in: JObject): String = Printer.compact(render(in))
}


object WebServiceHelpers {
  def buildPath(path: List[String]): String = 
    if (!path.isEmpty)
      path.tail.foldLeft(path.head)(_ + "/" + _)
    else 
      ""
}
