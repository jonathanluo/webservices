package com.javatpoint;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
 * https://www.javatpoint.com/difference-between-rpc-and-document
	There are many differences between RPC and Document web services.

	RPC Style

	1) RPC style web services use method name and parameters to generate XML structure.
	2) The generated WSDL is difficult to be validated against schema.
	3) In RPC style, SOAP message is sent as many elements.
	4) RPC style message is tightly coupled.
	5) In RPC style, SOAP message keeps the operation name.
	6) In RPC style, parameters are sent as discrete values.

	Document Style

	1) Document style web services can be validated against predefined schema.
	2) In document style, SOAP message is sent as a single document.
	3) Document style message is loosely coupled.
	4) In Document style, SOAP message loses the operation name.
	5) In Document style, parameters are sent in XML format.

 */
//Service Endpoint Interface
@WebService
@SOAPBinding(style = Style.RPC)
//@SOAPBinding(style = Style.DOCUMENT)
public interface HelloWorld{
 
	@WebMethod String getHelloWorldAsString(String name);
 
}