package com.pedantic.websocket.annotation;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.Session;

import com.pedantic.websocket.data.MySimplePojo;
import com.pedantic.websocket.data.MySimplePojoDecoder;
import com.pedantic.websocket.data.MySimplePojoEncoder;

@ClientEndpoint(decoders = MySimplePojoDecoder.class, encoders = MySimplePojoEncoder.class)
public class ClientEndPoint {
	@OnMessage
	public void processMessage(Session session, MySimplePojo mySimplePojo) throws IOException, EncodeException {
		Logger logger = Logger.getLogger(ClientEndpoint.class.getName());
		logger.log(Level.INFO, "%%%%%%%%%%% My simple pojo received on the programmatic %%%%%%%%%%%%");
		logger.log(Level.INFO, mySimplePojo.toString());
		session.getBasicRemote().sendObject(mySimplePojo);
	}
	
}
