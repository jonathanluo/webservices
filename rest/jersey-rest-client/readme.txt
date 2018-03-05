2) https://www.javatpoint.com/jax-rs-file-download-example
   src/com/javatpoint contains
    FileDownloadService
    FileUploadService
    ImageDownloadService

    http://localhost:8080/UserManagement/download.html
        Download Text File 
        Download Image File

    http://localhost:8080/UserManagement/upload.html
        File Upload Example

2) https://www.tutorialspoint.com/restful/restful_first_application.htm
   https://www.tutorialspoint.com/restful/restful_methods.htm
   src/com/tutorialspoint

Download jersey from https://jersey.java.net/download.html
Download Download JAR file jersey-media-multipart 2.25.1 with all dependencies
    https://jar-download.com/explore-java-source-code.php?a=jersey-media-multipart&g=org.glassfish.jersey.media&v=2.25.1&downloadable=1

Copy all jars from following directories of download jersey zip folder in WEB-INF/lib directory of the project.

cp /j01/sys/jaxrs-ri-2.25.1/api/*  ~/workspace/github/jon/webservices/rest/jersey-rest-client/WebContent/WEB-INF/lib
cp /j01/sys/jaxrs-ri-2.25.1/ext/*  ~/workspace/github/jon/webservices/rest/jersey-rest-client/WebContent/WEB-INF/lib
cp /j01/sys/jaxrs-ri-2.25.1/lib/*  ~/workspace/github/jon/webservices/rest/jersey-rest-client/WebContent/WEB-INF/lib

Creating the WebContent/WEB-INF/Web.xml configuration File

Deploying the Program
    export war file from eclipse ide
    File → export → Web → War File

Copy UserManagement.war file to /j01/srv/tomcat/webapps

Start Tomcat
/j01/srv/tomcat/bin/startup.sh

Test rest services
http://localhost:8080/UserManagement/rest/UserService/users

<users>
  <user>
    <id>1</id>
    <name>Mahesh</name>
    <profession>Teacher</profession>
  </user>
</users>

http://localhost:8088/UserManagement/rest/UserService/users/json/1

{"id":"1","name":"suresh","profession":"clerk"}

Right click on WebServiceTester.java, Run As > Java Application
