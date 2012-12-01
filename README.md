Rest Record
===========

A Record interface for RESTFul apis

Uses Dispath 0.9.4's async-http-client for NIO http transacations. 

Example
Creating a Twitter client to search for tweets: 

class Search extends RestRecord[Search] {
  def meta = Search

  override val uri = "search.json" :: Nil
  
  object results extends JSONSubRecordArrayField(this, SearchResult)
}

object Search extends Search with RestMetaRecord[Search] { }

class SearchResult extends JSONRecord[SearchResult] {
  def meta = SearchResult

  object text extends OptionalStringField(this, Empty)
}

object SearchResult extends SearchResult with JSONMetaRecord[SearchResult] {
  override def ignoreExtraJSONFields: Boolean = true
  override def needAllJSONFields: Boolean = false 
}
