package com.pipocaagil.feedback.exception;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException() {
        super("E-mail não cadastrado.");
    }
}
