package com.valhallagame.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * Simple utility helper for converting stuff to http responses with a json object payload.
 */
@SuppressWarnings({"WeakerAccess", "OptionalUsedAsFieldOrParameterType", "unused"})
public class JS {

	private static ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.registerModule(new Jdk8Module());
		mapper.registerModule(new JavaTimeModule());
	}

	private JS() {
		//NO create, only static...
	}

	public static ResponseEntity<JsonNode> message(HttpStatus status, String message, Object... args) {
		return ResponseEntity.status(status).body(JS.message(String.format(message, args)));
	}

	public static ResponseEntity<JsonNode> message(HttpStatus status, Optional<?> message) {
		return message.isPresent() ? message(status, message.get())
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(JS.parse("Not Present"));
	}

	public static ResponseEntity<JsonNode> message(HttpStatus status, Object o) {
		if(o instanceof String) {
			o = message((String) o);
		}
		return ResponseEntity.status(status).body(JS.parse(o));
	}

	public static JsonNode message(String message) {
		return parse(new ObjectWrapperForString(message));
	}

	public static JsonNode parse(Object o) {
		return mapper.valueToTree(o);
	}

	private static class ObjectWrapperForString {
		private final String message;

		public ObjectWrapperForString(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	public static ResponseEntity<JsonNode> message(RestResponse<?> restResponse) {

		return restResponse.get().map(object -> {
			if (object instanceof ArrayNode) {
				// ensure that we never return an array.
				ObjectNode o = mapper.createObjectNode();
				o.set("result", (ArrayNode) object);
				return JS.message(HttpStatus.OK, o);
			} else if (object instanceof List) {
				ObjectNode o = mapper.createObjectNode();
				o.set("result", JS.parse(object));
				return JS.message(HttpStatus.OK, o);
			} else {
				return JS.message(HttpStatus.OK, object);
			}
		}).orElse(JS.message(restResponse.getStatusCode(), restResponse.getErrorMessage()));
	}
}
