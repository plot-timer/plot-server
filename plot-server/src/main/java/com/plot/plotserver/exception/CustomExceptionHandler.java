package com.plot.plotserver.exception;

import com.plot.plotserver.domain.Message;
import com.plot.plotserver.exception.category.CategoryAlreadyExistException;
import com.plot.plotserver.exception.category.CategoryDeleteFailException;
import com.plot.plotserver.exception.category.CategoryNotExistException;
import com.plot.plotserver.exception.category.CategorySavedFailException;
import com.plot.plotserver.exception.categorygroup.*;
import com.plot.plotserver.exception.dailytodo.DailyTodoAlreadyExistException;
import com.plot.plotserver.exception.dailytodo.DailyTodoDeleteFailException;
import com.plot.plotserver.exception.dailytodo.DailyTodoSavedFailException;
import com.plot.plotserver.exception.dailytodo.DailyTodoUpdateFailException;
import com.plot.plotserver.exception.email.EmailCodeExpiredException;
import com.plot.plotserver.exception.email.EmailCodeMismatchException;
import com.plot.plotserver.exception.email.EmailCodeSendingFailureException;
import com.plot.plotserver.exception.history.HistorySavedFailException;
import com.plot.plotserver.exception.tag.TagNotFoundException;
import com.plot.plotserver.exception.todo.*;
import com.plot.plotserver.exception.token.TokenExpiredException;
import com.plot.plotserver.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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


    @ExceptionHandler(TodoSavedFailException.class)// todo 관련 에러 -300~-350
    public ResponseEntity<?> handleException(TodoSavedFailException e) {
        Message message = new Message(e.getMessage(), -300,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }


    @ExceptionHandler(TodoDeleteFailException.class)
    public ResponseEntity<?> handleException(TodoDeleteFailException e) {
        Message message = new Message(e.getMessage(), -301,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }




    @ExceptionHandler(TodoUpdateFailException.class)
    public ResponseEntity<?> handleException(TodoUpdateFailException e) {
        Message message = new Message(e.getMessage(), -302,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(TodoSearchFailException.class)
    public ResponseEntity<?> handleException(TodoSearchFailException e) {
        Message message = new Message(e.getMessage(), -303,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }


    @ExceptionHandler(TodoNotExistException.class)
    public ResponseEntity<?> handleException(TodoNotExistException e) {
        Message message = new Message(e.getMessage(), -304,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(TodoNotHasRecordsOnDateException.class)
    public ResponseEntity<?> handleException(TodoNotHasRecordsOnDateException e) {
        Message message = new Message(e.getMessage(), -305,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }




    @ExceptionHandler(CategoryGroupSavedFailException.class)// categorygroup 관련 에러 -350~-400
    public ResponseEntity<?> handleException(CategoryGroupSavedFailException e) {
        Message message = new Message(e.getMessage(), -351,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }


    @ExceptionHandler(CategoryGroupDeleteFailException.class)
    public ResponseEntity<?> handleException(CategoryGroupDeleteFailException e) {
        Message message = new Message(e.getMessage(), -352,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }


    @ExceptionHandler(CategoryGroupUpdateFailException.class)
    public ResponseEntity<?> handleException(CategoryGroupUpdateFailException e) {
        Message message = new Message(e.getMessage(), -353,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }


    @ExceptionHandler(CategoryGroupAlreadyExistException.class)
    public ResponseEntity<?> handleException(CategoryGroupAlreadyExistException e) {
        Message message = new Message(e.getMessage(), -354,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }


    @ExceptionHandler(CategoryGroupNotFoundException.class)
    public ResponseEntity<?> handleException(CategoryGroupNotFoundException e) {
        Message message = new Message(e.getMessage(), -355,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }



    @ExceptionHandler(CategorySavedFailException.class)// category 관련 에러 -400~-450
    public ResponseEntity<?> handleException(CategorySavedFailException e) {
        Message message = new Message(e.getMessage(), -401,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(CategoryDeleteFailException.class)
    public ResponseEntity<?> handleException(CategoryDeleteFailException e) {
        Message message = new Message(e.getMessage(), -403,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(CategoryAlreadyExistException.class)
    public ResponseEntity<?> handleException(CategoryAlreadyExistException e) {
        Message message = new Message(e.getMessage(), -404,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(CategoryNotExistException.class)
    public ResponseEntity<?> handleException(CategoryNotExistException e) {
        Message message = new Message(e.getMessage(), -405,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(DailyTodoSavedFailException.class)// Daily todo 관련 에러 -450~-470
    public ResponseEntity<?> handleException(DailyTodoSavedFailException e) {
        Message message = new Message(e.getMessage(), -451,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(DailyTodoDeleteFailException.class)
    public ResponseEntity<?> handleException(DailyTodoDeleteFailException e) {
        Message message = new Message(e.getMessage(), -452,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }


    @ExceptionHandler(DailyTodoAlreadyExistException.class)
    public ResponseEntity<?> handleException(DailyTodoAlreadyExistException e) {
        Message message = new Message(e.getMessage(), -453,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }


    @ExceptionHandler(DailyTodoUpdateFailException.class)
    public ResponseEntity<?> handleException(DailyTodoUpdateFailException e) {
        Message message = new Message(e.getMessage(), -454,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }



    @ExceptionHandler(HistorySavedFailException.class)//history 관련 에러 ~470~-480
    public ResponseEntity<?> handleException(HistorySavedFailException e) {
        Message message = new Message(e.getMessage(), -470,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }
    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<?> handleException(TagNotFoundException e) {
        Message message = new Message(e.getMessage(), -500,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

}
