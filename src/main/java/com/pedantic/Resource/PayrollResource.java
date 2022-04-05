package com.pedantic.Resource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.pedantic.service.PayrollService;

@Path("payroll")
public class PayrollResource {
	//ManagedExecutorService is extension of ExecutorService but for managed environment
	@Resource
	ManagedExecutorService managedExecutorService;
	
	@Inject
	PayrollService payrollService;
	
	@POST
	@Path("run")
	//simulate payroll operation inject asyncresponse using @Suspended
	//this will be used to resume the response
	public void run(@Suspended AsyncResponse asyncResponse) {
		//get name of thread that came with request
		final String currentThread = Thread.currentThread().getName();
		//we set a timeout of 5 seconds and when that is succeeded then timoutHandler will be invoked
		asyncResponse.setTimeout(5000, TimeUnit.MILLISECONDS);
		//1. Implement timeout handler
		asyncResponse.setTimeoutHandler(asyncResponse1 ->{
			asyncResponse1.resume(Response.status(Response.Status.REQUEST_TIMEOUT).entity("Sorry request timed out please try again. ").build());
			
		});		
		//2. Register other callbacks
		//request processing callback that recieves request processing completion events ConnectionCallback is optional
		asyncResponse.register(CompletionCallbackHandler.class);
		//3. Pass long running task to MES and resume in there		
		//submit a new thread
		managedExecutorService.submit(()  -> {
			//get name of new thread
			final String spawnedThreadName = Thread.currentThread().getName();
			//Long running tasks
			payrollService.computePayroll(); //Very long expensive operation
			//.resume can be used to resume the suspended request
			//header sends request thread and current thread
			asyncResponse.resume(Response.ok().header("Original Thread ", currentThread)
					.header("Spawned Thread", spawnedThreadName)
					.status(Response.Status.OK).build());
		});
	}
	
	static class CompletionCallbackHandler implements CompletionCallback{
		@Override
		public void onComplete(Throwable throwable) {
			
		}
	}
	
	static class ConnectionCallbackHandler implements ConnectionCallback{
		@Override
		public void onDisconnect(AsyncResponse disconnected) {
			
		}
	}
	
	@POST
	@Path("run-cf")
	public void computerPayrollCF(@Suspended AsyncResponse asyncResponse, @QueryParam("i") @DefaultValue("3") long number) {
		CompletableFuture.runAsync(() -> payrollService.fibonacci(number), managedExecutorService)
		.thenApply((result) -> asyncResponse.resume(Response.ok(result).build()));
	}
	

}
