package com.pedantic.config;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
public class DynamicFilterFeature implements DynamicFeature{
//this class and method is used to register filters dynamically we put @Provider here
//we will use resourceinfo which is a class that holds resource
//this could be null
	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		MaxAge annotation = resourceInfo.getResourceMethod().getAnnotation(MaxAge.class);
		if(annotation != null) {
			//if annotation has a value we use it to initialize the dynamicfilter
			DynamicFilter dynamicFilter = new DynamicFilter(annotation.age());
			context.register(dynamicFilter);
		}
		
	}
	

}
