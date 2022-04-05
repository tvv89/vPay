package com.tvv.service.exception;

/**
 * Application exception class
 */
public class AppException extends Exception{
    /**
     * Custom exception for web application
     * @param message custom message
     * @param exception cause exception
     */
    public AppException(String message, Exception exception)
    {
        super(message,exception);
    }

}
