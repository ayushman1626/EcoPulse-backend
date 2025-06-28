package com.ecopulse.EcoPulse_Backend.exceptions;

public class VerificationTokenExpiredException extends RuntimeException {
    public VerificationTokenExpiredException(String message){
        super(message);
    }
}
