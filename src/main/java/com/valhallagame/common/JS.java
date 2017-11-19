package com.valhallagame.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Simple utility helper for converting stuff to json.
 */
public class JS {

	private static ObjectMapper mapper = new ObjectMapper();

	public JS() {

	}

	public static ResponseEntity<?> message(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(JS.message(message));
	}

	public static ResponseEntity<?> message(HttpStatus status, Object o) {
		return ResponseEntity.status(status).body(JS.parse(o));
	}

	public static JsonMessage message(String message) {
		return new JsonMessage(message);
	}

	public static JsonNode parse(Object o) {
		return mapper.valueToTree(o);
	}

	public static class JsonMessage {
		private String message;

		public JsonMessage() {
		}

		public JsonMessage(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

	public static ResponseEntity<?> message(RestResponse<?> restResponse) {
		if (restResponse.isOk()) {
			return JS.message(HttpStatus.OK, restResponse.getResponse().get());
		} else {
			return JS.message(restResponse.getStatusCode(), restResponse.getErrorMessage());
		}
	}
}
