package com.transfer.app.response;

public class Response {

    private String status;
    private String message;

    public Response(String status, String message, String... args) {
        this.status = status;
        this.message = String.format(message, args);
    }

    public Response(String status,
                    Exception e) {
        this.status = status;
        this.message = e.getMessage();
    }

}
