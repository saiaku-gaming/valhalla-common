package com.valhallagame.common.exceptions;

import com.valhallagame.common.RestResponse;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final HttpStatus httpStatus;

    public <T> ApiException(RestResponse<T> restResponse) {
        super(restResponse.getErrorMessage());
        httpStatus = restResponse.getStatusCode();
    }

    public ApiException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
