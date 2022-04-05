package com.pedantic.Resource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.pedantic.config.Secure;
import com.pedantic.entities.Employee;
import com.pedantic.service.PersistenceService;
import com.pedantic.service.QueryService;
//there is also the jsonp api is a high lvl api where we can ask for java object to json
@Path("employees") //api/v1/employees/*
//we can annotate the class with @Produces annotation so every method that produces content will produce
//the type specificed like json
@Produces("application/json")
//@Consumes tells jax-rs what type of data the method needs
//the json api is json-b api
//there is also the jsonp api is a high lvl api where we can ask for java object to json
@Consumes("application/json")
public class EmployeeResource {
	
//if there is only 1 method in a class without adding the path the class root path will invoke the method\
//however if a 2nd get method is created it will not be deployed
//but if path is added to 2nd method it can work
	@Inject
	Logger logger;
	@Inject
	QueryService queryService;
	@Inject
	JaxRsClient jaxRsClient;
	@Inject
	PersistenceService persistenceService;
	
	//Context gives us application context and uriInfo gives uri details	
	@Context
	private UriInfo uriInfo;
	/*
	 * get request to jaxrs root path then to EmployeeResouce the GET method http 1.1 protocol
	 * GET /api/v1/employees HTTP/1.1
	 * Host www.ourdomain.com
	 * User-Agent: Java/1.8.0_151
	 * Content-Type: text/plain;charset=utf-8
	 * Accept means client is expecting data representation to be in the json format
	 * Accept: application/json
	 */
	
	@GET
	@Path("employees")//api/v1/employees/employees GET
	//returns a list of employees in the json format
	//overide class lvl @produces declaration for this method
//	@Produces("application/xml")
	/*
	 * httpheaders content negotiation
	 */
	//add the @Secure annotation from our secure classto secure the employee request the security dependencies 
	//are in the pom include jsonwebtoken apache-shiro and slf4j-simple
	@Secure
	public Response getEmployees(@Context HttpHeaders httpHeaders){
		//send client full restful response .found are http codes found is 302
		//the getAcceptableMediaTypes gets all the media types in the reuqest header
		//this line * Accept: application/json *
//		
		Collection<Employee> employees = new ArrayList<>();

        Employee employee = new Employee();
        employee.setFullName("John Mahama");
        employee.setSocialSecurityNumber("SSF12343");
        employee.setDateOfBirth(LocalDate.of(1986, Month.APRIL, 10));
        employee.setBasicSalary(new BigDecimal(60909));
        employee.setHiredDate(LocalDate.of(2018, Month.JANUARY, 24));


        Employee employee1 = new Employee();
        employee1.setFullName("Donald Trump");
        employee1.setSocialSecurityNumber("SKJBHJSBDKJ");
        employee1.setDateOfBirth(LocalDate.of(1900, Month.JULY, 31));
        employee1.setBasicSalary(new BigDecimal(250000));
        employee1.setHiredDate(LocalDate.of(2016, Month.NOVEMBER, 7));

        employees.add(employee);
        employees.add(employee1);
		//MediaType meadiaType = httpHeaders.getAcceptableMediaTypes().get(0); //.get gets the highest preference on the list of media types
		//return Response.ok(queryService.getEmployees()).status(Response.Status.OK).build();
        return Response.ok(employees).status(Response.Status.OK).build();
//		return employees;
	}
	@GET
	//path and query parameters
	//path param helps you create variable paths of a given resource uri so client at runtime can
	//substitute that value so you can grab it variable is the {variable_name}
	//we can use regular expression to restrict what the input will be here using numbers only since its Long type
	@Path("employees/{id: \\d+}") //api/v1/employees/employee/1 GET
//	@Path("employee/{id}{department}") //api/v1/employees/employee/2/FF001431 GET
	//path and query parameters
	//path param helps you create variable paths of a given resource uri so client at runtime can
	//substitute that value so you can grab it	
	//@PathParam will retrieve the value held in the uri variable
	public Response getEmployeeById(@PathParam("id") @DefaultValue("0") Long id, @Context Request request) {
		//first fetch employee from db
		Employee employee = queryService.findEmployeeById(id);
		//initialize cacheControl cc is an abstraction for the cachecontrol header and we set its max age
		CacheControl cacheControl = new CacheControl();
		cacheControl.setMaxAge(1000);
		//used as value of http entity tag
		//we instantiate a random uuid
		EntityTag entityTag = new EntityTag(UUID.randomUUID().toString());
		//evaluate the preconditions which returns a response builder if the data is still valid
		Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(entityTag);
		if(responseBuilder != null) {
			//we return a cache control
			responseBuilder.cacheControl(cacheControl);
			return responseBuilder.build();			
		}
		//if its not valid we build response here
		responseBuilder = Response.ok(employee);
		responseBuilder.tag(entityTag);
		responseBuilder.cacheControl(cacheControl);
		return responseBuilder.build();
		
		//return Response.ok(queryService.findEmployeeById(id)).status(Response.Status.OK).build();
	}
	//using query parameters parameter will get the details from the queryparam using the name as the uri
	//Caching 
	@GET
	@Path("id")//>id=27 /id?id=12
	public Employee findEmployeeById(@QueryParam("id") @DefaultValue("0") Long id) {
		return queryService.findEmployeeById(id);
	}
//absense of path annotation means this method will be saved at this path	
	@POST //api/v1/employees POST request
	@Path("employees") //api/v1/employees/new POST
	//we set as class lvl
//	@Consumes("application/json")
//@Valid validates at the resource layer before calling the save.Employee
	//otherwise validation invoked after its created.
	//now with the json-P api this method will return a response whos body has links to the relevant bits of information about the posted employee
	public Response createEmployee(@Valid Employee employee) {
		persistenceService.saveEmployee(employee);
		//use the context with uri info to get the uri with the employees id to return it to the client
		//first get the build path //api/v1/employees then add the employees id then send to client in the response
		//jsonb api with hypermedia
		URI uri = uriInfo.getAbsolutePathBuilder().path(employee.getId().toString()).build();
		//for jsonb first link the resource to itself and return it as json response
		//employee needs a link to itself and a link to other employees so link that can get otheremployees and link to department
		URI others = uriInfo.getAbsolutePathBuilder().path(EmployeeResource.class, "getEmployees").build();
		//we call objectbuilder add the name _links create jsonarray
		//then add the link to others then add link to self and put it all together asthe  response body
		//with the input of 
//	{
//		"fullName" : "Bob barker",
//		"basicSalary":100000,
//		"socialSecurityNumber":"GJHGHD5",
//		"dateOfBirth":"1999-12-12",
//		"hiredDate":"2017-06-11"
//	}
		
		//it returns this
//		{
//			  "_links": [
//			    {
//			      "_others": "http://localhost:8080/payroll/api/v1/employees/employees/employees",
//			      "_self": "http://localhost:8080/payroll/api/v1/employees/employees/1"
//			    }
//			  ]
//			}
		JsonObjectBuilder links = Json.createObjectBuilder().add("_links", Json.createArrayBuilder().add(Json.createObjectBuilder().add("_others", others.toString())
				.add("_self", uri.toString()).build()
				));  
		//return Response.created(uri).status(Status.CREATED).build();
		//here build the response with the links 
		//return Response.ok(links.build().toString()).status(Response.Status.CREATED).build();
		//for Server Sent Events
		//we  post employee to sse source
		jaxRsClient.postEmployeeToSSE(employee);
		return Response.ok(links.build().toString()).status(Response.Status.CREATED).build();
		
		
	}
	//UPLOAD files
	@POST
	@Path("upload")
	//content types we can cosnume in this method
	@Consumes({MediaType.APPLICATION_OCTET_STREAM, "image/png", "image/jpeg", "image/jpg"})
	@Produces(MediaType.TEXT_PLAIN)
	//once a file is in the body container will pick this method since we declaired it accepts file type
	public Response uploadPicture(File picture, @QueryParam("id") @NotNull Long id) {
		//first retrieve employee from databse
		Employee employee = queryService.findEmployeeById(id);
		//we use new io to read all the bytes from the file that is posted to the method
		try (Reader reader = new FileReader(picture)){
			//then we set picture with the uri to employee then persist employee to database
			employee.setPicture(Files.readAllBytes(Paths.get(picture.toURI())));
			persistenceService.saveEmployee(employee);
			int totalSize = 0;
			int count = 0;
			final char[] buffer = new char[256];
			//then we get the size of the file and return it to the client
			while((count = reader.read(buffer)) != -1) {
				totalSize += count;
			}
			return Response.ok(totalSize).build();
			
		}catch(IOException e) {
			//if doesnt work we throw server error
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	//DOWNLOAD files
	@GET
	@Path("download") //download/download?id=9 id for the queryparam
	@Produces({MediaType.APPLICATION_OCTET_STREAM, "image/jpg", "image/jpeg", "image/png"})
	public Response getEmployeePicture(@QueryParam("id") @NotNull Long id) throws IOException {
		//first get employee from database by the id
		Employee employee = queryService.findEmployeeById(id);
		//if employee is found we return a response, getting the picture array and passing it as part of the parameters of the write method
		//of the files class then toFile and build response
		//this response will also send a copy of the image to the client
		
		//Setting cookies on the client
		//here we set user id as a cookie in the client response
		//NewCookie used to create a new http cookie
		NewCookie userId = new NewCookie("userId", id.toString());
		if(employee != null) {
			//we add the cookie to the client response we can add multiple cookies
			return Response.ok().entity(Files.write(Paths.get("pic.png"), employee.getPicture()).toFile()).cookie(userId).build();
		}
		return Response.noContent().build();
	}
	
	@DELETE
	@Path("{id: \\d+}") //api/v1/employees/34 -DELETE
	//if client cant make delete request we can grab post request and do it for the client
	//so delete request will become post request
	public Response terminateEmployee(@PathParam("id") @NotNull Long id) {
		return Response.ok().build();
	}
	

}


