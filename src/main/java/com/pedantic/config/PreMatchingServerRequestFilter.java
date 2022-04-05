package com.pedantic.config;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

@Provider
//use premachting so method is invoked before our request is matched to our resource methods
//we want to inspect the request header we are looking for X-Http-Method-override == DELETE
@PreMatching
//container request filter is that is used to filter requests that is sent to our filter here we prematch so before request is match to our resources
//here if user cant make a delete request because of something like a firewall so make a post request so in header will have X-Http-Method-Override
public class PreMatchingServerRequestFilter implements ContainerRequestFilter{
//it only has one paramater request context since we dont need response
	@Inject
	Logger logger;
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		logger.log(Level.INFO, "Original http method was " + requestContext.getMethod());
		String httpMethod = requestContext.getHeaderString("X-Http-Method-Override");
		if(httpMethod != null && !httpMethod.isEmpty()) {
			logger.log(Level.INFO, "Http method: " + httpMethod);
			requestContext.setMethod(httpMethod);
			logger.log(Level.INFO, "Altered http method is now: " + requestContext.getMethod());
			
		}
		
	}
	

}
