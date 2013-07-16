/**
 * 
 */
package net.peaxy.simulator.web;


import org.glassfish.grizzly.http.server.HttpHandler;

import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.ContainerProvider;
import com.sun.jersey.spi.container.WebApplication;

/**
 * @author Liang
 *
 */
public class WebServerContainerPrivider implements ContainerProvider<HttpHandler> {
	public WebServerContainerPrivider() {
		@SuppressWarnings("unused")
        final Class<?> c = HttpHandler.class;
	}

	public WebServerContainer createContainer(Class<HttpHandler> type, ResourceConfig resourceConfig, WebApplication application) throws ContainerException {
		if (type != HttpHandler.class) return null;
		return new WebServerContainer(resourceConfig, application);
	}
}
