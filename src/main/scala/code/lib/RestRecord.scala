package code.lib

import net.liftweb.common.{Box, Full, Empty}
import net.liftweb.record.{MetaRecord, Record}
import net.liftweb.json.JsonAST.{JValue, JObject, JBool, JField, JString}

import dispatch._
import com.ning.http.client.{RequestBuilder}

object RestWebService {
  /** Default WebService for the application **/
  def req: RequestBuilder = host("localhost")
  var defaultWebService = new WebService(req)
}

trait RestRecordPK[MyType <: RestRecord[MyType]]
  extends RestRecord[MyType] {

  self: MyType =>

  /** Refine meta to require a RestMetaRecordPK */
  def meta: RestMetaRecord[MyType]

  /** Defines and uri identifier for this resource -- /foo/:id or /foo/:id/bar */
  def defaultIdValue: Box[String]
  
  /** Defines the RESTful suffix for endpoint -- /foo/bar or /foo/:id/bar */
  val uriSuffix: List[String] = Nil

  def buildUriWithId(idBox: Box[String]) = idBox match {
    case Full(id) => uri ::: List(id) ::: uriSuffix
    case _        => uri ::: uriSuffix
  }

  override def buildUri: List[String] = buildUriWithId(defaultIdValue) 

  override def buildUri(id: String): List[String] = buildUriWithId(Full(id)) 
}


trait RestRecord[MyType <: RestRecord[MyType]] extends JSONRecord[MyType] {

  self: MyType =>

  /** Refine meta to require a RestMetaRecord */
  def meta: RestMetaRecord[MyType]

  /** Defines the RESTful endpoint for this resource -- /foo */
  val uri: List[String]

  def buildUri: List[String] = uri

  def buildUri(id: String) = uri ::: List(id)

  /* aliases, override if you need spefific endpoints for create, delete, or update */
  def saveEndpoint = buildUri

  def createEndpoint = buildUri

 // def create: Box[JValue] = meta.create(this)

 // def save: Box[JValue] = meta.save(this)
   
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
