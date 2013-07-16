/**
 * 
 */
package net.peaxy.simulator.web.handler;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import net.peaxy.simulator.entity.Output;
import net.peaxy.simulator.entity.PredefinedRESTCommand;
import net.peaxy.simulator.manager.CommandManager;

import org.codehaus.jettison.json.JSONException;
import org.glassfish.grizzly.http.Method;

/**
 * @author Liang
 *
 */
public final class WebRequestHandler {
	public Response handle(final String method, final String endpoint, final MultivaluedMap<String, String> queryParameters, final MultivaluedMap<String, String> formParameters, final String sessionID, final String host) {
		if( CommandManager.isSupported(endpoint, method)){
			PredefinedRESTCommand command = CommandManager.getCommand(endpoint, method);
			Output output ;
			if(Method.GET.getMethodString().equalsIgnoreCase(method)){
				output = command.invoke(queryParameters, sessionID, host);
			} else {
				output = command.invoke(queryParameters, formParameters, sessionID, host);
			}
			ResponseBuilder builder = Response.status(output.getHttpStatus());
			builder.type(output.getType());
			if(output.getJson() != null){
				if(output.getType() == MediaType.APPLICATION_JSON_TYPE){
					builder.entity(output.getJson());
				} else if(output.getType() == MediaType.TEXT_PLAIN_TYPE){
					try {
						builder.entity(output.getJson().getString(command.getSuccessfulOutputKey()));
					} catch (JSONException e) {
						errorHandle(e.getMessage());
					}
				}
			} else if(output.getResult() != null){
				if(output.getType() == MediaType.TEXT_PLAIN_TYPE){
					builder.entity(output.getResult());
				}
			}
			return builder.build();
		} else {
			return errorHandle("Not Support Command.");
		}
	}
	
	private Response errorHandle(String message){
		ResponseBuilder builder = Response.status(500);
		builder.type(MediaType.TEXT_PLAIN_TYPE);
		builder.entity(message);
		return builder.build();
	}
}
