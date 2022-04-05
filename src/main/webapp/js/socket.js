var uri = "ws://" + document.location.host +'/payroll/' + "chat";
var websocket = new WebSocket(uri);
var messageArea = document.getElementById("messages");
websocket.onopen = function (evt){
	openEvent(evt)
};
websocket.onmessage = function(evt){
	onMessage(evt)
};
websocket.onerror = function(evt){
	onError(evt)
};
function openEvent(evt){
	console.log('connection established');
}
function sendMessage(){
	var time = new Date().toTimeString();
	websocket.send(time + ' ' + messageField.value);
	messageField.value = '';
}
function onMessage(evt){
	var label = document.createElement('p');
	label.style.wordWrap = "breal-word";
	label.innerHTML = evt.data;
	console.log("is working?");
	messageArea.appendChild(label);
}
function onError(evt){
	console.log("an error occured");
}

