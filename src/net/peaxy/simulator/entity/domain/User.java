package net.peaxy.simulator.entity.domain;

import net.peaxy.simulator.util.Utility;

import org.codehaus.jackson.annotate.JsonProperty;

public class User extends PeaxyBaseDomain {

	private String name = "";
	private String password = "";
	private String email = "";
	private UserRole role;
	private String token = "";

	private boolean lock;

	public User() {
		this.token = Utility.generateSN(16);
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty("role")
	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	@JsonProperty("token")
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}

	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}
}
