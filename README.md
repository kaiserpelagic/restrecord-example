Rest Record
===========

A Lift Record interface for RESTful apis

Uses Dispath 0.9.4's async-http-client for NIO transacations

<h2>How To Use<h2>

Finding a Record
with id
<code>MyRecord.find(3)</code>  returns a Promise[Box[BaseRecord]]

Http failure is contained in the Box Failure Promise[Failure("error", Http 404", Empty)

with id and query params
MyRecord.find(3, ("q", "greg"))



