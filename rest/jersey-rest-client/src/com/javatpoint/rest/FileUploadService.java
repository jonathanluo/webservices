package com.javatpoint.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
   https://www.javatpoint.com/jax-rs-file-upload-example
   web.xml
     <servlet-mapping>
       <servlet-name>Jersey RESTful Application</servlet-name>
       <url-pattern>/rest/*</url-pattern>
     </servlet-mapping>

   upload.xml
   <form action="rest/files/upload" method="post" enctype="multipart/form-data">
   ...
   </form>
 */
@Path("/files")  
public class FileUploadService {
    @POST  
    @Path("/upload")  
    @Consumes(MediaType.MULTIPART_FORM_DATA)  
    public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
	                           @FormDataParam("file") FormDataContentDisposition fileDetail) {

    	String fileLocation = "/home/moonwave/Downloads/" + fileDetail.getFileName();
	    //saving file  
	    try {
	        FileOutputStream out = new FileOutputStream(new File(fileLocation));
	        int read = 0;
	        byte[] bytes = new byte[1024];
	        out = new FileOutputStream(new File(fileLocation));
	        while ((read = uploadedInputStream.read(bytes)) != -1) {
	            out.write(bytes, 0, read);
	        }
	        out.flush();
	        out.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    String output = "File successfully uploaded to : " + fileLocation;
        return Response.status(200).entity(output).build();
    }
}
