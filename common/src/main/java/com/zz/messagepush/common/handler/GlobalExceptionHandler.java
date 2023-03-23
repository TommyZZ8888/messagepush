package com.zz.messagepush.common.handler;


import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.RespStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseResult<?> exceptionHandler(Exception e, HttpServletResponse response, HttpServletRequest request) {
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return ResponseResult.fail(RespStatusEnum.ERROR_400.getDescription() + e.getMessage());
        }
        if (e instanceof TypeMismatchException) {
            return ResponseResult.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS.getDescription() + e.getMessage());
        }
        if (e instanceof NullPointerException) {
            return ResponseResult.fail(RespStatusEnum.NULL_POINT.getDescription() + e.getMessage());
        }
        if (e instanceof HttpMessageNotReadableException) {
            return ResponseResult.fail(RespStatusEnum.ERROR_500.getDescription() + e.getMessage());
        }
        //参数校验未通过
        if (e instanceof MethodArgumentNotValidException) {
            List<FieldError> fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
            List<String> msgList = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
            return ResponseResult.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS.getDescription() + msgList.get(0));
        }
        if (e instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) e).getConstraintViolations();
            List<String> msgList = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
            return ResponseResult.fail(msgList.get(0));
        }
        if (e instanceof AccessDeniedException) {
            return ResponseResult.fail(RespStatusEnum.NO_LOGIN.getDescription() + e.getMessage());
        }
        return ResponseResult.fail(e.getMessage());
    }

}
