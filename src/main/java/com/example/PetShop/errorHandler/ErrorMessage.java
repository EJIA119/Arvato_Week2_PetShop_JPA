package com.example.PetShop.errorHandler;

public class ErrorMessage {

    private String status;
    private String Message;

    public ErrorMessage() {
    }

    public ErrorMessage(String status, String message) {
        this.status = status;
        Message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
