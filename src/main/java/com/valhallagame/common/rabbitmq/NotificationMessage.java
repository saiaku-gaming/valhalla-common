package com.valhallagame.common.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
    private String username;
    private final Map<String, Object> data = new HashMap<>();

    public NotificationMessage(String username, String reason) {
        this.username = username;
        data.put("reason", reason);
    }

    public void addData(String key, Object value) {
        data.put(key, value);
    }

    public void addData(Map<String, Object> data) {
        this.data.putAll(data);
    }

    public NotificationMessage withData(String key, Object value) {
        addData(key, value);
        return this;
    }

    public NotificationMessage withData(Map<String, Object> data) {
        addData(data);
        return this;
    }
}
