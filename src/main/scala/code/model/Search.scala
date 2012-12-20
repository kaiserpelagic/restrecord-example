package code.model

import net.liftweb.record.field._
import net.liftweb.http.js._ 
import net.liftweb.json._
import net.liftweb.common._

import net.liftmodules.restrecord._

class Statuses extends JSONRecord[Statuses] {
  def meta = Statuses

  object text extends OptionalStringField(this, Empty)
}

object Statuses extends Statuses with JSONMetaRecord[Statuses] {
  override def ignoreExtraJSONFields: Boolean = true
  override def needAllJSONFields: Boolean = false 
}

class Search extends RestRecord[Search] {
  def meta = Search

  override val uri = "search" :: "tweets.json" :: Nil
  
  object statuses extends JSONSubRecordArrayField(this, Statuses)
}

object Search extends Search with RestMetaRecord[Search] { }
