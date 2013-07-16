package net.peaxy.simulator.web.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

import org.glassfish.grizzly.http.util.HttpStatus;

@Provider
public class ExistedException extends WebApplicationException {

	private static final long serialVersionUID = 9132482461167491929L;

	public ExistedException(){
		super(HttpStatus.FOUND_302.getStatusCode());	
	}
	
	public ExistedException(String message){
		super(new Throwable(message),HttpStatus.FOUND_302.getStatusCode());
	}
}