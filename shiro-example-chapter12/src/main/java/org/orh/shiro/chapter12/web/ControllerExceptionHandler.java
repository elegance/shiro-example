package org.orh.shiro.chapter12.web;

import org.apache.shiro.authz.UnauthorizedException;
import org.orh.shiro.chapter12.web.bean.RetCode;
import org.orh.shiro.chapter12.web.bean.RetMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class ControllerExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public Object handleSysIllegalArgumentException(IllegalArgumentException ex, WebRequest req) {
        log.info("handling IllegalArgumentException...", ex);
        return new ResponseEntity<RetMsg>(RetMsg.error(RetCode.ILLEGALARGUMENT, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    public ResponseEntity<RetMsg> handleUnauthorizedException(UnauthorizedException ex, WebRequest req) {
        log.info("handling UnauthorizedException...", ex);
        return new ResponseEntity<RetMsg>(RetMsg.error(RetCode.UNAUTHORIZED_ERR, "身份认证错误"), HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RetMsg> handleException(Exception ex) {
        log.info("handling Exception...", ex);
        return new ResponseEntity<RetMsg>(RetMsg.error(RetCode.SYS_ERROR, "System Internal error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
