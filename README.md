Rest Record
===========

A Lift Record interface for RESTful apis

Uses Dispath 0.9.4's async-http-client for NIO transacations,
so there's no more blocking on io for api calls

<h2>How To Use<h2>

Finding a Record

<code>MyRecord.find(3)</code>  
with id and query params
<code>MyRecord.find(3, ("foo", "bar"), )</code>
or
<code>MyRecord.find(("foo", "bar"), ("baz, laraz"))</code>

Returns a Promise[Box[BaseRecord]]
Http failures are captured in the Box Promise[Failure("error", Http 404", Empty)]


