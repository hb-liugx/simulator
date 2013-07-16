package net.peaxy.simulator.web.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

import org.glassfish.grizzly.http.util.HttpStatus;

@Provider
public class InternalException extends WebApplicationException {

	private static final long serialVersionUID = 9132482461167491929L;

	public InternalException(){
		super(HttpStatus.INTERNAL_SERVER_ERROR_500.getStatusCode());	
	}
	
	public InternalException(String message){
		super(new Throwable(message),HttpStatus.INTERNAL_SERVER_ERROR_500.getStatusCode());
	}
}