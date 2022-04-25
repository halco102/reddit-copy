package com.project.reddit.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomGlobalException extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessage handleException(ClassCastException ex){
        log.error(ex.getMessage(), ex);
        return new ErrorMessage(500, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessage handleException(NotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorMessage(404, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorMessage handleException(DuplicateException ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorMessage(409, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorMessage handleException(Unauthorized ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorMessage(401, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage handleException(BadRequestException ex){
        log.error(ex.getMessage(), ex);
        return new ErrorMessage(400, ex.getMessage());
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorMessage {

        @JsonProperty("code")
        private final int code;

        @JsonProperty("message")
        private final String message;

        public ErrorMessage(final int code, final String message) {
            super();
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
