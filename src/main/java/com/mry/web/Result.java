package com.mry.web;

public class Result {

	private Type type;

	private String from;

	private String to;

	private String message;

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

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return "{\"type\":\"" + type + "\", \"from\":\"" + from + "\", \"to:\"" + to + "\", \"message:\"" + message
				+ "\"}";
	}

	public enum Type {
		LOGIN(0), CHAT(1),TOALL(2);
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
				aim = CHAT;
				break;
			case 2:
				aim =TOALL;
				break;
			}
			return aim;
		}

	}
}
