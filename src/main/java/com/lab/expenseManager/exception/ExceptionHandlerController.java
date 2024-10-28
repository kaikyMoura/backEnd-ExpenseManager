package com.lab.expenseManager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.lab.expenseManager.model.ErrorResponseModel;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseModel> handleEntityNotFoundException(IllegalArgumentException ex, WebRequest request) {
        ErrorResponseModel errorResponse = new ErrorResponseModel(404, "Nenhum resultado encontrado", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseModel> handleGenericException(Exception ex, WebRequest request) {
        ErrorResponseModel errorResponse = new ErrorResponseModel(500, "Erro interno no servidor", "Erro ao executar a operação.");
        ex.printStackTrace();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErrorResponseModel> handleJWTVerificationException(JWTVerificationException ex, WebRequest request) {
        ErrorResponseModel errorResponse = new ErrorResponseModel(401, "Usuário não autorizado", ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseModel> handleUsernameBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        ErrorResponseModel errorResponse = new ErrorResponseModel(400, "Algum campo não foi preenchido corretamente", ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseModel> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        ErrorResponseModel errorResponse = new ErrorResponseModel(400, "Algum campo não foi preenchido corretamente", ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
