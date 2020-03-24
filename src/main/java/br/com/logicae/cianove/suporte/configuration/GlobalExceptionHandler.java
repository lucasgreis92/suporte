package br.com.logicae.cianove.suporte.configuration;

import br.com.logicae.cianove.suporte.exception.BusinessException;
import br.com.logicae.cianove.suporte.exception.dto.ResponseErrorDtoV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity handleBusinessException(HttpServletRequest request, BusinessException ex){
        log.error("URL: " + request.getMethod()
                + " - " + request.getRequestURL()
                + " Erros: " + ex.getResponseError().toString(),ex);
        return ResponseEntity.badRequest().body(ex.getResponseError());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity handleHttpClientErrorException(HttpServletRequest request, HttpClientErrorException ex){
        String msg = "URL: " + request.getMethod()
                + " - " + request.getRequestURL()
                + " Erros: " + ex.getResponseBodyAsString();
        log.error(msg,ex);
        ResponseErrorDtoV1 responseError = new ResponseErrorDtoV1();
        responseError.addError("",msg);
        return ResponseEntity.badRequest().body(responseError);
    }
}
