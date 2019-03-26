package com.javatpoint.rest;
import java.io.File;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
    https://www.javatpoint.com/jax-rs-file-download-example
    web.xml
      <servlet-mapping>
        <servlet-name>Jersey RESTful Application</servlet-name>
        <url-pattern>/rest/*</url-pattern>
      </servlet-mapping>

    download.xml
     <html>
         <a href="rest/files/txt">Download Text File</a>
         <br/>
         <br/>
         <a href="rest/files/image">Download Image File</a>
     </html>

    You need to specify different content type to download different files. The @Produces annotation is used to specify 
    the type of file content.

    @Produces("text/plain"): for downloading text file.
    @Produces("image/png"): for downloading png image file.
    @Produces("application/pdf"): for downloading PDF file.
    @Produces("application/vnd.ms-excel"): for downloading excel file.
    @Produces("application/msword"): for downloading ms word file.

*/
@Path("/files")
public class ImageDownloadService {
    private static final String FILE_PATH = "/j01/srv/tomcat/webapps/docs/images/tomcat.png";
    @GET
    @Path("/image")
    @Produces("image/png") // for downloading png image file.
    public Response getFile() {
        File file = new File(FILE_PATH);
        ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition","attachment; filename=\"javatpoint_image.png\"");
        return response.build();
    }
 }