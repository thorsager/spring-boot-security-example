Spring Boot - Security Example
==============================
This repos is actually just some test-code I have written, to mess about with
_security_ when using spring boot.

The setup
---------
`User(s)`, `Role(s)` and `Privilege(s)` are all stored in an H2 database, using
JPA (spring-data). Which allows for creation of Users and assignment of Roles
and Privileges can be done at runtime.

This example
------------
This example does not have much in the way of functionality, as it is the security
part I'm messing with. In time I _SHOULD_ add functions to create, update and 
delete users.

>for now all is set up when the application is created, credentials and keys are 
logged.

**_Features:_**
- `/` - A simple 'hello world' page with no bells
- `/rest` - Some simple endpoints, authenticated by the session based `formLogin`
- `/api` - Some very simple endpoints, authenticated by header `X-Auth-Token`.
  Tokens are pr. user, and `@PreAuthorize` can be used in the same was ase if the
  user was authenticated by `formLogin`.

