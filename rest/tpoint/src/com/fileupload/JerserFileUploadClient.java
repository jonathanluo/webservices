package com.fileupload;

import java.io.File;
import java.nio.charset.Charset;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.json.JSONObject;


/**
 * https://stackoverflow.com/questions/24637038/jersey-2-multipart-upload-client
 * https://gist.github.com/damianmcdonald/f4ccc7805305daf5691f
 * 
 * 1) starts django rest fileupload service
 *    cd ~/workspace/github/drachen/django/django-rest-fileupload
 *    . ~/venv/bin/activate
 *    python3 manage.py runserver
 *
 * 2) JerserFileUploadClient > Run As > java Application
 *    201 Created InboundJaxrsResponse{context=ClientResponse{method=POST, uri=http://localhost:8000/file/upload/, status=201, reason=Created}}
 */
public class JerserFileUploadClient {

    private static final String TARGET_URL = "http://localhost:8000/file/upload/";

    public JerserFileUploadClient(File fileToUpload) {
        Client client = ClientBuilder.newBuilder()
            .register(MultiPartFeature.class).build();
        WebTarget webTarget = client.target(TARGET_URL);

//        MultiPart multiPart = new MultiPart();
//        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file",
            fileToUpload,
            MediaType.APPLICATION_OCTET_STREAM_TYPE);
//        multiPart.bodyPart(fileDataBodyPart);
        fileDataBodyPart.setContentDisposition(
                FormDataContentDisposition.name("file")
                .fileName(fileToUpload.getName()).build());

        // some json to send to the server as an element of the multi part request
        final JSONObject jsonToSend = new JSONObject();
        jsonToSend.put("character", "Jabba the Hutt");
        jsonToSend.put("movie", "Return of the Jedi");
        jsonToSend.put("isGoodGuy", false);

//FormDataMultiPart entity = new FormDataMultiPart();
//        entity..addPart("remark", new StringBody("{\"image 2018\"}", Charset.forName("UTF-8")));

        /* create the MultiPartRequest with:
         * Text field called "description"
         * JSON field called "characterProfile"
         * Text field called "filename"
         * Binary body part called "file" using fileDataBodyPart
         */
        final MultiPart multiPart = new FormDataMultiPart()
                .field("remark", "Picture to be upload", MediaType.TEXT_PLAIN_TYPE)
                //.field("characterProfile", jsonToSend, MediaType.APPLICATION_JSON_TYPE)
                .field("filename", fileToUpload.getName(), MediaType.TEXT_PLAIN_TYPE)
                .bodyPart(fileDataBodyPart);
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

        // POST request final
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(multiPart, multiPart.getMediaType()));

        System.out.println(response.getStatus() + " "
            + response.getStatusInfo() + " " + response);
    }

    public static void main(String[] args) {
        new JerserFileUploadClient(new File("/home/moonwave/Pictures/track-and-field.jpg"));
        new JerserFileUploadClient(new File("/home/moonwave/Pictures/pydev-run-1.png"));
        new JerserFileUploadClient(new File("/home/moonwave/Pictures/pydev-run-2.png"));
    }
}
