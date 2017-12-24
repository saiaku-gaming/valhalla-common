package com.valhallagame.common;

import java.util.Optional;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse<T> {
	private HttpStatus statusCode;
	private Optional<T> response = Optional.empty();
	private String errorMessage;

	public RestResponse(HttpStatus statusCode, Optional<T> response) {
		this.statusCode = statusCode;
		this.response = response;
		this.errorMessage = null;
	}

	public void setResponse(T response) {
		this.response = Optional.ofNullable(response);
	}

	public boolean isOk() {
		return HttpStatus.OK.equals(statusCode) && this.response.isPresent();
	}
	
	public Optional<T> get(){
		if(HttpStatus.OK.equals(statusCode)) {
			return this.response;
		} else {
			return Optional.empty();
		}
	}

	public static <T> RestResponse<T> errorResponse(Exception e) {
		return new RestResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, Optional.empty(), e.getMessage());
	}
}
