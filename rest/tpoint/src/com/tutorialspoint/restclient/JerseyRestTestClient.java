package com.tutorialspoint.restclient;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.tutorialspoint.*;

/**
 * http://www.vogella.com/tutorials/REST/article.html#rest
 * https://www.tutorialspoint.com/restful/restful_first_application.htm
 * https://www.tutorialspoint.com/restful/restful_methods.htm
 * https://vaadin.com/blog/consuming-rest-services-from-java-applications
 */
public class JerseyRestTestClient  {

   // http://www.tomchristie.com/rest-framework-2-docs/api-guide/authentication
   // https://docs.djangoproject.com/en/2.0/topics/auth/default/
   private static String ACCESS_TOKEN = "567c...90b";

   private static String REST_SERVICE_URL = "http://localhost:8080/UserManagement/rest/UserService/users";
   private static final String SUCCESS_RESULT="<result>success</result>";
   private static final String PASS = "pass";
   private static final String FAIL = "fail";

   private Client client;

   private void init(){
      this.client = ClientBuilder.newClient();
   }

   public static void main(String[] args){
      JerseyRestTestClient tester = new JerseyRestTestClient();
      //initialize the tester
      tester.init();

      //test get all users Web Service Method
      tester.testGetAllUsers();

      //test get user Web Service Method 
      tester.testGetUser();

      //test get user (json) Web Service Method 
      tester.testGetUserJson();

      //test update user Web Service Method
      tester.testUpdateUser();

      //test add user Web Service Method
      tester.testAddUser();

      //test delete user Web Service Method
      tester.testDeleteUser();
   }
   //Test: Get list of all users
   //Test: Check if list is not empty
   private void testGetAllUsers(){
      GenericType<List<User>> list = new GenericType<List<User>>() {};
      List<User> users = client
         .target(REST_SERVICE_URL)
         .request(MediaType.APPLICATION_XML)
         .get(list);
      String result = PASS;
      if(users.isEmpty()){
         result = FAIL;
      }
      System.out.println("Test case name: testGetAllUsers, Result: " + result );
   }
   //Test: Get User of id 1
   //Test: Check if user is same as sample user
   private void testGetUser(){
      User sampleUser = new User();
      sampleUser.setId(1);

      User user = client
         .target(REST_SERVICE_URL)
         .path("/{userid}")
         .resolveTemplate("userid", 1)
         .request(MediaType.APPLICATION_XML)
         .get(User.class);
      String result = FAIL;
      if(sampleUser != null && sampleUser.getId() == user.getId()){
         result = PASS;
      }
      System.out.println("Test case name: testGetUser, Result: " + result );
   }

   private void testGetUserJson(){
      User sampleUser = new User();
      sampleUser.setId(1);

      String str = client
         .target(REST_SERVICE_URL)
         .path("/json/{userid}")
         .resolveTemplate("userid", 1)
         .request(MediaType.APPLICATION_JSON).get(String.class);

      System.out.println("Test case name: testGetUserJson, Result: " + str );
      System.out.println("Test case name: testGetUserJson, Result: " + PASS );
   }

   private void testGetUserJsonUsingTokenAuthentication(){
      User sampleUser = new User();
      sampleUser.setId(1);

      String str = client
         .target(REST_SERVICE_URL)
         .path("/json/{userid}")
         .resolveTemplate("userid", 1)
         .request(MediaType.APPLICATION_JSON)
         .header("Authorization", "Token " + ACCESS_TOKEN)
         .get(String.class);

      System.out.println("Test case name: testGetUserJson, Result: " + str );
      System.out.println("Test case name: testGetUserJson, Result: " + PASS );
   }

   //Test: Update User of id 1
   //Test: Check if result is success XML.
   private void testUpdateUser(){
      Form form = new Form();
      form.param("id", "1");
      form.param("name", "suresh");
      form.param("profession", "clerk");

      String callResult = client
         .target(REST_SERVICE_URL)
         .request(MediaType.APPLICATION_XML)
         .put(Entity.entity(form,
            MediaType.APPLICATION_FORM_URLENCODED_TYPE),
            String.class);
      String result = PASS;
      if(!SUCCESS_RESULT.equals(callResult)){
         result = FAIL;
      }

      System.out.println("Test case name: testUpdateUser, Result: " + result );
   }
   //Test: Add User of id 2
   //Test: Check if result is success XML.
   private void testAddUser(){
      Form form = new Form();
      form.param("id", "2");
      form.param("name", "naresh");
      form.param("profession", "clerk");

      String callResult = client
         .target(REST_SERVICE_URL)
         .request(MediaType.APPLICATION_XML)
         .post(Entity.entity(form,
            MediaType.APPLICATION_FORM_URLENCODED_TYPE),
            String.class);
   
      String result = PASS;
      if(!SUCCESS_RESULT.equals(callResult)){
         result = FAIL;
      }

      System.out.println("Test case name: testAddUser, Result: " + result );
   }

   //Test: Delete User of id 2
   //Test: Check if result is success XML.
   private void testDeleteUser(){
      String callResult = client
         .target(REST_SERVICE_URL)
         .path("/{userid}")
         .resolveTemplate("userid", 2)
         .request(MediaType.APPLICATION_XML)
         .delete(String.class);

      String result = PASS;
      if(!SUCCESS_RESULT.equals(callResult)){
         result = FAIL;
      }

      System.out.println("Test case name: testDeleteUser, Result: " + result );
   }
}