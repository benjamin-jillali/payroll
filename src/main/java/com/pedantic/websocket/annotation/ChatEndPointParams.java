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
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
//templated uri webpaoth with variable allows us to get values from the header with the @PathParam annotation
//take care the pathparam for websockets is from jax.websocket.server package instead of jax.rs api
@ServerEndpoint(value = "/connect/{user}")
public class ChatEndPointParams {
	private static final ConcurrentLinkedQueue<Session> peers = new ConcurrentLinkedQueue<Session>();
	@Inject
	private Logger logger;
	//add client to the peers queue
	@OnOpen
    public void open(Session session) {
        peers.add(session);

    }
//on close remove the session
    @OnClose
    public void close(Session session, CloseReason closeReason) {
        logger.log(Level.INFO, String.format("Session closed with reason %s", closeReason.getReasonPhrase()));
        peers.remove(session);
    }
//get users name from the header and inject into  name then send message to peers
//we have session and message
    @OnMessage
    public void relayMessage(String message, Session session, @PathParam("user") String name) throws IOException {
        for (Session peer : peers) {
            if (!peer.equals(session)) {
                logger.log(Level.INFO, "User name is " + name);
                peer.getBasicRemote().sendText(name + " <br/> " + message);
            }
        }
    }
}
