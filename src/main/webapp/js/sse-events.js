if(typeof (EventSource) !== "undefined"){
	//if browser accepts sse we initialize a new direct source passing in the path to sse resource
	var source = new EventSource("http://localhost:8080/payroll/api/v1/sse-path");
	//we use source to listen to new mesages
	//the onmessage method is meant to consume events for unnamed event objects in ServerSentEvent.java the broadcast method with the sse.newEvent
	source.onmessage = function (evt){
		document.getElementById("message").innerText += evt.data + "\n"
	};
	source.addEventListener("employee", function (event){
		document.getElementById("employee").innerText += event.data + "\n"
	});
	
}else{
	document.getElementById("message").innerText = "Sorry! your browser doesn't support SSE";
}