package com.pedantic.websocket.programmatic;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;
//this method creates a uri endpoint for the programmatic websocket
public class ServerConfig implements ServerApplicationConfig {
	@Override
	//we get endoint config to deploy programmatic endpoint at specific path /pr
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> set){
		return new HashSet<ServerEndpointConfig>() {
			{
				add(ServerEndpointConfig.Builder.create(MyProgrammaticEndPoint.class, "/pr").build());
			}
		};
	}
	//we can do same at annotated endpoint
	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> set){
		return new HashSet<>(set);
	}

}
