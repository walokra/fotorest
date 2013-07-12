Simple imgur browser application with Vaadin
======================

Technologies
-----------

* Vaadin 7
* Java EE 6
* RESTEasy
* GSON

Prerequirements
---------------

To use imgur API you need to register your app: https://api.imgur.com/oauth2/addclient
* Fill in the values client.id and client.secret in runtime.properties

Run
======================
mvn install tomee:run

http://localhost:8080/fotorest-0.1-SNAPSHOT

Or directly from Eclipse with TomEE

http://localhost:8080/fotorest
