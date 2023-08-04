package com.plot.plotserver.exception;

import com.plot.plotserver.domain.Message;
import com.plot.plotserver.exception.email.EmailCodeExpiredException;
import com.plot.plotserver.exception.email.EmailCodeMismatchException;
import com.plot.plotserver.exception.email.EmailCodeSendingFailureException;
import com.plot.plotserver.exception.token.TokenExpiredException;
import com.plot.plotserver.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UsernameExistException.class)
    public ResponseEntity<?> handleException(UsernameExistException e){
        Message message = new Message(e.getMessage(), -100, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(PasswordFormatException.class)
    public ResponseEntity<?> handleException(PasswordFormatException e) {
        Message message = new Message(e.getMessage(), -102, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(SecurityContextUserNotFoundException.class)
    public ResponseEntity<?> handleException(SecurityContextUserNotFoundException e){
        Message message = new Message(e.getMessage(), -104, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handleException(TokenExpiredException e){
        Message message = new Message(e.getMessage(), -108, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(UserPrincipalNotFoundException.class)
    public ResponseEntity<?> handleException(UserPrincipalNotFoundException e){
        Message message = new Message(e.getMessage(), -120, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleException(UserNotFoundException e){
        Message message = new Message(e.getMessage(), -127, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(WrongLoginException.class)
    public ResponseEntity<?> handleException(WrongLoginException e) {
        Message message = new Message(e.getMessage(), -131, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }


    @ExceptionHandler(EmailCodeSendingFailureException.class)//email 관련 오류 -200~300
    public ResponseEntity<?> handleException(EmailCodeSendingFailureException e) {
        Message message = new Message(e.getMessage(), -200, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }


    @ExceptionHandler(EmailCodeExpiredException.class)//email 관련 오류 -200~300
    public ResponseEntity<?> handleException(EmailCodeExpiredException e) {
        Message message = new Message(e.getMessage(), -201, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(EmailCodeMismatchException.class)//email 관련 오류 -200~300
    public ResponseEntity<?> handleException(EmailCodeMismatchException e) {
        Message message = new Message(e.getMessage(), -202, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }
}
