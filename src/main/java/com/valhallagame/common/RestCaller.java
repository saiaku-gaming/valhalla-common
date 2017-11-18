package com.valhallagame.common;

import java.io.IOException;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RestCaller {
	private OkHttpClient client;
	private ObjectMapper objectMapper;
	private boolean logging;

	public RestCaller() {
		client = new OkHttpClient();
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		logging = false;
	}

	public RestCaller(boolean logging) {
		this();
		this.logging = logging;
	}

	@SuppressWarnings("unchecked")
	public <T> RestResponse<T> getCall(String url, Class<T> responseClass) throws IOException {
		Request request = new Request.Builder().url(url).get().build();
		Response response = client.newCall(request).execute();

		RestResponse<T> restResponse = new RestResponse<>();
		T responseBody = null;

		if (response.code() == 200) {
			if (String.class.equals(responseClass)) {
				responseBody = (T) extractResponseBody(response, StringResponse.class).getMessage();
			} else {
				responseBody = extractResponseBody(response, responseClass);
			}
		} else {
			restResponse.setMessage(extractResponseBody(response, StringResponse.class).getMessage());
		}

		restResponse.setResponse(responseBody);
		restResponse.setStatusCode(HttpStatus.valueOf(response.code()));

		return restResponse;
	}

	@SuppressWarnings("unchecked")
	public <T> RestResponse<T> postCall(String url, Object requestBody, Class<T> responseClass) throws IOException {
		RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
				objectMapper.writeValueAsString(requestBody));
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = client.newCall(request).execute();

		RestResponse<T> restResponse = new RestResponse<>();
		T responseBody = null;

		if (response.code() == 200) {
			if (String.class.equals(responseClass)) {
				responseBody = (T) extractResponseBody(response, StringResponse.class).getMessage();
			} else {
				responseBody = extractResponseBody(response, responseClass);
			}
		} else {
			restResponse.setMessage(extractResponseBody(response, StringResponse.class).getMessage());
		}

		restResponse.setResponse(responseBody);
		restResponse.setStatusCode(HttpStatus.valueOf(response.code()));

		return restResponse;
	}

	private <T> T extractResponseBody(Response response, Class<T> responseClass) throws IOException {
		if (logging) {
			String res = response.body().string();
			System.out.println(res);

			return objectMapper.readValue(res, responseClass);
		} else {
			return objectMapper.readValue(response.body().string(), responseClass);
		}
	}
}
