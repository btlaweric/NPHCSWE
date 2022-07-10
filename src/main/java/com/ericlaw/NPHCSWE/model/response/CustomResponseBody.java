package com.ericlaw.NPHCSWE.model.response;

public class CustomResponseBody {
    private String message;

    public CustomResponseBody(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
