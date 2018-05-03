Welcome to Earnit-api.
===================

Java based api for Earnit.

Build
-------------

* To build a deployable war file : 
> ``` $ ./mvnw clean package ``` .



Deploy
-------
* To deploy the war file to a standalone tomcat server
> Copy the `earnit-api.war` generated on the target folder to 
> $CATALINA_HOME/webapps

* To deploy on spring-boot embedded tomcat server

>``` $ ./mvnw spring-boot:run```

Verify Deployment
-----------------
* Use the test api
> ```$ curl localhost:8080/earnit-api/hello```
> 
> OR
> 
> ```$ curl localhost:8080/hello```
