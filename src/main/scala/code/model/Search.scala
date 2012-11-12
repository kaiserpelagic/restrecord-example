package code.model

import net.liftweb.record.field._
import net.liftweb.http.js._ 
import net.liftweb.json._
import net.liftweb.common._

import code.lib._

class SearchResult extends JSONRecord[SearchResult] {
  def meta = SearchResult

  object text extends OptionalStringField(this, Empty)
}

object SearchResult extends SearchResult with JSONMetaRecord[SearchResult] {
  override def ignoreExtraJSONFields: Boolean = true
  override def needAllJSONFields: Boolean = false 
}

class Search extends RestRecord[Search] {
  def meta = Search

  override val uri = "search.json" :: Nil
  
  object results extends JSONSubRecordArrayField(this, SearchResult)
}

object Search extends Search with RestMetaRecord[Search] { }
