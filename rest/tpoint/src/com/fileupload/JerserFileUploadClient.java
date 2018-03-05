package com.fileupload;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

/**
 * https://stackoverflow.com/questions/24637038/jersey-2-multipart-upload-client
 *
 * 1) starts django rest fileupload service
 *    cd ~/workspace/github/drachen/django/django-rest-fileupload
 *    . ~/venv/bin/activate
 *    python3 manage.py runserver
 *
 * 2) JerserFileUploadClient > Run As > java Application
 */
public class JerserFileUploadClient {

    private static final String TARGET_URL = "http://localhost:8000/file/upload/";

    public JerserFileUploadClient() {
        Client client = ClientBuilder.newBuilder()
            .register(MultiPartFeature.class).build();
        WebTarget webTarget = client.target(TARGET_URL);
        MultiPart multiPart = new MultiPart();
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file",
            new File("/home/moonwave/Pictures/track-and-field.jpg"),
            MediaType.APPLICATION_OCTET_STREAM_TYPE);
        multiPart.bodyPart(fileDataBodyPart);

        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(multiPart, multiPart.getMediaType()));

        System.out.println(response.getStatus() + " "
            + response.getStatusInfo() + " " + response);
    }

    public static void main(String[] args) {
        new JerserFileUploadClient();
    }
}
