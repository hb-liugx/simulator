package net.peaxy.simulator.entity;

import javax.ws.rs.core.MultivaluedMap;

import net.peaxy.simulator.data.TableSpace;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class PredefinedRESTCommandHS extends PredefinedRESTCommand {

	private static final long serialVersionUID = -76024608616253685L;

	@Override 
	public Output invoke(final MultivaluedMap<String, String> queryParameters, final String sessionID, final String host) {
		return super.invoke(queryParameters, sessionID, host);
	}
	
	@Override
	public Output invoke(final MultivaluedMap<String, String> queryParameters, final MultivaluedMap<String, String> formParameters, final String sessionID, final String host) {
		String tableKey = findTableKey(formParameters,this.parameters);
		JSONObject json = TableSpace.getInstance().getData(this.getStorageName(),tableKey).toJson();
		try {
			json.put("type", "HsCfg");			
		} catch (JSONException e) {
		}
		TableSpace.getInstance().setData(this.getStorageName(),tableKey, json);
		return super.invoke(queryParameters, formParameters, sessionID, host);
	}
}
