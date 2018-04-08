package com.mry.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mry.web.Result.Type;

@ServerEndpoint("/websocket")
public class WebSocket {

	private static ObjectMapper mapper = new ObjectMapper();

	private Session session;
	private String username;

	@OnMessage
	public void onMessage(String message, Session session) {
		Result result = null;
		try {
			result = mapper.readValue(message, Result.class);
		} catch (IOException e) {
			System.out.println("json反序列化失败");
			e.printStackTrace();
		}
		chat(result);
	}

	private void chat(Result result) {
		String aim = "";
		if (result.getType() == Type.CHAT) {
			String touser = result.getTo();
			WebSocket to = WebSocketPool.getWsByUser(touser);
			if (to == null) {
				// 对发送者
				aim = "用户不存在</br>";
				RespsonseResult resonseresult = new RespsonseResult();
				resonseresult.setMessage(aim);
				resonseresult.setType(RespsonseResult.Type.COMMON);
				try {
					WebSocketPool.sendone(resonseresult, session);
				} catch (IOException e) {
					System.out.println("单发失败");
					e.printStackTrace();
				}
			} else {
				Session toseesion = to.getSession();
				// 对接受者
				aim = username + "悄悄对你说:</br>" + result.getMessage();
				toseesion = to.getSession();
				RespsonseResult resonseresult = new RespsonseResult();
				resonseresult.setMessage(aim);
				resonseresult.setType(RespsonseResult.Type.COMMON);
				try {
					WebSocketPool.sendone(resonseresult, toseesion);
				} catch (IOException e) {
					System.out.println("单发失败");
					e.printStackTrace();
				}
				// 对发送者
				aim = "你悄悄对" + to.getUsername() + "说:</br>" + result.getMessage();
				resonseresult.setMessage(aim);
				resonseresult.setType(RespsonseResult.Type.COMMON);
				try {
					WebSocketPool.sendone(resonseresult, session);
				} catch (IOException e) {
					System.out.println("单发失败");
					e.printStackTrace();
				}
			}
		} else if (result.getType() == Type.TOALL) {
			try {
				aim = username + "说:</br>" + result.getMessage();
				RespsonseResult resonseresult = new RespsonseResult();
				resonseresult.setMessage(aim);
				resonseresult.setType(RespsonseResult.Type.COMMON);
				WebSocketPool.sendall(resonseresult, null);
			} catch (IOException e) {
				System.out.println("群发失败");
				e.printStackTrace();
			}
		}
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Client connected");
		this.session = session;
		Map<String, List<String>> params = session.getRequestParameterMap();
		this.username = params.get("username").get(0);
		String message = username + "来了";
		WebSocketPool.adduser(username, this);
		RespsonseResult resonseresult = new RespsonseResult();
		resonseresult.setMessage(message);
		resonseresult.setType(RespsonseResult.Type.LOGIN);
		resonseresult.setUsername(this.username);
		resonseresult.setTousers(WebSocketPool.getallusername());
		try {
			WebSocketPool.sendall(resonseresult, this.session);
		} catch (IOException e) {
			System.out.println("登录群发失败");
			e.printStackTrace();
		}
	}

	@OnClose
	public void onClose() {
		WebSocketPool.removeuser(this);
		RespsonseResult resonseresult = new RespsonseResult();
		String message = username + "已退出";
		resonseresult.setMessage(message);
		resonseresult.setType(RespsonseResult.Type.LOGOUT);
		resonseresult.setTousers(WebSocketPool.getallusername());
		try {
			WebSocketPool.sendall(resonseresult, this.session);
		} catch (IOException e) {
			System.out.println("登出群发失败");
			e.printStackTrace();
		}
		System.out.println("Connection closed");
	}

	@OnError
	public void onerror(Throwable throwable) {
		WebSocketPool.removeuser(this);
		RespsonseResult resonseresult = new RespsonseResult();
		String message = username + "已退出";
		resonseresult.setMessage(message);
		resonseresult.setType(RespsonseResult.Type.LOGOUT);
		resonseresult.setTousers(WebSocketPool.getallusername());
		try {
			WebSocketPool.sendall(resonseresult, this.session);
		} catch (IOException e) {
			System.out.println("登出群发失败");
			e.printStackTrace();
		}
		System.out.println("Connection closed");
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
