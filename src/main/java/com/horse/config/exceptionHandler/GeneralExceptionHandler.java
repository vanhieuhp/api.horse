package com.horse.config.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class GeneralExceptionHandler extends RuntimeException{

    public GeneralExceptionHandler(String message) {
        super(message);
    }

    public static void sendErrorNotAllowException(HttpServletResponse response) throws IOException {
        response.setHeader("error", METHOD_NOT_ALLOWED.getReasonPhrase());
        response.setStatus(METHOD_NOT_ALLOWED.value());
        Map<String, String> error = new HashMap<>();
        error.put("error_message", METHOD_NOT_ALLOWED.getReasonPhrase());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

    public static void sendErrorUnauthorizedException(HttpServletResponse response) throws IOException {
        response.setHeader("error", UNAUTHORIZED.getReasonPhrase());
        response.setStatus(UNAUTHORIZED.value());
        Map<String, String> error = new HashMap<>();
        error.put("error_message", UNAUTHORIZED.getReasonPhrase());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

    public static void sendErrorForbiddenException(HttpServletResponse response) throws IOException {
        response.setHeader("error", FORBIDDEN.getReasonPhrase());
        response.setStatus(FORBIDDEN.value());
        Map<String, String> error = new HashMap<>();
        error.put("error_message", FORBIDDEN.getReasonPhrase());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

    public static void sendErrorTokenException(HttpServletResponse response, String message) throws IOException {
        response.setHeader("error", BAD_REQUEST.getReasonPhrase());
        response.setStatus(BAD_REQUEST.value());
        Map<String, String> error = new HashMap<>();
        error.put("error_message", message);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
