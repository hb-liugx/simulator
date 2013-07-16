package net.peaxy.simulator.entity;

import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

import net.peaxy.simulator.data.TableSpace;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class PredefinedRESTCommandCreateUser extends PredefinedRESTCommand {

	private static final long serialVersionUID = 5875599672327739336L;

	@Override
	public Output invoke(final MultivaluedMap<String, String> queryParameters, final MultivaluedMap<String, String> formParameters, final String sessionID, final String host) {
		if(parameters == null || parameters.isEmpty()){
			return generateOutput(null,null,401,false);
		}
		SessionCache sessionCache = SessionCacheManager.getSessionCache(host);
		String tableKey = findTableKey(formParameters,this.parameters);
		JSONObject json = new JSONObject();
		String value;
		Set<String> names = formParameters.keySet();		
		for (String name : names) {
			CommandParameter parameter = this.parameters.get(name);
			if(parameter != null){				
				value = formParameters.getFirst(name);
				if (parameter.isSessional()){
					if(sessionCache != null)
						sessionCache.setSessionData(sessionID, name, value);
				}
				try {
					json.put(name, value);
				} catch (JSONException e) {
				}
			}
		}
		if(TableSpace.getInstance().hasTable(this.getStorageName())){
			return generateOutput(json,"This User exist.",401,false);		
		} else {
			TableSpace.getInstance().setData(this.getStorageName(),tableKey, json);
			if(sessionCache != null)
				sessionCache.setSessionData(sessionID, "LoginUser", tableKey);
			//UserManager.getInstance().loginUser(tableKey,json);
			return generateOutput(null,null,204,true);
		}
	}
}
