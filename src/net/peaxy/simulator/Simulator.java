package net.peaxy.simulator;

import java.util.Date;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.peaxy.simulator.conf.RuntimeParameter;
import net.peaxy.simulator.ui.MainFrame;
import net.peaxy.simulator.ui.MainFrameMenuActionListener;
import net.peaxy.simulator.web.WebServer;
import net.peaxy.simulator.web.WebServerEvent;
import net.peaxy.simulator.web.WebServerObservable;
import net.peaxy.simulator.web.WebServerObserver;
import net.peaxy.simulator.xml.CommandListRead;

public final class Simulator implements WebServerObserver, MainFrameMenuActionListener, Runnable {
	public static void main(String[] args) throws Throwable {
		System.setProperty("java.util.logging.config.file", "conf/log.conf");
		RuntimeParameter.load("conf/web.conf");
		new CommandListRead().read("conf/command_list.xml");
		new Simulator().start();
	}
	
	private MainFrame mainFrame;
	private WebServer webServer;
	
	private Simulator() throws Throwable {
		this(false);
	}
	
	private Simulator(boolean gui) throws Throwable {
		String host = RuntimeParameter.get("simulator.web.host", "localhost");
		int port = RuntimeParameter.get("simulator.web.port", 8080);
		String path = RuntimeParameter.get("simulator.web.path", "/");
		boolean websocket = RuntimeParameter.get("simulator.web.websocket", true);
		this.webServer = new WebServer(host, port, path, websocket);
		this.webServer.addObserver(this);
	}
	
	public void run() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			this.mainFrame = new MainFrame(this);
			//this.mainFrame.setVisible(true);
			this.webServer.start();
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}
	
	public void start() {
		SwingUtilities.invokeLater(this);
	}
	
	private void clearup() {
		this.webServer.stop();
		this.mainFrame.dispose();
	}
	
	/* (non-Javadoc)
	 * @see net.peaxy.simulator.ui.MainFrameMenuActionListener#onActionPerformed(java.lang.String)
	 */
	public void onActionPerformed(String menuName) {
		if (menuName.equals("exit")) {
			this.clearup();
			System.exit(0);
		}
		if (menuName.equals("start")) {
			try {
				this.webServer.start();
			} catch (Throwable t) {
				t.printStackTrace();
			}
			return;
		}
		if (menuName.equals("stop")) {
			this.webServer.stop();
			return;
		}
	}
	
	public void update(WebServerObservable observable, WebServerEvent data) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(new Date(data.getTimestamp()));
		stringBuilder.append("\t");
		stringBuilder.append(data.getProtocal());
		stringBuilder.append("\t");
		stringBuilder.append(data.getMethod());
		stringBuilder.append("\t");
		stringBuilder.append(data.getScheme());
		stringBuilder.append("://");
		stringBuilder.append(data.getHost());
		int port = data.getPort();
		if (port != -1 && port != 80) {
			stringBuilder.append(":");
			stringBuilder.append(port);
		}
		stringBuilder.append(data.getUri());		
		String query = data.getQuery();
		if (query != null) {
			stringBuilder.append("\t");
			stringBuilder.append(query);
		}
		String postData = data.getPostData();
		if (postData != null) {
			stringBuilder.append("\t");
			stringBuilder.append(postData);
		}
	}
}
