Simple imgur browser application with JSF2
==========================================

Technologies
------------

* JSF 2.0
* PrimeFaces 3.5 (http://www.primefaces.org/)
* Java EE 6
* Spring 3.2 (for CDI)
* Apache CXF 2.7 (http://cxf.apache.org/)
* GSON

* App server: 
	* WebLogic 11g (10.3.6)
		* See http://docs.oracle.com/cd/E21764_01/web.1111/e13712/configurejsfandjtsl.htm for configurin WLS for JSF 2.0
		* In WLS Admin Console deploy jsf-2.0.war and jstl-1.2.war as libraries

Prerequirements
---------------

To use imgur API you need to register your app: https://api.imgur.com/oauth2/addclient

* Fill in the value client.id in runtime.properties

Running the application
-----------------------
* Download and install Maven 3 (http://maven.apache.org/download.html#Installation). If you 
  have already installed Maven 3, you can skip this step.
* Go the root directory of project (The one which contains the pom.xml file)
* Run command "mvn clean jetty:run" or run the app from Eclipse with TomEE web profile
* Start your browser and go to the location: http://localhost:8080/fotorest-jsf2-pf
