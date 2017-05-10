package com.javatpoint.rest;
import java.io.File;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;


/**
 * https://www.javatpoint.com/jax-rs-file-download-example
 */
@Path("/files")
public class FileDownloadService {
	private static final String FILE_PATH = "/j01/srv/tomcat/bin/startup.sh";
	@GET
	@Path("/txt")
	@Produces("text/plain")
	public Response getFile() {
		File file = new File(FILE_PATH);

		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition","attachment; filename=\"javatpoint_file.txt\"");
		return response.build();
	}
}
