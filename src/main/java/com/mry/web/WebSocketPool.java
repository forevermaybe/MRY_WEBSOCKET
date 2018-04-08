package com.mry.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WebSocketPool {
	private static Map<WebSocket, String> wsuserMap = new ConcurrentHashMap<WebSocket, String>(32);
	
	private static ObjectMapper mapper = new ObjectMapper();

	// 通过websocket找用户名
	public static String getUserByWs(WebSocket ws) {
		return wsuserMap.get(ws);
	}

	// 通过用户名找websocket
	public static WebSocket getWsByUser(String username) {
		Set<WebSocket> set = wsuserMap.keySet();
		for (WebSocket ws : set) {
			String cuser = wsuserMap.get(ws);
			if (cuser.equals(username)) {
				return ws;
			}
		}
		return null;
	}

	// 添加一个
	public static void adduser(String username, WebSocket ws) {
		wsuserMap.put(ws, username);
	}

	// 移除一个
	public static void removeuser(WebSocket conn) {
		wsuserMap.remove(conn);
	}

	// 发送给一个人
	public static void sendone(RespsonseResult result, Session session) throws IOException {
		session.getBasicRemote().sendText(mapper.writeValueAsString(result));
	}

	// 发送给所有人
	public static void sendall(RespsonseResult result, Session except) throws IOException {
		Set<WebSocket> websockets = wsuserMap.keySet();
		for (WebSocket ws : websockets) {
			sendone(result, ws.getSession());
		}
	}

	// 获得所有用户名
	public static List<String> getallusername() {
		List<String> usernames = new ArrayList<String>();
		Set<WebSocket> set = wsuserMap.keySet();
		for (WebSocket ws : set) {
			String cuser = wsuserMap.get(ws);
			usernames.add(cuser);
		}
		return usernames;
	}

}
