package org.arpit.javapostsforlearning.webservice.client;

import org.arpit.javapostsforlearning.webservice.HelloWorld;  
import org.arpit.javapostsforlearning.webservice.HelloWorldImplService;

public class JAXWSClient {  

    /** 
     * @author Arpit Mandliya 
     */  
    public static void main(String[] args) {  

        HelloWorldImplService helloWorldService = new HelloWorldImplService();
        HelloWorld helloWorld = helloWorldService.getHelloWorldImplPort();  
        System.out.println(helloWorld.helloWorld("Arpit"));
    }  
}  
