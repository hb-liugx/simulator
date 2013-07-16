package net.peaxy.simulator.web.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

import org.glassfish.grizzly.http.util.HttpStatus;

@Provider
public class NotExistException extends WebApplicationException {

	private static final long serialVersionUID = 9132482461167491929L;

	public NotExistException(){
		super(HttpStatus.METHOD_NOT_ALLOWED_405.getStatusCode());	
	}
	
	public NotExistException(String message){
		super(new Throwable(message),HttpStatus.METHOD_NOT_ALLOWED_405.getStatusCode());
	}
}