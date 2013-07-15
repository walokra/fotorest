Simple imgur browser application with JSF2
======================

Technologies
-----------

* JSF 2.0
* PrimeFaces 3.5 (http://www.primefaces.org/)
* Java EE 6
* Spring 3.2 (for CDI)
* Apache CXF 2.7
* GSON

* App server: WebLogic 11g (10.3.6)
	* See http://docs.oracle.com/cd/E21764_01/web.1111/e13712/configurejsfandjtsl.htm for configurin WLS for JSF 2.0
	* In WLS Admin Console deploy jsf-2.0.war and jstl-1.2.war as libraries

Prerequirements
---------------

To use imgur API you need to register your app: https://api.imgur.com/oauth2/addclient

* Fill in the values client.id and client.secret in runtime.properties

Run
---------------
From the terminal: mvn tomee:run
Or directly from Eclipse with TomEE

http://localhost:8080/fotorest-jsf2-pf
