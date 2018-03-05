package com.fileupload;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

/**
 * https://howtodoinjava.com/jersey/jersey-file-upload-example/ 
 * Test file upload using java client
 * 
 * see also project jersey-rest-fileupload
 * 
 */
public class UploadFileTest {

    public static void main(String[] args) throws IOException {
        final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();

        final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("/home/moonwave/Pictures/track-and-field.jpg"));
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("remark", "core image").bodyPart(filePart);

        final WebTarget target = client.target("http://localhost:8000/file/upload/");
        final Response response = target.request().post(Entity.entity(multipart, multipart.getMediaType()));

        //Use response object to verify upload success
        System.out.println(response.getStatus() + " " + response.getStatusInfo() + " " + response);

        formDataMultiPart.close();
        multipart.close();
    }
}

/*
201 Created InboundJaxrsResponse{context=ClientResponse{method=POST, uri=http://localhost:8000/file/upload/, status=201, reason=Created}}

open ~/workspace/github/jon/webservices/rest/jersey-rest-client/db.sqlite3

table file_app_file 

id    file                          remark                 timestamp
"1"  "20180210_135850.jpg"         "test file upload"     "2018-02-23 06:45:00.327620"
"2"  "track-and-field.jpg"         "test file upload #2"  "2018-03-05 05:37:35.427048"
"3"  "track-and-field.jpg"         "Picture to be upload" "2018-03-05 06:21:54.347663"
"4"  "track-and-field.jpg"         "Picture to be upload" "2018-03-05 06:26:01.095827"
"5"  "pydev-run-1.png"             "Picture to be upload" "2018-03-05 06:26:01.294001"
"6"  "pydev-run-2.png"             "Picture to be upload" "2018-03-05 06:26:01.414246"
"7"  "track-and-field_ClefvLi.jpg" "Picture to be upload" "2018-03-05 06:39:42.721089"
"8"  "pydev-run-1_dZ6WiHu.png"     "Picture to be upload" "2018-03-05 06:39:43.001307"
"9"  "pydev-run-2_VMNXQRb.png"     "Picture to be upload" "2018-03-05 06:39:43.233302"
"10" "track-and-field.jpg"         "Picture to be upload" "2018-03-05 06:40:17.223578"
"11" "pydev-run-1.png"             "Picture to be upload" "2018-03-05 06:40:17.431705"
"12" "pydev-run-2.png"             "Picture to be upload" "2018-03-05 06:40:17.596441"
"13" "track-and-field_tTPwQw3.jpg" "core image"           "2018-03-05 07:25:48.654359"

*/
