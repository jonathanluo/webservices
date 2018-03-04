package net.javatutorial.tutorials.clienst;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

/**
 * This example shows how to upload files using POST requests 
 * with encryption type "multipart/form-data".
 *
 * For more details please read the full tutorial
 * on https://javatutorial.net/java-file-upload-rest-service
 *
 * @author javatutorial.net
 */
public class FileUploaderClient {

    public static void main(String[] args) {

        // the file we want to upload
        String inFile = "/home/moonwave/Pictures/pydev-run-1.png";
//        File inFile = new File("/home/moonwave/Pictures/pydev-run-1.png");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(inFile);
            DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

            // server back-end URL
            HttpPost httppost = new HttpPost("http://localhost:8000/file/upload/");
            MultipartEntity entity = new MultipartEntity();

//            new FileEntity(new File(getClass().getResource("/tomtom/download/test.metalink").toURI())
            // set the file input stream and file name as arguments
            entity.addPart("file", new FileEntity(new File(FileUploaderClient.class.getResource(inFile).toURI())));
//            entity.addPart("file", new InputStreamBody(fis, inFile.getName()));
//            mutiEntity.addPart("LogFile", new FileBody(file)); // https://www.programcreek.com/java-api-examples/org.apache.http.entity.mime.MultipartEntity
            entity.addPart("remark", new StringBody("{\"image 2018\"}", Charset.forName("UTF-8")));

            httppost.setEntity(entity);

            // execute the request
            HttpResponse response = httpclient.execute(httppost);
            
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity, "UTF-8");

            System.out.println("[" + statusCode + "] " + responseString);

        } catch (ClientProtocolException e) {
            System.err.println("Unable to make connection");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Unable to read file");
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException e) {}
        }
    }

}
