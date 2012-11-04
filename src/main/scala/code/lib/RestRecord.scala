package code.lib

import net.liftweb.couchdb.{JSONRecord}
import net.liftweb.common.{Box, Full, Empty}
import net.liftweb.record.{MetaRecord, Record}
import net.liftweb.json.JsonAST.{JValue, JObject, JBool, JField, JString}

import dispatch._
import dispatch.Request._
import liftjson._
import Js._

object RestWebService {
  /** Default WebService for the application **/
  var defaultWebService = new WebService("localhost", 8080, "",
    Map("Accept" -> "application/json", "Content-type" -> "application/json"))
}

trait RestRecord[MyType <: RestRecord[MyType]] extends JSONRecord[MyType] {

  self: MyType =>

  /** Refine meta to require a RestMetaRecord */
  def meta: RestMetaRecord[MyType]

  /** Defines the RESTful endpoint for this resource -- /foo */
  val uri: List[String]
  
  /** Defines the RESTful suffix for endpoint -- /foo/bar or /foo/:id/bar */
  val uriSuffix: List[String] = Nil

  /** Defines and uri identifier for this resource -- /foo/:id or /foo/:id/bar */
  def id: Box[String] = Empty

  def buildUri(id: String): List[String] = _buildUri(Full(id))

  def buildUri: List[String] = _buildUri(id)

  private def _buildUri(ident: Box[String]): List[String] = ident match {
    case Full(x) => uri ::: List(x) ::: uriSuffix
    case _ => uri ::: uriSuffix
  }

  def create: Box[JValue] = meta.create(this)

  def save: Box[JValue] = meta.save(this)
   
  /** override this method to handle api specific POST / PUT / DELETE responses **/
  def handleResponse[JValue](json: JValue): Box[JValue] = Full(json)

  /** The webservice this record is associated with */
  private var _webservice: Box[WebService] = Empty

  def webservice: WebService = _webservice match {
    case Full(webservice) => webservice
    case _ => {
      _webservice = Full(_discoverWebService)
      webservice
    }
  }
  
  def _discoverWebService = RestWebService.defaultWebService
}
