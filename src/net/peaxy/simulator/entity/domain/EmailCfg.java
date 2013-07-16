package net.peaxy.simulator.entity.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class EmailCfg extends ConfigData {
	public static final int SCHEMA_VERSION = 1;

	private String emailProtocol;
	private String emailServer;
	private int emailPort;
	private String emailLogin;
	private String emailPassword;
	private String emailFrom;

	public EmailCfg() {
		setType(ConfigDataType.EmailCfg);
		setSchemaVersion(SCHEMA_VERSION);
	}

	@JsonProperty("protocol")
	public String getEmailProtocol() {
		return emailProtocol;
	}

	public void setEmailProtocol(String emailProtocol) {
		this.emailProtocol = emailProtocol;
	}

	@JsonProperty("server")
	public String getEmailServer() {
		return emailServer;
	}

	public void setEmailServer(String emailServer) {
		this.emailServer = emailServer;
	}

	@JsonProperty("port")
	public int getEmailPort() {
		return emailPort;
	}

	public void setEmailPort(int emailPort) {
		this.emailPort = emailPort;
	}

	@JsonProperty("login")
	public String getEmailLogin() {
		return emailLogin;
	}

	public void setEmailLogin(String emailLogin) {
		this.emailLogin = emailLogin;
	}

	@JsonProperty("password")
	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	@JsonProperty("from")
	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	@Override
	public String toString() {
		return "EmailCfg [emailProtocol=" + emailProtocol + ", emailServer="
				+ emailServer + ", emailPort=" + emailPort + ", emailLogin="
				+ emailLogin + ", emailPassword=" + emailPassword
				+ ", emailFrom=" + emailFrom + "]";
	}
}
