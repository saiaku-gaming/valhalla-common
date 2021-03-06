package com.valhallagame.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.ConnectException;

@SuppressWarnings({"WeakerAccess", "unused", "Duplicates"})
public class RestCaller {

	private static final Logger logger = LoggerFactory.getLogger(RestCaller.class);

	private OkHttpClient client;
	private ObjectMapper objectMapper;
	private boolean logging;

	public RestCaller() {
		client = new OkHttpClient();
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(new Jdk8Module());
		objectMapper.registerModule(new JavaTimeModule());
		logging = false;
	}

	public RestCaller(boolean logging) {
		this();
		this.logging = logging;
	}

	public <T> RestResponse<T> getCall(String url, Class<T> responseClass) throws IOException {
		Response response = get(url);

		RestResponse<T> restResponse = new RestResponse<>();

		if (response.code() == 200) {
			restResponse.setResponse(extractResponseBody(response, responseClass));
		} else {
			restResponse.setErrorMessage(extractResponseBody(response, StringResponse.class).getMessage());
		}
		restResponse.setStatusCode(HttpStatus.valueOf(response.code()));
		return restResponse;
	}

	public <T> RestResponse<T> getCall(String url, TypeReference<T> typeReference) throws IOException {
		Response response = get(url);

		RestResponse<T> restResponse = new RestResponse<>();

		if (response.code() == 200) {
			restResponse.setResponse(extractResponseBody(response, typeReference));
		} else {
			restResponse.setErrorMessage(extractResponseBody(response, StringResponse.class).getMessage());
		}
		restResponse.setStatusCode(HttpStatus.valueOf(response.code()));
		return restResponse;
	}

	private Response get(String url) throws IOException {
		Request.Builder builder = new Request.Builder().url(url);

		if(MDC.getMDCAdapter() != null && MDC.get("request_id") != null) {
			builder.addHeader("X-REQUEST-ID", MDC.get("request_id"));
		}

		Request request = builder.get().build();
		return client.newCall(request).execute();
	}

	@SuppressWarnings("unchecked")
	public <T> RestResponse<T> postCall(String url, Object requestBody, Class<T> responseClass) throws IOException {
		try {
			Response response = post(url, requestBody);

			RestResponse<T> restResponse = new RestResponse<>();

			if (response.code() == 200) {
				if (String.class.equals(responseClass)) {
					restResponse.setResponse((T) extractResponseBody(response, StringResponse.class).getMessage());
				} else {
					restResponse.setResponse(extractResponseBody(response, responseClass));
				}
			} else {
				restResponse.setErrorMessage(extractResponseBody(response, StringResponse.class).getMessage());
			}

			restResponse.setStatusCode(HttpStatus.valueOf(response.code()));
			return restResponse;
		} catch (ConnectException e) {
			throw new IOException("Could not access url: " + url, e);
		} catch (Exception e) {
			throw new IOException("Error (" + e.getMessage() + ") occurred when calling: " + url + " with request body " + requestBody, e);
		}
	}

	public <T> RestResponse<T> postCall(String url, Object requestBody, TypeReference<T> typeReference)
			throws IOException {
		try {
			Response response = post(url, requestBody);

			RestResponse<T> restResponse = new RestResponse<>();
			if (response.code() == 200) {
				restResponse.setResponse(extractResponseBody(response, typeReference));
			} else {
				restResponse.setErrorMessage(extractResponseBody(response, StringResponse.class).getMessage());
			}
			restResponse.setStatusCode(HttpStatus.valueOf(response.code()));
			return restResponse;
		} catch (ConnectException e) {
			throw new IOException("Could not access url: " + url, e);
		} catch (Exception e) {
			throw new IOException("Strange things are happening here: " + url, e);
		}
	}


	private <T> T extractResponseBody(Response response, Class<T> responseClass) throws IOException {
		String body = response.body() == null ? null : response.body().string();
		if (logging) {
			logger.info(body);
		}
		return objectMapper.readValue(body, responseClass);
	}

	private <T> T extractResponseBody(Response response, TypeReference<T> typeReference) throws IOException {
		String body = response.body() == null ? null : response.body().string();
		if (logging) {
			logger.info(body);
		}
		return objectMapper.readValue(body, typeReference);
	}

	private Response post(String url, Object requestBody) throws IOException {
		RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
				objectMapper.writeValueAsString(requestBody));

		Request.Builder builder = new Request.Builder().url(url);

		if(MDC.getMDCAdapter() != null && MDC.get("request_id") != null) {
			builder.addHeader("X-REQUEST-ID", MDC.get("request_id"));
		}

		Request request = builder.post(body).build();
		return client.newCall(request).execute();
	}

}
