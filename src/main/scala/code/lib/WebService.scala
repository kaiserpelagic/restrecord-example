package code.lib

import net.liftweb.common.{Box, Empty}
import net.liftweb.json.JsonAST.{JValue, JObject, render}
import net.liftweb.json.{Printer}

import dispatch._
import dispatch.Request._
import liftjson._
import Js._

import scala.xml._


import WebServiceHelpers._


class WebService(request: Request) extends Request(request) {
 
  def this(hostname: String) = this(:/(hostname))

  def this(hostname: String, port: Int, context: String) = this(:/(hostname, port) / context)

  def this(hostname: String, port: Int, context: String, head: Map[String, String]) = 
    this(:/(hostname, port) / context <:< head)
  
  def apply(path: List[String]) = new WebService(this / buildPath(path))
  
  def apply(path: List[String], params: Map[String, String]) = 
    new WebService(this / buildPath(path) <<? params)


  /** JSON Handlers */

  def findJS = this ># identity
  
  def createJS(body: JObject) = this << jobjectToString(body) ># identity 
  
  def saveJS(body: JObject) = this <<< jobjectToString(body) ># identity

  def deleteJS = this.DELETE ># identity


  /** XML Handlers */

  def findXML = this <> { xml => xml }
    
  def saveXML(body: NodeSeq) = this <<< body.toString <> { xml => xml }  
  
  def createXML(body: NodeSeq) = this << body.toString <> { xml => xml }
  
  def deleteXML = this.DELETE <> { xml => xml }


  /** Form Handlers */

  def createFORM(body: String) = this <<< body <> { xml => xml }
  
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
