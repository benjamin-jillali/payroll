package com.pedantic.config;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

//exception mapper is a jax-rs api construct that can use to map a given exception to a response object
//jax-rs specific construct need to annotate with @Provider so jax can see it and call it
//so any time a constraint violation is thrown it will call the class and pass in the exception of toResponse
//we can then send a meaningful message to the client of the issue
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException>{
	@Override
	//we have response method implenting to send violation exceptions to the client
	//constraint violation has set of violations
	public Response toResponse(ConstraintViolationException exception) {
		//constraint violation has set<> of violations that we get
		final Map<String, String> constraintViolation = new HashMap<>();
		//iterate over the map of exceptions and put them in our map
		for(ConstraintViolation cv : exception.getConstraintViolations()) {
			//we split path into 3 parts and get the 3rd one example from "createEmployee.arg0.fullName" to "fullName"
			String[] pathItems = cv.getPropertyPath().toString().split("\\.");
			String path = pathItems[pathItems.length - 1];
			//String path = cv.getPropertyPath().toString().split("\\.")[2];			
			constraintViolation.put(path, cv.getMessage());
		}
		//we build a response status  with the PRECONDITION_FAILED error code then pass in the map then build
		return Response.status(Response.Status.PRECONDITION_FAILED).entity(constraintViolation).build();
	}

}
