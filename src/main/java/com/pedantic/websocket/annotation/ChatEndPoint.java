package com.pedantic.websocket.annotation;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
//websocket is fully bydirection server and client back and forth
//2 ways to create websockets in jee via annotated api like this case and programmatic route
//pojo -> endpoint use ServerEndpoint annotation from websocket.server api
@ServerEndpoint("/chat") //chat is endpoint uri requires annotated method onopen onclose and onmessage
public class ChatEndPoint {
	//we have link queue that we add the newly opened session to the queue
	private static final ConcurrentLinkedQueue<Session> peers = new ConcurrentLinkedQueue<Session>();
	@Inject
	private Logger logger;
	//this method will be invoked by the runtime when session is initialized or client connects
	//the Session object is an interface into the current session with the client
	@OnOpen
	public void open (Session session) {
		logger.log(Level.INFO, "New session open");
		//add the session to the queue
		peers.add(session);
	}
//oposite of onOpen invoked when client closes the session with the server
	@OnClose
	public void close(Session session, CloseReason closeReason) {
		logger.log(Level.INFO, String.format("Session closed with the reason %s", closeReason.getReasonPhrase()));
		//remove client from session
		peers.remove(session);
	}
	//invoked whenever client sends a message takes message and session we iterate through the peers concurrent linked peers queue
	//and send a message back to all the present clients except the sender
	@OnMessage
	public void relayMessage(String message, Session session) throws IOException {
		for(Session peer : peers) {
			if(!peer.equals(session)) {
				peer.getBasicRemote().sendText(message);
			}
		}
	}
}
