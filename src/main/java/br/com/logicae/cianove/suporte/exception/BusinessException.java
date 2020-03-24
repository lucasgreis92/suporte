package br.com.logicae.cianove.suporte.exception;

import br.com.logicae.cianove.suporte.exception.dto.ResponseErrorDtoV1;

public class BusinessException extends Exception {

    private static final long serialVersionUID = 2036907995422745505L;
    private ResponseErrorDtoV1 responseError = new ResponseErrorDtoV1();

    public BusinessException(String message) {
        super(message);
        this.responseError.addError("", message);
    }

    public BusinessException(String message, Exception ex) {
        super(message, ex);
        this.responseError.addError("", message);
    }

    public BusinessException(String key, String message) {
        super(message);
        this.responseError.addError(key, message);
    }

    public ResponseErrorDtoV1 getResponseError() {
        return this.responseError;
    }
}
