package com.arifacar.api.conf;

import com.arifacar.domain.model.constants.ResponseCodes;
import com.arifacar.domain.model.generic.GenericResponse;
import com.arifacar.domain.model.generic.TodoApiException;
import com.arifacar.domain.model.service.exception.LoginException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder errors = new StringBuilder();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(" - ");
        }
        errors.append(" / ");
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.append(error.getObjectName()).append(": ").append(error.getDefaultMessage()).append(" - ");
        }

        GenericResponse genericResponse = getGenericResponse(ResponseCodes.FAIL_WITH_POPUP, ex.getMessage(), errors.toString());
        ex.printStackTrace();
        return handleExceptionInternal(ex, genericResponse, headers, HttpStatus.OK, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        GenericResponse genericResponse = getGenericResponse(ResponseCodes.FAIL_WITH_POPUP, ex.getMessage(), ex.getParameterName() + " parameter is missing");
        ex.printStackTrace();
        return new ResponseEntity<>(genericResponse, new HttpHeaders(), HttpStatus.OK);
    }


    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();

        GenericResponse genericResponse = getGenericResponse(
                ResponseCodes.FAIL_WITH_POPUP, ex.getMessage() + " / " + error, ex.toString());

        ex.printStackTrace();
        return new ResponseEntity<>(genericResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        GenericResponse genericResponse = getGenericResponse(ResponseCodes.FAIL_WITH_POPUP, ex.getLocalizedMessage(), error);

        ex.printStackTrace();
        return new ResponseEntity<>(genericResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleAssertException(IllegalArgumentException ex) {
        GenericResponse genericResponse = getGenericResponse(ResponseCodes.FAIL_WITH_POPUP, ex.getMessage(), null);
        ex.printStackTrace();
        return new ResponseEntity<>(genericResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler({TodoApiException.class})
    public ResponseEntity<Object> handleTodoApiException(TodoApiException ex, WebRequest request) {
        int statusCode = ex.getStatusCode() == 0 ? ResponseCodes.FAIL_WITH_POPUP : ex.getStatusCode();
        GenericResponse genericResponse = getGenericResponse(statusCode, ex.getStatusDesc(), ex.getDevelopmentDesc());
        ex.printStackTrace();
        return new ResponseEntity<>(genericResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler({LoginException.class})
    public ResponseEntity<Object> handleLoginException(LoginException ex) {
        GenericResponse genericResponse = getGenericResponse(ex.getStatusCode(), ex.getStatusDesc(), ex.getDevelopmentDesc());
        return new ResponseEntity<>(genericResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        GenericResponse genericResponse = getGenericResponse(ResponseCodes.FAIL_WITH_POPUP, "İşleminizi gerçekleştiremiyoruz.",
                ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(genericResponse, new HttpHeaders(), HttpStatus.OK);
    }

    private GenericResponse getGenericResponse(int statusCode, String desc, String developerDesc) {
        return GenericResponse.builder()
                .statusCode(statusCode)
                .statusDesc(desc)
                .developmentDesc(developerDesc)
                .build();
    }
}
