package com.pedantic.websocket.data;

import javax.json.bind.JsonbBuilder;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

//here we use Encoder to encode the type we want so we implement the encoder.text we want to send a json
//we implement three methods from encoder encode, init and destroy
public class MySimplePojoEncoder implements Encoder.Text<MySimplePojo>{
	@Override
	//we encode or serialize the type to json with builder.create.tojson with the pojo
	public String encode(MySimplePojo mySimplePojo) {
		//Using JSON-B (JSR 367) API for mapping to JSON from T
		return JsonbBuilder.create().toJson(mySimplePojo);
	}
	@Override
	public void init(EndpointConfig endpointConfig) {
				
	}
	@Override
	public void destroy() {
		
	}

}
