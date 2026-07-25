package com.pipocaagil.feedback.exception;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException() {
        super("E-mail já cadastrado.");
    }
}
