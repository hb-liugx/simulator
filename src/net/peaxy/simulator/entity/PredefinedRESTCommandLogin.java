package net.peaxy.simulator.entity;

import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

import net.peaxy.simulator.data.TableSpace;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class PredefinedRESTCommandLogin extends PredefinedRESTCommand {

	private static final long serialVersionUID = -6718573908481761657L;

	public Output invoke(final MultivaluedMap<String, String> queryParameters, final MultivaluedMap<String, String> formParameters, final String sessionID, final String host) {
		SessionCache sessionCache = SessionCacheManager.getSessionCache(host);
		String result = "";
		String tableKey = findTableKey(formParameters,this.parameters);
		JSONObject json = TableSpace.getInstance().getData(this.getStorageName(),tableKey).toJson();
		boolean success = true;
		boolean isLock = false;//UserManager.getInstance().isLockUser(tableKey);		
		if(parameters == null || parameters.isEmpty()){
			return generateOutput(json,"The request is invalid.",401,false);
		}		
		if(isLock){
			result = "This user is locked.";
			return generateOutput(json,"This user is locked.",401,false);
		} else {
			Set<String> names = formParameters.keySet();
			for (String name : names) {
				CommandParameter parameter = this.parameters.get(name);
				if(parameter != null){
					String value = formParameters.getFirst(name);
					if(parameter.isSessional()){
						if(sessionCache != null)
							sessionCache.setSessionData(sessionID, name, value);
					}
					if(parameter.isComparable()){
						try {
							if(!value.equalsIgnoreCase(json.getString(name))){
								result = "User or Password Error.";
								success &= false;
								break;
							}
						} catch (JSONException e) {
							success &= false;
							break;
						}
					}
				}
			}
		}
		if(success){			
			if(sessionCache != null)
				sessionCache.setSessionData(sessionID, "LoginUser", tableKey);
			//UserManager.getInstance().loginUser(tableKey,json);
			return generateOutput(null,"",200,success);
		} else {
			//UserManager.getInstance().loginFailedUser(tableKey,json);
			return generateOutput(json,result,401,success);
		}
	}
}
