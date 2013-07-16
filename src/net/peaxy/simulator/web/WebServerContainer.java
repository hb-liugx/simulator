/**
 * 
 */
package net.peaxy.simulator.web;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.utils.Charsets;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.server.impl.ThreadLocalInvoker;
import com.sun.jersey.spi.container.ContainerListener;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseWriter;
import com.sun.jersey.spi.container.ReloadListener;
import com.sun.jersey.spi.container.WebApplication;
import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;

/**
 * @author Liang
 *
 */
public final class WebServerContainer extends HttpHandler implements ContainerListener {
	private volatile WebApplication application;
	private final ThreadLocalInvoker<Request> requestInvoker = new ThreadLocalInvoker<Request>();
	private final ThreadLocalInvoker<Response> responseInvoker = new ThreadLocalInvoker<Response>();
	private static WebServerSessionManager sessonManager;
	private WebServerEventAgent webServerEventAgent;
	
	WebServerContainer(final ResourceConfig resourceConfig, final WebApplication application) {
		this.application = application;
		this.setAllowEncodedSlash(resourceConfig.getFeature("com.sun.jersey.api.container.grizzly.AllowEncodedSlashFeature"));
		final GenericEntity<ThreadLocal<Request>> requestThreadLocal = new GenericEntity<ThreadLocal<Request>>(this.requestInvoker.getImmutableThreadLocal()) {};
        resourceConfig.getSingletons().add(new ContextInjectableProvider<ThreadLocal<Request>>(requestThreadLocal.getType(), requestThreadLocal.getEntity()));
        final GenericEntity<ThreadLocal<Response>> responseThreadLocal = new GenericEntity<ThreadLocal<Response>>(this.responseInvoker.getImmutableThreadLocal()) {};
        resourceConfig.getSingletons().add(new ContextInjectableProvider<ThreadLocal<Response>>(responseThreadLocal.getType(), responseThreadLocal.getEntity()));
        WebServerContainer.sessonManager = new WebServerSessionManager();
        this.webServerEventAgent = new WebServerEventAgent() {
			public void act(WebServerEvent event) {	}
        };
	}
	
	public static WebServerSessionManager getSessionManager() {
		return sessonManager;
	}
	
	public void setWebServerEventAgent(WebServerEventAgent eventAgent) {
		if (eventAgent != null)
			this.webServerEventAgent = eventAgent;
	}
	
	public void clearWebServerEventAgent() {
		this.webServerEventAgent = new WebServerEventAgent() {
			public void act(WebServerEvent event) {	}
        };
	}
	
	public void onReload() {
        final WebApplication oldApplication = this.application;
        this.application = this.application.clone();
        if (this.application.getFeaturesAndProperties() instanceof ReloadListener)
            ((ReloadListener)this.application.getFeaturesAndProperties()).onReload();
        oldApplication.destroy();
    }
	
	@Override
	public void service(final Request request, final Response response) {
        try {
            this.requestInvoker.set(request);
            this.responseInvoker.set(response);
            this._service(request, response);
        } finally {
            this.requestInvoker.set(null);
            this.responseInvoker.set(null);
        }
    }
	
	private void _service(final Request request, final Response response) {
        final WebApplication _application = this.application;
        final URI baseUri = this.getBaseUri(request);
        String originalURI = UriBuilder.fromPath(request.getRequest().getRequestURIRef().getOriginalRequestURIBC().toString(Charsets.DEFAULT_CHARSET)).build().toString();
        String queryString = request.getQueryString();
        
        if (queryString != null)
            originalURI = originalURI + "?" + queryString;
        try {
        	final URI requestUri = baseUri.resolve(originalURI);
            final ContainerRequest cRequest = new ContainerRequest(_application, request.getMethod().getMethodString(), baseUri, requestUri, this.getHeaders(request), request.getInputStream());
            Cookie cookie = cRequest.getCookies().get("JSESSIONID");
            if (cookie == null || cookie.getValue().isEmpty())
            	WebServerContainer.sessonManager.start(request.getSession().getIdInternal());
            else
            	WebServerContainer.sessonManager.refresh(cookie.getValue());
            WebServerEvent webServerEvent = new WebServerEvent(request);
            if (request.getMethod().equals(Method.POST))
            	webServerEvent.setPostData(cRequest.getFormParameters().entrySet());
            this.webServerEventAgent.act(webServerEvent);
            final Writer writer = new Writer(response);
            _application.handleRequest(cRequest, writer);            
        } catch (final Exception ex) {
        	ex.printStackTrace();
            //throw new RuntimeException(ex);
        }
    }
	
	private URI getBaseUri(final Request request) {
        try {
            return new URI(request.getScheme(), null, request.getServerName(), request.getServerPort(), getBasePath(request), request.getQueryString(), null);
        } catch (final URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
	
	private String getBasePath(final Request request) {
        final String contextPath = request.getContextPath();

        if (contextPath == null || contextPath.length() == 0) return "/";
        if (contextPath.charAt(contextPath.length() - 1) != '/') return contextPath + "/";
        return contextPath;
    }
	
	 private static class ContextInjectableProvider<T> extends SingletonTypeInjectableProvider<Context, T> {
		 protected ContextInjectableProvider(final Type type, final T instance) {
			 super(type, instance);
	     }
	 }
	 
	 private InBoundHeaders getHeaders(final Request request) {
	        final InBoundHeaders rh = new InBoundHeaders();
	        Iterable<String> names = request.getHeaderNames();
	        for (final String name : names)
	            rh.add(name, request.getHeader(name));
	        return rh;
	    }
	 
	 private final static class Writer implements ContainerResponseWriter {
	        final Response response;

	        Writer(final Response response) {
	            this.response = response;
	        }

	        public void finish() throws IOException {
	        }

	        public OutputStream writeStatusAndHeaders(final long contentLength, final ContainerResponse cResponse) throws IOException {
	            this.response.setStatus(cResponse.getStatus());
	            if (contentLength != -1 && contentLength < Integer.MAX_VALUE)
	                this.response.setContentLength((int) contentLength);
	            Set<Map.Entry<String, List<Object>>> entries = cResponse.getHttpHeaders().entrySet();
	            for (final Map.Entry<String, List<Object>> entry : entries) {
	            	List<Object> values = entry.getValue();
	                for (final Object value : values)
	                    this.response.addHeader(entry.getKey(), ContainerResponse.getHeaderValue(value));
	            }

	            final String contentType = this.response.getHeader("Content-Type");
	            if (contentType != null)
	                this.response.setContentType(contentType);
	            return this.response.getOutputStream();
	        }
	    }
}
