package net.peaxy.simulator.web.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

import org.glassfish.grizzly.http.util.HttpStatus;

@Provider
public class AuthenticationException extends WebApplicationException {

	private static final long serialVersionUID = 1845236687727553898L;

	public AuthenticationException(){
		super(HttpStatus.UNAUTHORIZED_401.getStatusCode());	
	}
	
	public AuthenticationException(String message){
		super(new Throwable(message),HttpStatus.UNAUTHORIZED_401.getStatusCode());
	}
}