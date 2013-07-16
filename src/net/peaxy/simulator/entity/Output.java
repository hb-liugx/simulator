package net.peaxy.simulator.entity;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONObject;

public class Output {

	private int httpStatus;
	private String result;
	private MediaType type;
	private JSONObject json;
	
	public Output(JSONObject json){
		httpStatus = 200;
		result = "Success";
		type = MediaType.TEXT_PLAIN_TYPE;
		this.json = json;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public MediaType getType() {
		return type;
	}

	public void setType(MediaType type) {
		this.type = type;
	}

	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}
}
