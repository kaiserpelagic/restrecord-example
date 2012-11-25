package code.lib

import net.liftweb.common.{Box, Full, Empty}
import net.liftweb.record.{MetaRecord, Record}
import net.liftweb.json.JsonAST.{JValue, JObject, JBool, JField, JString}

import dispatch.{host, Promise}
import com.ning.http.client.{RequestBuilder}

object RestWebService {
  var url = "localhost"
  
  def req: RequestBuilder = host(url)
  def defaultWebService = new WebService(req)
}

trait RestRecord[MyType <: RestRecord[MyType]] extends JSONRecord[MyType] {

  self: MyType =>
  
  /** Refine meta to require a RestMetaRecord */
  def meta: RestMetaRecord[MyType]

  /** Defines the RESTful endpoint for this resource -- /foo */
  val uri: List[String]
  
  /** Defines the RESTful suffix for endpoint -- /foo/:id/bar */
  val uriSuffix: List[String] = Nil

  /** Defines and uri identifier for this resource -- /foo/:id or /foo/:id/bar */
  def idPK: Box[String] = Empty

  def buildUri: List[String] = uri ::: uriSuffix 
  
  def buildUri(id: String): List[String] = uri ::: List(id) ::: uriSuffix 

  def buildUri(box: Box[String]): List[String] = box.map(buildUri(_)) openOr buildUri 

  def create: Promise[Box[JValue]] = meta.create(this)

  def save: Promise[Box[JValue]] = meta.save(this)
  
  //def delete: Box[JValue] = meta.delete(this)
  
  def createEndpoint = buildUri

  def saveEndpoint = buildUri(idPK)

  def deleteEndpoint = buildUri(idPK)

  /** override this method to handle api specific POST / PUT / DELETE responses **/
  def handleResponse[JValue](json: JValue): Box[JValue] = Full(json)

  // override this if you want to change this record's webservice
  def webservice = RestWebService.defaultWebService
}
