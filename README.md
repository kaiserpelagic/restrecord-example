Rest Record
===========

A Lift Record interface for RESTful apis

Uses Dispath 0.9.4's async-http-client for NIO transacations,
so there's no more blocking while waiting on api calls


<h2>How To Use</h2>

<h3>Finding a Record (GET)</h3>

<div><code>MyRecord.find(3)</code></div>
<div><code>MyRecord.find(3, ("foo", "bar"))</code></div>
<div><code>MyRecord.find(("foo", "bar"), ("baz, laraz"))</code></div>
</br>
<div>Find returns a -> <code>Promise[Box[BaseRecord]]</code></div>
<div>Http failures are captured in the Box Promise[Failure("error", Http 404, Empty)]</div>

<h3>Creating a Record (POST)</h3>
<div><code>MyRecord.create</code></div>

<h3>Updating a Record (PUT)</h3>
<div><code>MyRecord.save</code></div>

<h3>Deleting a Record (DELETE)</h3>
<div><code>MyRecord.delete</code></div>

<h3>Creating a Record</h3>

'''scala
import net.liftmodules.restrecord.x
class Search extends RestRecord[Search] {
  def meta = Search

  override val uri = "search.json" :: Nil
      
  object results extends JSONSubRecordArrayField(this, SearchResult)
}

object Search extends Search with RestMetaRecord[Search] { }
'''

