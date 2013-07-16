/**
 * 
 */
package net.peaxy.simulator.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import net.peaxy.simulator.data.TableSpace;
import net.peaxy.simulator.util.Utility;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * @author Liang
 *
 */
public class PredefinedRESTCommand implements Serializable {
	private static final long serialVersionUID = 7348433496338574084L;

	protected HashMap<String, CommandParameter> parameters;
	
	public static class CommandParameter implements Serializable {
		private static final long serialVersionUID = 394042830174417600L;
		
		private String name;
		private String objectName;
		private boolean comparable;
		private boolean sessional;
		private boolean key;

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isComparable() {
			return this.comparable;
		}

		public void setComparable(boolean comparable) {
			this.comparable = comparable;
		}

		public boolean isSessional() {
			return this.sessional;
		}

		public void setSessional(boolean sessional) {
			this.sessional = sessional;
		}
		public boolean isKey() {
			return this.key;
		}

		public void setKey(boolean key) {
			this.key = key;
		}

		public String getObjectName() {
			return this.objectName;
		}

		public void setObjectName(String objectName) {
			this.objectName = objectName;
		}
	}

	private String name;
	private String method;
	private String endpoint;
	private String storageName;
	private boolean pesistent;
	private String source;
	private String sourceClass;
	private String successfulOutput;
	private String successfulOutputType;
	private String successfulOutputKey;
	private int successfulHttpCode;
	private String failedOutput;
	private String failedOutputType;
	private int failedHttpCode;
	
	protected Output generateOutput(final JSONObject json, final String result,final int status, final boolean success) {
		Output output = new Output(json);
		output.setHttpStatus(status);
		output.setResult(result);
		if(success) {
			if("json".equalsIgnoreCase(successfulOutputType)){
				output.setType(MediaType.APPLICATION_JSON_TYPE);				
			} else if("text".equalsIgnoreCase(successfulOutputType)){
				output.setType(MediaType.TEXT_PLAIN_TYPE);
			} else if("file".equalsIgnoreCase(successfulOutputType)){
				output.setType(MediaType.TEXT_PLAIN_TYPE);
				if(result != null){
					output.setResult(Utility.readFile(result));
				}
			}
		} else {
			if("json".equalsIgnoreCase(failedOutputType)){
				output.setType(MediaType.APPLICATION_JSON_TYPE);
			} else if("text".equalsIgnoreCase(failedOutputType)){
				output.setType(MediaType.TEXT_PLAIN_TYPE);
			} else if("file".equalsIgnoreCase(failedOutputType)){
				output.setType(MediaType.TEXT_PLAIN_TYPE);
				if(result != null){
					output.setResult(Utility.readFile(result));
				}
			}
		}
		return output;
	}

	public void saveCommandParam(MultivaluedMap<String, String> params){
		String tableKey = findTableKey(params,this.parameters);
		JSONObject json = new JSONObject();
		TableSpace.getInstance().setData(this.getStorageName(),tableKey, json);
	}
	
	public Output invoke(final MultivaluedMap<String, String> queryParameters, final String sessionID, final String host) {
		SessionCache sessionCache = SessionCacheManager.getSessionCache(host);
		String value = "";
		String tableKey = findTableKey(queryParameters,this.parameters);
		JSONObject json = TableSpace.getInstance().getData(this.getStorageName(),tableKey).toJson();
		if(parameters == null || parameters.isEmpty()){
			return generateOutput(json,null,200,true);
		}
		Set<String> names = queryParameters.keySet();
		for (String name : names) {
			CommandParameter parameter = this.parameters.get(name);
			if(parameter != null){
				value = queryParameters.getFirst(name);
				if (parameter.isSessional()){
					sessionCache.setSessionData(sessionID, name, value);
				}
				try {
					json.put(name, value);
				} catch (JSONException e) {
				}
			}
		}
		TableSpace.getInstance().setData(this.getStorageName(),tableKey, json);
		return generateOutput(json,null,200,true);
	}

	public Output invoke(final MultivaluedMap<String, String> queryParameters, final MultivaluedMap<String, String> formParameters, final String sessionID, final String host) {
		SessionCache sessionCache = SessionCacheManager.getSessionCache(host);
		String tableKey = findTableKey(formParameters,this.parameters);
		JSONObject json = TableSpace.getInstance().getData(this.getStorageName(),tableKey).toJson();
		if(parameters == null || parameters.isEmpty()){
			return generateOutput(json,json.toString(),200,true);
		}
		String value;
		Set<String> names = formParameters.keySet();		
		for (String name : names) {
			CommandParameter parameter = this.parameters.get(name);
			if(parameter != null){				
				value = formParameters.getFirst(name);
				if (parameter.isSessional()){
					sessionCache.setSessionData(sessionID, name, value);
				}
				if(parameter.getObjectName() != null){
					try {
						JSONObject obj ;
						if(json.has(parameter.getObjectName())){
							obj = new JSONObject(json.get(parameter.getObjectName()).toString());
						}  else {
							obj = new JSONObject();
						}
						obj.put(name, value);
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
	
	protected String findTableKey(MultivaluedMap<String, String> query,HashMap<String, CommandParameter> params){
		String key = "__internal__";
		if(params != null && !params.isEmpty()){
			Set<String> names = query.keySet();
			for(String name : names){
				CommandParameter param = params.get(name);
				if(param != null && param.isKey()){
					key = query.getFirst(name);
					break;
				}
			}
		}
		return key;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMethod() {
		return this.method;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
	
	public String getEndpoint() {
		return this.endpoint;
	}
	
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	public String getStorageName() {
		return this.storageName;
	}
	
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
	
	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceClass() {
		return this.sourceClass;
	}

	public void setSourceClass(String sourceClass) {
		this.sourceClass = sourceClass;
	}

	public String getSuccessfulOutput() {
		return this.successfulOutput;
	}

	public void setSuccessfulOutput(String successfulOutput) {
		this.successfulOutput = successfulOutput;
	}
	
	public String getSuccessfulOutputType() {
		return this.successfulOutputType;
	}

	public void setSuccessfulOutputType(String successfulOutputType) {
		this.successfulOutputType = successfulOutputType;
	}

	public String getSuccessfulOutputKey() {
		return this.successfulOutputKey;
	}
	
	public void setSuccessfulOutputKey(String successfulOutputKey) {
		this.successfulOutputKey = successfulOutputKey;
	}
	
	public int getSuccessfulHttpCode() {
		return this.successfulHttpCode;
	}
	
	public void setSuccessfulHttpCode(int successfulHttpCode) {
		this.successfulHttpCode = successfulHttpCode;
	}
	
	public String getFailedOutput() {
		return this.failedOutput;
	}

	public void setFailedOutput(String failedOutput) {
		this.failedOutput = failedOutput;
	}

	public String getFailedOutputType() {
		return this.failedOutputType;
	}

	public void setFailedOutputType(String failedOutputType) {
		this.failedOutputType = failedOutputType;
	}

	public int getFailedHttpCode() {
		return this.failedHttpCode;
	}
	
	public void setFailedHttpCode(int failedHttpCode) {
		this.failedHttpCode = failedHttpCode;
	}
	
	public void initParamaeter(){
		if(this.parameters == null){
			this.parameters = new HashMap<String, PredefinedRESTCommand.CommandParameter>();
		}
	}
	public CommandParameter getParameter(String name) {
		return this.parameters.get(name);
	}
	
	public Set<String> getParameterNames() {
		return this.parameters.keySet();
	}
	
	public void addParameter(CommandParameter parameter) {
		this.parameters.put(parameter.getName(), parameter);
	}

	public boolean isPesistent() {
		return this.pesistent;
	}

	public void setPesistent(boolean pesistent) {
		this.pesistent = pesistent;
	}
}
