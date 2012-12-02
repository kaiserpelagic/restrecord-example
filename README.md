Rest Record
===========

A Lift Record interface for RESTful apis

Uses <a href="http://dispatch.databinder.net/Dispatch.html">Dispath 0.9.X's </a><a href="https://github.com/AsyncHttpClient/async-http-client">async-http-client</a> for NIO transacations, so there's no more blocking while waiting on api calls

## Setup and Configuration

Configure the api endpoint in Boot.scala. Here we are using Twitter's search api. This can be override later on
if you a spefic Record to hit a different endpoint. If no url is specified the default is "localhost". 

```scala
object Boot.scala {
  etc ...
   
  RestWebService.url = "search.twitter.com"
}
```

## Implementing A Record

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

### Finding a Record (GET)

```scala
  Search.find(3) // search.twitter.com/search.json/3
  Search.find(3, ("foo", "bar")) // search.twitter.com/search.json/3?foo=bar 
  Search.find(("q", "liftweb"), ("baz, laraz")) //searh.twitter.com/search.json?q=liftweb&baz=larax
```
* Find returns a: <code>Promise[Box[BaseRecord]]</code>
  * In this case it's <code>Promise[Box[Search]]</cod>
* HTTP failures are captured in the Box as a Failure("error", Http 404, Empty)
  * The caller is responsible for all Failures (included network) 

### Creating a Record (POST)
<div><code>MyRecord.create</code></div>

### Updating a Record (PUT)
<div><code>MyRecord.save</code></div>

### Deleting a Record (DELETE)
<div><code>MyRecord.delete</code></div>


