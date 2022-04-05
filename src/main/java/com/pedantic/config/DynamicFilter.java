package com.pedantic.config;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.CacheControl;

public class DynamicFilter implements ContainerResponseFilter{
	int age;
	
	public DynamicFilter(int age) {
		super();
		this.age = age;
	}
	

	public DynamicFilter() {
	}


	@Override
	//get request and response context
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		//get requestContext for get call then create new cache control and add the cache age variable
		if(requestContext.getMethod().equalsIgnoreCase("GET")) {
			CacheControl cacheControl = new CacheControl();
			cacheControl.setMaxAge(age);
			responseContext.getHeaders().add("Cache-Control", cacheControl);
			responseContext.getHeaders().add("Dynamic Filter", "Dynamic filter has been invoked and works");
		}	
		
	}
	

}
