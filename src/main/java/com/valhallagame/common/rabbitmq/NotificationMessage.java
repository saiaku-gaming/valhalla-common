package com.valhallagame.common.rabbitmq;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
	private String username;
	private String reason;
	private Map<String, Object> data = new HashMap<>();

	public NotificationMessage(String username, String reason) {
		this.username = username;
		this.reason = reason;
	}

	public void addData(String key, Object value) {
		data.put(key, value);
	}
}
