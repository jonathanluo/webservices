package com.jeffhanson.ws.security;

import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.SOAPPart;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.NullProvider;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.utils.XMLUtils;
import org.apache.ws.axis.security.util.AxisUtil;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.message.WSSignEnvelope;
import org.apache.ws.security.message.WSEncryptBody;
import org.apache.ws.security.message.WSSAddUsernameToken;
import org.apache.ws.security.message.token.SecurityTokenReference;
import org.apache.ws.security.message.token.Reference;
import org.apache.ws.security.WSSecurityEngine;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSSConfig;
import org.apache.ws.security.util.WSSecurityUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * http://www.devx.com/Java/Article/28816/0/page/4
 *
 * Enter description here.
 *
 * @author <a href="mailto:jeff@jeffhanson.com">Jeff Hanson</a>
 * @version $Revision: 1.1 $
 *          <p/>
 *          <p><b>Revisions:</b>
 *          <p/>
 *          <p><b>Jul 26, 2005 jhanson:</b>
 *          <ul>
 *          <li> Created file.
 *          </ul>
 */

public class WSSecuritySample
{
   private static final String soapMsg =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
      "<SOAP-ENV:Envelope" +
      "   xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\"\n" +
      "   xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
      "   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
      "   <SOAP-ENV:Body>" +
      "      <sayHello xmlns=\"http://jeffhanson.com/services/helloworld\">" +
      "         <value xmlns=\"\">Hello world!</value>" +
      "      </sayHello>" +
      "   </SOAP-ENV:Body>" +
      "</SOAP-ENV:Envelope>";

   // The following initializes the security engine to the
   // default WS-Security settings
   private static final WSSecurityEngine secEngine =
      new WSSecurityEngine();

   // The following creates a crypto provider according to the
   // class name specified by the system property:
   //    org.apache.ws.security.crypto.provider
   //
   // If the provider property is not set, the default class,
   // org.apache.ws.security.components.crypto.BouncyCastle, is
   // used.
   //
   // The provider is initialized to the values specified in
   // the crypto.properties file. The crypto.properties file
   // found in the wss4j jar file specifies
   //    org.apache.ws.security.components.crypto.Merlin
   // as the provider class.
   private static final Crypto crypto =
      CryptoFactory.getInstance();

   private AxisClient engine = null;
   private MessageContext msgContext = null;

   /**
    * Main method
    */
   public static void main(String[] args)
   {
      try
      {
         WSSecuritySample app = new WSSecuritySample();

         Message axisMessage = app.getAxisMessage(soapMsg);
         SOAPEnvelope unsignedEnvelope = axisMessage.getSOAPEnvelope();

         System.out.println("<<<<<< Unsigned and Unencrypted >>>>>>");
         XMLUtils.PrettyElementToWriter(unsignedEnvelope.getAsDOM(),
                                        new PrintWriter(System.out));
/*
         Message samlMsg = app.addUserTokens(unsignedEnvelope);
         System.out.println("\n<<<<<< User Tokens >>>>>>");
         XMLUtils.PrettyElementToWriter(samlMsg.getSOAPEnvelope().getAsDOM(),
                                        new PrintWriter(System.out));

         Message encryptedMsg = app.encryptSOAPEnvelope(unsignedEnvelope,
                                                        axisMessage);
         System.out.println("\n<<<<<< Encrypted >>>>>>");
         XMLUtils.PrettyElementToWriter(encryptedMsg.getSOAPEnvelope().getAsDOM(),
                                        new PrintWriter(System.out));
*/
         Message signedMsg = app.signSOAPEnvelope(unsignedEnvelope);
         System.out.println("\n<<<<<< Signed >>>>>>");
         XMLUtils.PrettyElementToWriter(signedMsg.getSOAPEnvelope().getAsDOM(),
                                        new PrintWriter(System.out));                                       
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   /**
    * WSSecuritySample constructor
    */
   public WSSecuritySample()
   {
      engine = new AxisClient(new NullProvider());
      msgContext = new MessageContext(engine);
   }

   /**
    * Creates and returns an Axis message from a
    * SOAP envelope string.
    *
    * @param unsignedEnvelope   a string containing a SOAP
    *                           envelope
    * @return <code>Message</code>   the Axis message
    */
   private Message getAxisMessage(String unsignedEnvelope)
   {
      InputStream inStream =
         new ByteArrayInputStream(unsignedEnvelope.getBytes());
      Message axisMessage = new Message(inStream);
      axisMessage.setMessageContext(msgContext);
      return axisMessage;
   }

   /**
    * Creates a signed SOAP message in compliance with WS-Security.
    *
    * @return <code>Message</code>  the signed SOAP envelope
    *         as an Axis message
    * @throws Exception on error
    */
   public Message signSOAPEnvelope(SOAPEnvelope unsignedEnvelope)
      throws Exception
   {
      // WSSignEnvelope signs a SOAP envelope according to the
      // WS Specification (X509 profile) and adds the signature data
      // to the envelope.
      WSSignEnvelope signer = new WSSignEnvelope();

      String alias = "16c73ab6-b892-458f-abf5-2f875f74882e";
      String password = "security";
      signer.setUserInfo(alias, password);

      Document doc = unsignedEnvelope.getAsDocument();

      // The "build" method, creates the signed SOAP envelope.
      // It takes a SOAP Envelope as a W3C Document and adds
      // a WSS Signature header to it. The signed elements
      // depend on the signature parts that are specified by
      // the WSBaseMessage.setParts(java.util.Vector parts)
      // method. By default, SOAP Body is signed.
      // The "crypto" parameter is the object that implements
      // access to the keystore and handling of certificates.
      // A default implementation is included:
      //    org.apache.ws.security.components.crypto.Merlin
      Document signedDoc = signer.build(doc, crypto);

      // Convert the signed document into a SOAP message.
      Message signedSOAPMsg =
         (org.apache.axis.Message)AxisUtil.toSOAPMessage(signedDoc);

      return signedSOAPMsg;
   }

   /**
    * Adds user tokens to a SOAP envelope in compliance with WS-Security.
    *
    * @return <code>Message</code>  the signed SOAP envelope
    *         as an Axis message
    * @throws Exception on error
    */
   public Message addUserTokens(SOAPEnvelope unsignedEnvelope)
      throws Exception
   {
      WSEncryptBody wsEncrypt = new WSEncryptBody();

      // Get the message as document
      Document doc = unsignedEnvelope.getAsDocument();

      String username = "joedoe";
      String password = "this is a lot of foobar ";
      byte[] key = password.getBytes();

      // Add the UserNameToken.
      WSSAddUsernameToken builder =
         new WSSAddUsernameToken("", false);
      builder.setPasswordType(WSConstants.PASSWORD_TEXT);
      builder.build(doc, username, password);

      // Add an Id to it.
      Element usrEle =
          (Element)(doc.getElementsByTagNameNS(WSConstants.WSSE_NS,
                                               "UsernameToken").item(0));
      String idValue = "7654";
      usrEle.setAttribute("Id", idValue);

      // Create a Reference to the UserNameToken.
      Reference ref = new Reference(WSSConfig.getDefaultWSConfig(),
                                    doc);
      ref.setURI("#" + idValue);
      ref.setValueType("UsernameToken");
      SecurityTokenReference secRef =
          new SecurityTokenReference(WSSConfig.getDefaultWSConfig(),
                                     doc);
      secRef.setReference(ref);

      // adding the namespace
      WSSecurityUtil.setNamespace(secRef.getElement(),
                                  WSConstants.WSSE_NS,
                                  WSConstants.WSSE_PREFIX);

      // Setting necessary parameters in WSEncryptBody.
      wsEncrypt.setKeyIdentifierType(WSConstants.EMBED_SECURITY_TOKEN_REF);
      wsEncrypt.setSecurityTokenReference(secRef);
      wsEncrypt.setKey(key);

      // Encrypt using the using the key
      Document encDoc = wsEncrypt.build(doc, crypto);

      // Convert the document into a SOAP message.
      Message signedMsg =
         (Message)AxisUtil.toSOAPMessage(encDoc);

      return signedMsg;
   }

   /**
    * Encrypts a SOAP envelope in compliance with WS-Security.
    *
    * @return <code>Message</code>  the signed SOAP envelope
    *         as an Axis message
    * @throws Exception on error
    */
   public Message encryptSOAPEnvelope(SOAPEnvelope unsignedEnvelope,
                                      Message axisMessage)
      throws Exception
   {
      WSEncryptBody encrypt = new WSEncryptBody();
      encrypt.setUserInfo("16c73ab6-b892-458f-abf5-2f875f74882e");

      // Before Encryption
      Document doc = unsignedEnvelope.getAsDocument();
      Document encryptedDoc = encrypt.build(doc, crypto);

      // Convert the document into a SOAP message.
      Message encryptedMsg = (Message) AxisUtil.toSOAPMessage(encryptedDoc);
      String soapPart = encryptedMsg.getSOAPPartAsString();
      ((SOAPPart)axisMessage.getSOAPPart()).setCurrentMessage(soapPart,
                                                              SOAPPart.FORM_STRING);

      encryptedDoc = axisMessage.getSOAPEnvelope().getAsDocument();

      // Convert the document into a SOAP message.
      Message encryptedSOAPMsg =
         (Message)AxisUtil.toSOAPMessage(encryptedDoc);

      return encryptedSOAPMsg;
   }
}
