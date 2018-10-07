package com.valhallagame.common.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.valhallagame.common.JS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice(annotations = RestController.class)
public class ApiResponseExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ApiResponseExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<JsonNode> handleException(RuntimeException ex, WebRequest request) {

        if (ex instanceof ApiException) {
            ApiException apiEx = (ApiException) ex;
            logger.warn("Api exception", ex);
            return JS.message(apiEx.getHttpStatus(), apiEx.getMessage());
        }
        logger.error("Unexpected exception", ex);
        return JS.message(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
