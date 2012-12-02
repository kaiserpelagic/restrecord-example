Rest Record
===========

A Lift Record interface for RESTful apis

Uses Dispath 0.9.4's async-http-client for NIO transacations,
so there's no more blocking while waiting on api calls

<h2>Setup and Configuration</h2>

Configure the api endpoint base in Boot.scala.

```scala
object Boot.scala {
  etc ...
   
  RestWebService.url = "search.twitter.com"
}
```

<h2>Implementing A Record</h2>

```scala
import net.liftmodules.restrecord._

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
  // we aren't going to serialize the entire Twitter response 
  // so we want to be flexible about parsing
  override def ignoreExtraJSONFields: Boolean = true
  override def needAllJSONFields: Boolean = false 
}

```

<h3>Finding a Record (GET)</h3>

```scala
  MyRecord.find(3)
  MyRecord.find(3, ("foo", "bar"))
  MyRecord.find(("foo", "bar"), ("baz, laraz"))
```
<div>Find returns a: <code>Promise[Box[BaseRecord]]</code></div>
<div>HTTP failures are captured in the Box as a Failure("error", Http 404, Empty). 
The caller can decide what should happen in case of network failure</div>

<h3>Creating a Record (POST)</h3>
<div><code>MyRecord.create</code></div>

<h3>Updating a Record (PUT)</h3>
<div><code>MyRecord.save</code></div>

<h3>Deleting a Record (DELETE)</h3>
<div><code>MyRecord.delete</code></div>


