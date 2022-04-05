package com.pedantic.Resource;


import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
//import javax.resource.spi.SecurityException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.pedantic.entities.ApplicationUser;
import com.pedantic.service.ApplicationState;
import com.pedantic.service.PersistenceService;
import com.pedantic.service.SecurityUtil;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;


@Path("users")
@Consumes("application/json")
@Produces("application/json")
@RequestScoped
public class UsersResource {
	
	@Inject
	ApplicationState applicationState;
	
	@Inject
	private SecurityUtil securityUtil;
	
	@Inject
	PersistenceService persistenceService;
	
	@Inject
	JaxRsClient jaxRsClient;
	
	@Context
	private UriInfo uriInfo;
	
	@Inject 
	Logger logger;
	//makes request with json representation of the Application user
	@POST
	public Response createUser(@Valid ApplicationUser user) {		
		//grab the user then persist then add the user to the header in the response to the client
		persistenceService.saveUser(user);
		return Response.created(uriInfo.getAbsolutePathBuilder().path(user.getId().toString()).build())
				.status(Response.Status.OK).build();
		
	}
	
	@POST //api/v1/users
	//we want to accept form fields
	//this will consume a url encoded form instead of json
	//media type is a class that contains constants to media types
	//using the formparam to ge the form elements from the html form aka "email and "password"
	@Path("form")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//to gain access to infromation in the header we can use the @HeaderParam referer will grab the referer in the header we can set default value too
	public Response createNewUser(@FormParam("email") String email, @FormParam("password") String password, @HeaderParam("Referer") String referer) {
		return null;
	}	
	
	@POST
	@Path("map")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//this will pull the resources in the form and put it in a map so we can pull the values with .get
	//@Context injects jax-rs classes and objects
	public Response  createNewUser(MultivaluedMap<String, String> formMap, @Context HttpHeaders httpHeaders) {
//		httpHeaders.getHeaderString("Referer");
		httpHeaders.getRequestHeader("Referer").get(0);
		for(String h : httpHeaders.getRequestHeaders().keySet()) {
			System.out.println("header key set: " + h);
		}
		String email = formMap.getFirst("email");
		String password = formMap.getFirst("password");
		return null;
	}
	//make a post to login  consumes form_url encoded
	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//grab and validate email and password to make sure the values are set
	public Response login(@FormParam("email") @NotEmpty(message = "Email must be set. ") String email, 
						  @FormParam("password") @NotEmpty(message = "password must be set") String password) throws Exception {
		//using securityutl class to authenticate user
		//if authenticate doesnt succeed method returns false then we throw securityexception
		//secexcp throws a security violation
		//so we have a config.SecurityExceptionMapper class
		if(!securityUtil.authenticateUser(email, password)) {
			throw new SecurityException("Email or password Incorret!");
		}
		//if auth succeeds we have applicationstate
		//to store logged email in session scope
		applicationState.setEmail(email);
		//get token gets token for user
		String token = getToken(email);
		//the returned token is passed to the response header
		//authorization tells us that the token is authorized with the bearer
		return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();
	}
	//we get the token 
	private String getToken(String email) {
		//pass email and generate a key
		//we use it to sign jwt
		Key key = securityUtil.generateKey(email);
		//issuer referes to our api set dates set expiration for token in this case 15 mins
		//sign wth signature method and hs512 algorithm giving the key then compact  it returns string representation
		//audience is who the token is intended for
		String token = Jwts.builder().setSubject(email).setIssuer(uriInfo.getAbsolutePath().toString()) //get absoulte path = http://localhost:8080/payroll/v1/users/login
				.setIssuedAt(new Date()).setExpiration(securityUtil.toDate(LocalDateTime.now().plusMinutes(15)))
				.signWith(SignatureAlgorithm.HS512, key).setAudience(uriInfo.getBaseUri().toString()).compact();
		
		logger.log(Level.INFO, "Generated Token is {0}", token);
		
		return token;
	}

	//third way to get fields is bean param
	//the @BeanParam will instantiate the class input in this case ApplicationUser and go through 
	//using reflection and instantiate the individual fields with the values from the html form with the matching
	//@FormParam
	@POST
	@Path("bean")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//get cookie values from the header with @CookieParam
	public Response createNewUser(@BeanParam ApplicationUser applicationUser, @CookieParam("user") String user) {		
		return null;
	}
	
	/*
	 * get request to jaxrs root path then to EmployeeResouce the GET method http 1.1 protocol
	 * GET /api/v1/employees HTTP/1.1
	 * Host www.ourdomain.com
	 * User-Agent: Java/1.8.0_151
	 * Content-Type: text/plain;charset=utf-8
	 * Accept means client is expecting data representation to be in the json format
	 * Accept: application/json;q=2, application/xml;q=6
	 * 
	 * json has the priority since q=2 is lower then q=6
	 */
	
	//Client content negotiation
	//using this @Produces annotation syntax we can recieve more then one type of data format in this case json and xml
	@GET
	@Path("id")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getUserById(@PathParam("id") Long id) {
		return Response.ok().status(Response.Status.OK).build();
	}
	
}





