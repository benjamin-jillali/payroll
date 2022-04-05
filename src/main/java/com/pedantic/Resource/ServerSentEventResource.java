package com.pedantic.Resource;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
//Sse are unidirectional server broadcasted events
@ApplicationScoped
@Path("sse-path")
public class ServerSentEventResource {
	//gateway to Sse api is the Sse and we initialize with context annotation
	@Context
	private Sse sse;
	@Inject
	private Logger logger;
	private SseBroadcaster sseBroadcaster;
	private SseEventSink eventSink;
	//intialize broadcaster
	@PostConstruct
	private void init() {
		sseBroadcaster = sse.newBroadcaster();
	}
	//we inject ssesink event initialize broadcaster then log and intialize sink
	@GET
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void fetch(@Context SseEventSink sseEventSink) {
		sseBroadcaster.register(sseEventSink);
		this.eventSink = sseEventSink;
	}
	//consums form object
	//get outbound and pass in message then boradcast
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response broadcast(@FormParam("message") String message) {
		OutboundSseEvent broadcastEvent = sse.newEvent(message);
		sseBroadcaster.broadcast(broadcastEvent);
		return Response.noContent().build();
		
	}
	//here we use named event object with new EventBuilder and pass in data and media type then broadcast
	//this will broadcast the sse to the client
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	public Response broadcastEmployee(String employee) {
		OutboundSseEvent broadcastEvent = sse.newEventBuilder().name("employee").data(employee)
				.mediaType(MediaType.TEXT_PLAIN_TYPE).build();
		sseBroadcaster.broadcast(broadcastEvent);
		return Response.ok().status(Response.Status.OK).build();		
	}
	
	@PreDestroy
	private void destroy() {
		if(eventSink != null) {
			eventSink.close();
		}
	}

}





