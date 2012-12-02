Rest Record
===========

A Lift Record interface for RESTful apis

Uses <a href="http://dispatch.databinder.net/Dispatch.html">Databinder Dispatch's </a><a href="https://github.com/AsyncHttpClient/async-http-client">async-http-client</a> for NIO transacations, so there's no more blocking while waiting on api calls

## Setup and Configuration

### Geting Rest Recort

Add dependency to your project description:
```scala
 val restrecord "net.liftmodules" %% "restrecord" % (liftVersion + "-1.1") % "XXX"
```

Configure the api endpoint in Boot.scala by setting the url var in RestWebSerice. Here we are using Twitter's search
api. This can be overriden later if you need a spefic Record to hit a different endpoint. The default is "localhost". 

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
* HTTP failures are captured in the Box as a Failure("network error", SomeError(HTTPException404), Empty)
  * The caller is responsible for handling Failures (including networking and json parsing) 

### Creating a Record (POST)
```scala
MyRecord.create
```

### Updating a Record (PUT)
```scala
MyRecord.save
```

### Deleting a Record (DELETE)
```scala
MyRecord.delete
```


