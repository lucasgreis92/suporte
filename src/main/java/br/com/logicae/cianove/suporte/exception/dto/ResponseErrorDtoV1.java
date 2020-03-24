package br.com.logicae.cianove.suporte.exception.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResponseErrorDtoV1 {
    private List<ItemErrorDtoV1> errors = new ArrayList();

    public ResponseErrorDtoV1() {
    }

    public void addError(String key, String message) {
        this.errors.add(new ItemErrorDtoV1(key, message));
    }

    public List<ItemErrorDtoV1> getErrors() {
        return this.errors;
    }

    public String toString() {
        String err = "";
        if (this.errors != null) {
            Iterator var2 = this.errors.iterator();

            while(var2.hasNext()) {
                ItemErrorDtoV1 itemErrorDtoV1 = (ItemErrorDtoV1)var2.next();
                if (err.isEmpty()) {
                    err = itemErrorDtoV1.getMessage();
                } else {
                    err = err + ", " + itemErrorDtoV1.getMessage();
                }
            }
        }

        return err;
    }
}
