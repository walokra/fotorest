Simple imgur browser application with JSF2
======================

Technologies
-----------

* JSF 2.1
* PrimeFaces (http://www.primefaces.org/)
* Java EE 6
* Apache CXF
* GSON

Prerequirements
---------------

To use imgur API you need to register your app: https://api.imgur.com/oauth2/addclient

* Fill in the values client.id and client.secret in runtime.properties

If using TomEE web profile

* Remove the scope of apache.cxf depencies as they don't come with web profile.

Run
---------------
From the terminal: mvn tomee:run
Or directly from Eclipse with TomEE

http://localhost:8080/fotorest-jsf2-pf

