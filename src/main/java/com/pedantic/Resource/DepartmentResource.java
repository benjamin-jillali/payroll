package com.pedantic.Resource;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.pedantic.config.MaxAge;

@Path("departments")
public class DepartmentResource {
	//content negotiation via qs
	//qs refers to priority if client doesnt give priority so we can do it from our end qs(quality from server) is like q from client side
	@GET
	@Produces({"application/json; qs=0.9", "application/xml; qs=0.7"})
	public Response getDepartment() {
		return null;
	}	
	@GET
	@Path("{id}") //api/v1/departments/2
	@Produces("application/json")
	//we annotate with maxage to call the dynamic filter
	@MaxAge(age = 200)
	public Response getDepartmentById(@PathParam("id") @NotNull Long id) {
		//get department from db
		return Response.ok().build();
	}

}
