package com.valhallagame.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.ConnectException;

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
		Request request = new Request.Builder().url(url).get().build();
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
			throw new IOException("Strange things are happening here: " + url, e);
		}
	}

	private <T> T extractResponseBody(Response response, Class<T> responseClass) throws IOException {
		if (logging) {
			String res = response.body().string();
			logger.info(res);
			return objectMapper.readValue(res, responseClass);
		} else {
			return objectMapper.readValue(response.body().string(), responseClass);
		}
	}

	private <T> T extractResponseBody(Response response, TypeReference<T> typeReference) throws IOException {
		if (logging) {
			String res = response.body().string();
			logger.info(res);
			return objectMapper.readValue(res, typeReference);
		} else {
			return objectMapper.readValue(response.body().string(), typeReference);
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

	private Response post(String url, Object requestBody) throws IOException {
		RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
				objectMapper.writeValueAsString(requestBody));
		Request request = new Request.Builder().url(url).post(body).build();
		return client.newCall(request).execute();
	}

}
