/**
 * 
 */
package net.peaxy.simulator.web;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.util.DataChunk;

/**
 * @author Liang
 *
 */
public final class WebServerEvent implements Serializable {
	private static final long serialVersionUID = 4043038759908847503L;

	private long timestamp;
	private String method;
	private String scheme;
	private String host;
	private int port;
	private String uri;
	private String protocal;
	private String query;
	private String postData;
	
	public WebServerEvent(final HttpRequestPacket request) {
		this.timestamp = (new Date()).getTime();
		this.method = request.getMethod().getMethodString();
		this.scheme = "ws";
		this.host = request.getLocalName();
		this.port = request.getServerPort();
		DataChunk header = request.getHeaders().getValue("Host");
		if (header != null) {
			String[] headerHost = header.toString().split(":");
			if (headerHost.length > 0) this.host = headerHost[0];
			if (headerHost.length > 1) this.port = Integer.parseInt(headerHost[1]);
		}
		this.uri = request.getRequestURI();
		this.protocal = request.getProtocolString().replaceFirst("_", "").replaceFirst("_", ".");
		this.query = request.getQueryString();
	}
	
	public WebServerEvent(final Request request) {
		this.timestamp = (new Date()).getTime();
		this.method = request.getMethod().getMethodString();
		this.scheme = request.getScheme();
		this.host = request.getServerName();
		this.port = request.getServerPort();
		this.uri = request.getRequestURI();
		this.protocal = request.getProtocol().name().replaceFirst("_", "/").replaceFirst("_", ".");
		this.query = request.getQueryString();
	}
	
	public long getTimestamp() {
		return this.timestamp;
	}

	public String getMethod() {
		return this.method;
	}

	public String getScheme() {
		return this.scheme;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	public String getUri() {
		return this.uri;
	}

	public String getProtocal() {
		return this.protocal;
	}

	public String getQuery() {
		return this.query;
	}

	public String getPostData() {
		return this.postData;
	}

	public void setPostData(final Set<Entry<String, List<String>>> postForm) {
		StringBuilder stringBuilder = new StringBuilder();
		JSONObject json;
		for (Entry<String, List<String>> entry : postForm) {
			json = new JSONObject();
			try {
				json.put(entry.getKey(), entry.getValue());
			} catch (JSONException e) {}
			stringBuilder.append(json.toString());
		}
		this.postData = stringBuilder.toString().replaceAll("\\}\\{", ", ").replaceAll("\\{", "").replaceAll("\\}", "");
	}
}
