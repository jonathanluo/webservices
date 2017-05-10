1)
https://www.tutorialspoint.com/restful/restful_first_application.htm
https://www.tutorialspoint.com/restful/restful_methods.htm

Copy all jars from following directories of download jersey zip folder in WEB-INF/lib directory of the project.

cp /j01/sys/jaxrs-ri-2.25.1/api/*  ~/workspace/github/jon/webservices/rest/tpoint/WebContent/WEB-INF/lib
cp /j01/sys/jaxrs-ri-2.25.1/ext/*  ~/workspace/github/jon/webservices/rest/tpoint/WebContent/WEB-INF/lib
cp /j01/sys/jaxrs-ri-2.25.1//lib/* ~/workspace/github/jon/webservices/rest/tpoint/WebContent/WEB-INF/lib

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

2)
https://www.javatpoint.com/jax-rs-file-download-example
Test text/image files download
http://localhost:8080/UserManagement/download.html