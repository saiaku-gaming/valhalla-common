package com.valhallagame.common;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

/**
 * Simple utility helper for converting stuff to json.
 */
public class JS {

	private static ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.registerModule(new Jdk8Module());
	}
	public JS() {

	}

	public static ResponseEntity<?> message(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(JS.message(message));
	}

	public static ResponseEntity<?> message(HttpStatus status, Optional<?> message) {
		return message.isPresent() ? ResponseEntity.status(status).body(JS.parse(message.get()))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(JS.parse("Not Present"));
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
			// Response should always be an object (not array or primitive);
			Object object = restResponse.getResponse().get();
			if (object instanceof ArrayNode) {
				ObjectNode o = mapper.createObjectNode();
				o.set("result", (ArrayNode) object);
				return JS.message(HttpStatus.OK, o);
			} else {
				return JS.message(HttpStatus.OK, object);
			}
		} else {
			return JS.message(restResponse.getStatusCode(), restResponse.getErrorMessage());
		}
	}
}
