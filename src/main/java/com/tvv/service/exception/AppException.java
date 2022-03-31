package com.tvv.service.exception;

public class AppException extends Exception{
    public AppException(String message, Exception exception)
    {
        super(message,exception);
    }

}
