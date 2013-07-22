package code.model

import net.liftweb.record.field._
import net.liftweb.http.js._ 
import net.liftweb.json._
import net.liftweb.common._

import net.liftmodules.restrecord._
import com.ning.http.client.oauth._
   
trait TwitterConfig {
  // you need to provide your twitter keys and secrets
  val consumerKey = new ConsumerKey("", "")
  val tolken = new RequestToken("", "")

  val configuration = RestRecordConfig(
    "api.twitter.com",
    Empty,
    Full("1.1"),
    true, 
    true,
    Full(consumerKey),
    Full(tolken)
  )
}


/*****************************************************************
 **  SEARCH
 *****************************************************************/

class Search extends RestRecord[Search] {
  def meta = Search

  // defines the search resource endpoint
  override val uri = "search" :: "tweets.json" :: Nil

  object statuses extends JSONSubRecordArrayField(this, Statuses)
}

object Search extends Search with RestMetaRecord[Search] with TwitterConfig 


/*****************************************************************
 **  STATUS
 *****************************************************************/

class Statuses extends RestRecord[Statuses] {
  def meta = Statuses

  override val uri = "statuses" :: "show" :: Nil

  // Defines the id in the resource path.
  // This will be used on Save and Deletes if the Box is Full
  // Twitter requires ".json" after the id even though they only respond with json !!!
  override def idPk = Full((id_str.is + ".json"))

  object id_str extends StringField(this, "")
  object text extends OptionalStringField(this, Empty)
}

object Statuses extends Statuses with RestMetaRecord[Statuses] with TwitterConfig
