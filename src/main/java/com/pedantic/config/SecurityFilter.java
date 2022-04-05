package com.pedantic.config;

import java.io.IOException;
import java.security.Key;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.inject.Inject;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import com.pedantic.service.ApplicationState;
import com.pedantic.service.SecurityUtil;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

//provider used to register jaxrs components with runtime
@Provider
//name binding annotation
@Secure
//is used to prioritize filters so this one has highest priorities above other filters
//@Proirity uses an integer lower the number higher the priority
@Priority(Priorities.AUTHENTICATION) // = 1000
public class SecurityFilter implements ContainerRequestFilter{
		private static final String BEARER = "Bearer";
		
		@Inject
		private Logger logger;
		
		@Inject
		private SecurityUtil securityUtil;
//	    @Inject
//	    private MySessionStore sessionStore;
		
		@Inject
		ApplicationState applicationState;
		
		@Override
		// containerrequestcontext is an interface that gives contextual information about the request we are filtering
		//we get the authorization header key from the header to check then throw not authorized if there isnt
		public void filter(ContainerRequestContext reqCtx) throws IOException{
			//1. Get the token from the request header
			String authHeader = reqCtx.getHeaderString(HttpHeaders.AUTHORIZATION);
			if(authHeader == null || !authHeader.startsWith(BEARER)) {
				logger.log(Level.SEVERE, "Wrong or no authorization header found {0}", authHeader);
				//throw exception if not authorized and send the headers with the message
				throw new NotAuthorizedException("No authorization header provided");
			}
			//if there is a header execution will proceed here 
			//we seperate the key to retrieve just the token
			String token = authHeader.substring(BEARER.length()).trim();
			//2. Parse the token
			try {
				//we now sign with the same key that we got from the session state
				Key key = securityUtil.generateKey(applicationState.getEmail());
				//call here to pass the key set the key and parse the claim with the token
				//if key isnt correct or cant be parsed we throw exception
				Jwts.parser().setSigningKey(key).parseClaimsJws(token);
				//if successful we grab old security context and create new security context and initialize with old ones parameters
				SecurityContext securityContext = reqCtx.getSecurityContext();
				reqCtx.setSecurityContext(new SecurityContext() {				
					
					
					@Override
					public boolean isUserInRole(String role) {
						return securityContext.isUserInRole(role);
					}
					
					@Override
					public boolean isSecure() {
						return securityContext.isSecure();
					}
					
					@Override
					public Principal getUserPrincipal() {
						return () -> Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
					}

					@Override
					public String getAuthenticationScheme() {						
						return securityContext.getAuthenticationScheme();
					}

					
				});
				logger.info("Token parsed succcesfully");				
			}catch (Exception e) {
				logger.log(Level.SEVERE, "Invalud{0}", token);
				//Another way to send exceptions to the client
				//returns the context of the aborted request context interface has an abortwith
				reqCtx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			}
			
		
	}

}
