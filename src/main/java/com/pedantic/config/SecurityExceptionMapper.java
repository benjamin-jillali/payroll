package com.pedantic.config;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
//,aps security exception
@Provider
public class SecurityExceptionMapper implements ExceptionMapper<SecurityException> {
	@Override
	//grabs and sets status to unauthorized grabs the message and builds
	public Response toResponse(SecurityException exception) {
		return Response.status(Response.Status.UNAUTHORIZED).entity(exception.getMessage()).build();
	}
}
