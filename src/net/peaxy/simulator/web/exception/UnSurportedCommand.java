package net.peaxy.simulator.web.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

@Provider
public class UnSurportedCommand extends WebApplicationException {

	private static final long serialVersionUID = -3625691318744947289L;

	public UnSurportedCommand(){
		super(425);	
	}
	
	public UnSurportedCommand(String message){
		super(new Throwable(message),425);
	}
}