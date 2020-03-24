package br.com.logicae.cianove.suporte.exception.dto;

public class ItemErrorDtoV1 {
    private String key;
    private String message;

    public ItemErrorDtoV1(String key, String message) {
        this.key = key;
        this.message = message;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
