package com.pedantic.websocket.data;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
//we pass in the uri endpoint and the encoder and decoder class to register with the runtime
@ServerEndpoint(value = "/pojo", encoders = MySimplePojoEncoder.class, decoders = MySimplePojoDecoder.class)
public class MySimplePojoEndPoint {
	@Inject
	Logger logger;
	 @OnOpen
	 public void opened(final Session session) throws IOException, EncodeException{
		 MySimplePojo mySimplePojo = new MySimplePojo("Java EE", "bla@bla.com", "Great day! How is life?");
		 session.getBasicRemote().sendObject(mySimplePojo);
	 }
	 //we call the getbasicRemote.sendObject since we are sending an object
	 //this will call the MeSimplePojoEncoder class
	 //this assumes client sends json representation to us this will then call the pojo decoder class
	 @OnMessage
	 public void processMessage(final Session session, MySimplePojo mySimplePojo) throws IOException, EncodeException{
		 logger.log(Level.INFO, "My simple pojo received on the server *************");
		 logger.log(Level.INFO, mySimplePojo.toString());
		 session.getBasicRemote().sendObject(mySimplePojo);
		 
	 }

	

}
