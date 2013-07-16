package net.peaxy.simulator.entity;

import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

import net.peaxy.simulator.data.TableSpace;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class PredefinedRESTCommandLog extends PredefinedRESTCommand {

	private static final long serialVersionUID = 2686129215391221279L;

	@Override 
	public Output invoke(final MultivaluedMap<String, String> queryParameters, final String sessionID, final String host) {
		return super.invoke(queryParameters, sessionID, host);
	}
	
	@Override
	public Output invoke(final MultivaluedMap<String, String> queryParameters, final MultivaluedMap<String, String> formParameters, final String sessionID, final String host) {
		SessionCache sessionCache = SessionCacheManager.getSessionCache(host);
		String tableKey = findTableKey(formParameters,this.parameters);
		JSONObject json = TableSpace.getInstance().getData(this.getStorageName(),tableKey).toJson();
		if(parameters == null || parameters.isEmpty()){
			return generateOutput(json,json.toString(),200,true);
		}
		String value;
		Set<String> names = formParameters.keySet();
		JSONArray obj ;
		for (String name : names) {
			CommandParameter parameter = this.parameters.get(name);
			if(parameter != null){				
				value = formParameters.getFirst(name);
				if (parameter.isSessional()){
					sessionCache.setSessionData(sessionID, name, value);
				}
				if(parameter.getObjectName() != null){
					try {
						
						if(json.has(parameter.getObjectName())){
							obj = new JSONArray(json.get(parameter.getObjectName()).toString());
							//obj = new JSONArray();
						}  else {
							obj = new JSONArray();
						}
						obj.put(obj.length(),new JSONObject().put(name, value));
						json.put(parameter.getObjectName(), obj);
					} catch (JSONException e) {
						e.printStackTrace();
					}					
				} else {
					try {
						json.put(name, value);
					} catch (JSONException e) {
					}
				}
			}
		}		
		TableSpace.getInstance().setData(this.getStorageName(),tableKey, json);
		return generateOutput(null,null,204,true);
	}
}
