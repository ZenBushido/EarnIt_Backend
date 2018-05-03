package com.mobiledi.earnitapi.domain.custom;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Response implements Serializable {
    private List<String> message;

    public Response(List<String> message) {
        this.message = message;
    }

    public Response(String message) {
        this.message = Arrays.asList(message);
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
