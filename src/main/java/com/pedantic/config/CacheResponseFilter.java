package com.pedantic.config;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.ext.Provider;
//Static Container Response Filter
//want to tell client to cache response for certain amount of time
@Provider
public class CacheResponseFilter implements ContainerResponseFilter{
	//this is a jax-rs component need to register with jax runtime so can be used to filter response whne condition is met
	@Override
	//the response and request has all the data that triggered the response
	//request will be injected by jax-rs
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException{
		//getMethod will return the kind of method that was in the request post delete etc.
		String method = requestContext.getMethod();
		//will give us uri info to get path to target resource method 
		String path = requestContext.getUriInfo().getPath();//api/v1/departments
		//we check that the request is to the resource DepartmentResource before executing the if block
		if(method.equalsIgnoreCase("GET") && path.equalsIgnoreCase("departments")) {
			CacheControl cacheControl = new CacheControl();
			cacheControl.setMaxAge(100);
			//private = only client will cache
			cacheControl.setPrivate(true);
			//pass cache control object to the response
			//all the data about the response is held in the responseContext including headers
			//this refers to map holding headers
			//so we add our own to the response header with cacheControl
			responseContext.getHeaders().add("Cache-Control", cacheControl);
			responseContext.getHeaders().add("MyMessage", "This seems to work!");		
			
		}
		
	}

}
