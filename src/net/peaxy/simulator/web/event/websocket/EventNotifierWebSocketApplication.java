/**
 * 
 */
package net.peaxy.simulator.web.event.websocket;

import java.util.Set;
import java.util.regex.Pattern;

import net.peaxy.simulator.entity.PredefinedEvent;
import net.peaxy.simulator.web.WebServerEvent;
import net.peaxy.simulator.web.WebServerEventAgent;
import net.peaxy.simulator.web.event.EventNotification;
import net.peaxy.simulator.web.event.EventNotifier;

import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;

/**
 * @author Liang
 *
 */
public final class EventNotifierWebSocketApplication extends WebSocketApplication implements EventNotifier {
	private static final String requestURIStart = "^/event/";
	private static final String requestURIEnd = "/register$";
	private static final String requestURI = "^/event/\\w+[.]\\w+[.]\\w+/register$";
	
	private WebServerEventAgent webServerEventAgent;
	
	public EventNotifierWebSocketApplication() {
		this.webServerEventAgent = new WebServerEventAgent() {
			public void act(WebServerEvent event) {}
		};
	}
	
	public void setWebServerEventAgent(WebServerEventAgent eventAgent) {
		if (eventAgent != null)
			this.webServerEventAgent = eventAgent;
	}
	
	public void clearWebServerEventAgent() {
		this.webServerEventAgent = new WebServerEventAgent() {
			public void act(WebServerEvent event) {}
		};
	}
	
	@Override
	public boolean isApplicationRequest(HttpRequestPacket request) {
		this.webServerEventAgent.act(new WebServerEvent(request));
		String uri = request.getRequestURI();
		if (Pattern.matches(requestURI, uri))
			return PredefinedEvent.isSupported(uri.replaceAll(requestURIStart, "").replaceAll(requestURIEnd, ""));
		return false;
	}

	@Override
	public void onMessage(WebSocket socket, String text) {
		this.broadcast(Thread.currentThread().getId() + ": " + text);
	}
	
	public void notify(EventNotification notification) {
		this.broadcast(notification.getData());
	}
	
	private void broadcast(String data) {
		Set<WebSocket> sockets = this.getWebSockets();
		for (WebSocket socket : sockets)
			socket.send(data);
	}
}
