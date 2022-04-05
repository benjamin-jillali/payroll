package com.pedantic.config;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures a JAX-RS endpoint. Delete this class, if you are not exposing
 * JAX-RS resources in your application.
 *
 * @author airhacks.com
 */
//required to extend Application for jax and annotate application path
//you can have multiple configuration classes
@ApplicationPath("api/v1") //https://foo.com/resources/...
public class JAXRSConfiguration extends Application {
//not required to override but need for more then 1 application path
//use getClasses when having multiple paths
//	@Override
//	public Set<Class<?>> getClasses() {
//		// TODO Auto-generated method stub
//		return super.getClasses();
//	}
	

}
