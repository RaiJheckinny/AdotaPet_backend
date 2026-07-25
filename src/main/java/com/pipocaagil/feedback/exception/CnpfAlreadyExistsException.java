package com.pipocaagil.feedback.exception;

public class CnpfAlreadyExistsException extends RuntimeException {
    public CnpfAlreadyExistsException() {
        super("E-mail já cadastrado.");
    }
}
