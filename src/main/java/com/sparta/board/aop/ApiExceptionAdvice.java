package com.sparta.board.aop;

import com.sparta.board.exception.InvalidPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApiExceptionAdvice {


    /**
     * [Exception] API 호출 시 PathVariable 에 없는 리소스 ID 값으로 조회하는 경우 예외 발생
     *
     * @param exception NoSuchElementException
     * @return ResponseEntity<Map<String,String>>
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String,String>> noSuchElementExceptionHandler(NoSuchElementException exception) {
        String msg = "";
        if (StringUtils.hasText(exception.getMessage())) {
            msg = exception.getMessage();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", msg));
    }
    /**
     * [Exception] 수정, 삭제 시 입력한 비밀번호가 게시글의 비밀번호와 일치하지 않는 경우 예외 발생
     *
     * @param exception InvalidPasswordException
     * @return ResponseEntity<Map<String,String>>
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String,String>> noSuchElementExceptionHandler(InvalidPasswordException exception) {
        String msg = "";
        if (StringUtils.hasText(exception.getMessage())) {
            msg = exception.getMessage();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("msg", msg));
    }
}
