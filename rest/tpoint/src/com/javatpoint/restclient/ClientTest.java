package com.javatpoint.restclient;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.ClientConfig;

/**
 * https://www.javatpoint.com/jax-rs-example-jersey
 * http://www.vogella.com/tutorials/REST/article.html#rest
 * https://vaadin.com/blog/consuming-rest-services-from-java-applications
 */
public class ClientTest {

    private static String ACCESS_TOKEN = "567c...90b";

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target(getBaseURI());
        //Now printing the server code of different media type
        System.out.println(target.path("rest").path("hello").request().accept(MediaType.TEXT_PLAIN).get(String.class));
        System.out.println(target.path("rest").path("hello").request().accept(MediaType.TEXT_XML).get(String.class));
        System.out.println(target.path("rest").path("hello").request().accept(MediaType.TEXT_HTML).get(String.class));

        // token authentication
        System.out.println(target.path("rest").path("hello").request().header("Authorization", "Token " + ACCESS_TOKEN)
                .accept(MediaType.TEXT_HTML).get(String.class));

        System.out.println(query("rest/hello"));
        System.out.println(query("rest/hello", "limit=100&pageSize=10"));
    }

    private static String query(String path) {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target(getBaseURI());
        target.path(path).request().accept(MediaType.APPLICATION_JSON).get(String.class);
        target.path(path).request().accept(MediaType.TEXT_PLAIN).get(String.class);
        
        // token authentication
        String responseStr = target.path(path).request().header("Authorization", "Token " + ACCESS_TOKEN)
                .accept(MediaType.APPLICATION_JSON).get(String.class);
        System.out.println(responseStr);
        return responseStr;
    }

    private static String query(String path, String queryParams) {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target(getBaseURI());
        target = target.path(path);

        Map<String, String> map = getQueryMap(queryParams);
        Iterator<Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            target = target.queryParam(entry.getKey(), entry.getValue());
        }

        // token authentication
        String responseStr = target.request().header("Authorization", "Token " + ACCESS_TOKEN)
                .accept(MediaType.APPLICATION_JSON).get(String.class);
        System.out.println(responseStr);
        return responseStr;
    }

    private static URI getBaseURI() {
        //here server is running on 4444 port number and project name is restfuljersey
        return UriBuilder.fromUri("http://localhost:4444/restfuljersey").build();
    }

    /**
     * https://coderanch.com/t/383310/java/parse-url-query-string-parameter
     * @param queryParams e.g. limit=100&pageSize=10
     * @return
     */
    private static Map<String, String> getQueryMap(String queryParams) {
        String[] params = queryParams.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params)
        {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }}