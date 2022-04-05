package com.pedantic.websocket.programmatic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
//for programmatic way of websocket extend Endpoint class we do that in ServerConfig with the ServerApplicationConfig interface
//since programmatic we need to host at specific path
public class MyProgrammaticEndPoint extends Endpoint{
	//override methos u want theres opOpen, onClose, onError
	@Override
	//invoked whenever socket initiated and passed session and endpoint 
	//we can use EndpointConfig to do configurations required for the client
	public void onOpen(Session session, EndpointConfig endpointConfig) {
		//add message handler then we can do stuff here get the client that opened the session and send them a message
		session.addMessageHandler((MessageHandler.Whole<String>) s ->{
			System.out.println("Server: " + s);
			try {
				session.getBasicRemote().sendText("In response to message received from programmatic. This is the server");
			}catch (IOException ex) {
				Logger.getLogger(MyProgrammaticEndPoint.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
	}

}
