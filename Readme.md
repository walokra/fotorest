Simple imgur browser application with Wicket
======================

Technologies
-----------

* Wicket 6
* Java EE 6
* Spring 3.2 (for CDI)
* Apache CXF 2.7
* GSON

* App server: WebLogic 11g (10.3.6)

Prerequirements
---------------

To use imgur API you need to register your app: https://api.imgur.com/oauth2/addclient

* Fill in the values client.id and client.secret in runtime.properties

Run
---------------
From the terminal: mvn tomee:run
Or directly from Eclipse with TomEE

http://localhost:8080/fotorest-wicket
