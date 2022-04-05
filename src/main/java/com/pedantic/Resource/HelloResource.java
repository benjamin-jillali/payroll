package com.pedantic.Resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("hello")
public class HelloResource {
	//jax exposes resources at restful points every one must be annotated with @Path
	//use path annotation to expose a rest method with that path also method type like @GET
	@Path("{name}") //api/v1/{name}
	@GET
	public Response sayHello(@PathParam("name") String name) {
		String greeting = "Hello " + name;
		return Response.ok(greeting).build();
	}
	//@GET means this method response to the http get request @GET method is needed for a get request for the method
	@GET
	@Path("greet") //api/v1/greet
	public String greet() {
		return "Hello, World";
	}

}
