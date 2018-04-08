package com.mry.web;

import java.util.List;

public class RespsonseResult {
	private Type type;
	private String message;
	private String username;
	private List<String> tousers;
	

	public Type getType() {
		return type;
	}


	public void setType(Type type) {
		this.type = type;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public List<String> getTousers() {
		return tousers;
	}


	@Override
	public String toString() {
		return "RespsonseResult [type=" + type + ", message=" + message + ", username=" + username + ", tousers="
				+ tousers + "]";
	}


	public void setTousers(List<String> tousers) {
		this.tousers = tousers;
	}


	public enum Type {
		LOGIN(0), COMMON(1),LOGOUT(2);
		public final int num;

		private Type(int num) {
			this.num = num;
		}

		public static Type valueof(int num) {
			Type aim = null;
			switch (num) {
			case 0:
				aim = LOGIN;
				break;
			case 1:
				aim = COMMON;
				break;
			case 2:
				aim = LOGOUT;
				break;
			}
			return aim;
		}
	}
}
