Simple imgur browser application with Vaadin
======================

Technologies
-----------

* Vaadin 7
* Java EE 6
* Apache CXF (http://cxf.apache.org/) or RESTEasy (http://www.jboss.org/resteasy)
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

http://localhost:8080/fotorest-vaadin

