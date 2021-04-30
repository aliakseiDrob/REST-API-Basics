package com.epam.esm.dao.exception;

public class EntityNotFoundException extends RuntimeException {
    private final int errorCode;

    public EntityNotFoundException(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
