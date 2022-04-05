package com.pedantic.Resource;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.bind.JsonbBuilder;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.pedantic.entities.Employee;
//the jaxrs api can be used to access restful webservices from across the internet
//mirroring server side apis
@ApplicationScoped
public class JaxRsClient {
	//Client is the gateway to jaxrs api
	private Client client;
	WebTarget webTarget;
	
	private final String haveIBeenPawned = "https://haveibeenpwned.com/api/v3/breachedaccount"; ////https://haveibeenpwned.com/api/v2/breachedaccount/bla@bla.com
	//initialized by ClientBuilder
	//new builder to pass arguments
	@PostConstruct
	private void init() {
		//here we set timeout then build and return client here we are targeting haveibeenpwned api
		client = ClientBuilder.newBuilder().connectTimeout(7, TimeUnit.SECONDS)
				.readTimeout(3, TimeUnit.SECONDS).build();
		//calling target with uri returns webtarget object
		webTarget = client.target(haveIBeenPawned);
		//can also use
		//ClientBuilder.newClient();
		
	}
	
	@PreDestroy
	private void destroy() {
		if(client != null) {
			client.close();
		}
	}
//checks if email is breached
	public int checkBreaches(String email) {
		//this api has a path with email to check
		JsonArray jsonValues = webTarget.path("{account}")
				.resolveTemplate("account", email).request(MediaType.TEXT_PLAIN).get(JsonArray.class);
		parseJsonArray(jsonValues);
		return jsonValues.size();
	}
	
	public JsonArray getBreaches(String email) {
		return webTarget.path("{account}").resolveTemplate("account", email).request(MediaType.TEXT_PLAIN).get(JsonArray.class);
	}
	//call the path appends the email and calling in the template then request we convert the response to jsonarray
	public void checkBreachesRx(String email) {
		//jax reactive way .rx() method
		//this calls creates a CompletionStage response object
		CompletionStage<Response> responseCompletionStage = webTarget.path("{account}")
				.resolveTemplate("account", email).request().rx().get();
		//use response object to do the reaction stuff
		//we pass the result then read the array then pass the parseJsonArray to the thenAccept object
		responseCompletionStage.thenApply(response -> response.readEntity(JsonArray.class))
		.thenAccept(this::parseJsonArray);		
	}
	private void parseJsonArray(JsonArray jsonArray) {
		//iterate over the json array then print breach date and domain then print breach size
		//normally we would warn the user thier email has been reached
		//udemy tim bushaka java ee course has more about reactive applications
		for(JsonValue jsonValue : jsonArray) {
			JsonObject jsonObject = jsonValue.asJsonObject();
			String domain = jsonObject.getString("Domain");
			String breachDate = jsonObject.getString("BreachDate");
			
			System.out.println("Breach name is " + domain);
            System.out.println("Breach date is " + breachDate);
            System.out.println();

		}
		System.out.println("Breach size is " + jsonArray.size());
		
	}
	//we post employe thats passed in to our sse resource
	public void postEmployeeToSSE(Employee employee) {
		//using jsonb convert employee to json string representation
		String json = JsonbBuilder.create().toJson(employee);
		//target sse resource with request type as media then post to the resource then get the status then log status
		//when posted it will be sent to the resource ServerSentEventResource.broadcastEmployee()
		int status = client.target("http://localhost:8080/payroll/api/v1/sse-path").request(MediaType.TEXT_PLAIN)
				.post(Entity.text(json)).getStatus();
		System.out.println("Status Recieved " + status);
		System.out.println(json);
	}
	

}






