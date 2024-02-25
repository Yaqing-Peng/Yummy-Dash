package com.yummy.handler;

import com.yummy.constant.MessageConstant;
import com.yummy.exception.BaseException;
import com.yummy.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * global exception handler for business rules
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("Error message：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * duplicate user handler
     * @param ex
     * @return
     */
    //SQLIntegrityConstraintViolationException: Duplicate entry 'jasmine' for key 'employee.idx_username'
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //get error message
        String message = ex.getMessage();
        //split and link
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String username = split[2];
            String msg = username + " " + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }else{
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }
}
