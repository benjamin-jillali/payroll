package com.pedantic.Resource;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
//check for breach with email at this resource
@Path("client")
@Produces(MediaType.APPLICATION_JSON)
public class JaxRsClientResouce {
	
	@Inject
	JaxRsClient jaxRsClient;
	
	@Path("breach/{email}") //http://localhost:8080/payroll/api/v1/client/breach/user_email
	@GET
	public Response checkBreaches(@PathParam("email") @NotEmpty String email) {
		int breaches = jaxRsClient.checkBreaches(email);
		return Response.ok(breaches + "breaches found for email" + email).build();
	}
	
	

}
