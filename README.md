Rest Record
===========

A Lift Record interface for RESTful apis

Uses Dispath 0.9.4's async-http-client for NIO transacations,
so there's no more blocking while waiting on api calls


<h2>How To Use</h2>

Finding a Record (GET)

<div><code>MyRecord.find(3)</code><div>
<div><code>MyRecord.find(3, ("foo", "bar"))</code><div>
<div><code>MyRecord.find(("foo", "bar"), ("baz, laraz"))</code></div>

Find returns a <code>Promise[Box[BaseRecord]]</code> </br>
Http failures are captured in the Box Promise[Failure("error", Http 404, Empty)]

Creating a Record (POST)


Updating a Record (PUT)


Deleting a Record (DELETE)
