package com.ecommerce.project.exceptionHandler;

import com.ecommerce.project.payload.APIResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    @Autowired
    APIResponse apiResponse;


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String,String> map = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            map.put(fieldName,errorMessage);
        });

        return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e){
        String message = e.getMessage();
        apiResponse.setMessage(message);
        apiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(APIException e){
        String message = e.getMessage();
        apiResponse.setMessage(message);
        apiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
       public ResponseEntity<APIResponse> myIllegalArgumentException (IllegalArgumentException e){
        String message = e.getMessage();
        apiResponse.setMessage(message);
        apiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<APIResponse> myMissingServletRequestParameterException (MissingServletRequestParameterException e){

//        APIResponse apiResponse1 = new APIResponse();
        String message = e.getMessage();
        apiResponse.setMessage(message);
        apiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponse> myConstraintViolationException (ConstraintViolationException e){

        String propertyPath = e.getConstraintViolations().iterator().next().getPropertyPath().toString();
        String interpolatedMessage = e.getConstraintViolations().iterator().next().getMessage();
        String message = propertyPath + ": " + interpolatedMessage;

        apiResponse.setMessage(message);
        apiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<APIResponse> myUnexpectedTypeException (UnexpectedTypeException e){
        String message = e.getMessage();
        apiResponse.setMessage(message);
        apiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

}
