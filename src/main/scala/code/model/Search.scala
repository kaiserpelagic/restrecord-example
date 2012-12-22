package code.model

import net.liftweb.record.field._
import net.liftweb.http.js._ 
import net.liftweb.json._
import net.liftweb.common._

import net.liftmodules.restrecord._

class Search extends RestRecord[Search] {
  def meta = Search

  // defines the search resource endpoint
  override val uri = "search" :: "tweets.json" :: Nil

  object statuses extends JSONSubRecordArrayField(this, Statuses)
}

object Search extends Search with RestMetaRecord[Search] { }

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

object Statuses extends Statuses with RestMetaRecord[Statuses] {
}
