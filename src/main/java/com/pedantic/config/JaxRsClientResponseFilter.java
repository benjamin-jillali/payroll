package com.pedantic.config;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class JaxRsClientResponseFilter implements ClientResponseFilter{
	
	@Inject
	Logger logger;
	
	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException{
		logger.log(Level.INFO, "Client response filter invoked" );
	}

}
