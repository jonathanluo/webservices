package com.tutorialspoint.restclient;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tutorialspoint.User;

/**
 * http://www.vogella.com/tutorials/REST/article.html#rest
 * https://www.tutorialspoint.com/restful/restful_first_application.htm
 * https://www.tutorialspoint.com/restful/restful_methods.htm
 * https://vaadin.com/blog/consuming-rest-services-from-java-applications
 * 
 * http://www.tomchristie.com/rest-framework-2-docs/api-guide/authentication
 * https://docs.djangoproject.com/en/2.0/topics/auth/default/
 */
public class JerseyRestPostClient {

    /**
     https://howtodoinjava.com/jersey/jersey-restful-client-examples/
     Jersey REST Client HTTP POST Example
    */
    private static String REST_SERVICE_URL = "http://localhost:8000";
    private Client client;
    private String token;

    private void init() {
        this.client = ClientBuilder.newClient();
    }

    public static void main(String[] args) {
        JerseyRestPostClient jrpc = new JerseyRestPostClient();
        jrpc.init();
        jrpc.token = jrpc.restPostToGetAccessToken();
        jrpc.testGetUserJsonUsingTokenAuthentication("mini-snippets/");
    }

    /**
     * cd /home/moonwave/workspace/github/drachen/django/rest-tut-mysql
     * . ~/venv/bin/activate
     * python3 manage.py runserver
     *
     * JerseyRestPostClient.java > Run As > Java Application
     */
    private String restPostToGetAccessToken() {
        Form form = new Form();
        form.param("username", "dcfg");
        form.param("password", "dconfig");

        String data = client.target(REST_SERVICE_URL).path("api-token-auth/")
              .request(MediaType.APPLICATION_JSON)
              .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);

        JSONObject obj = new JSONObject(data);
        String result = obj.getString("token");
        System.out.println("Result: " + result);
        return result;
    }

    private void testGetUserJsonUsingTokenAuthentication(String path) {
        User sampleUser = new User();
        sampleUser.setId(1);

//        String data = client.target(REST_SERVICE_URL).path("employees/")
        String data = client.target(REST_SERVICE_URL).path(path)
           .request(MediaType.APPLICATION_JSON)
           .header("Authorization", "Token " + this.token)
           .get(String.class);

        System.out.println("data: " + data );

        JSONObject obj = new JSONObject(data);
        JSONArray jsonEntries = (JSONArray) obj.get("results");
        if (jsonEntries != null) {
            for (int i = 0; i < jsonEntries.length(); i++) {
                JSONObject jsonEntry = (JSONObject) jsonEntries.get(i);
                System.out.println("item " + (i+1) + ": " + jsonEntry.toString());
            }
        }
    }
}