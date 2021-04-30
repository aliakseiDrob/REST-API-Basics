package com.epam.esm.dao.exception;

public class DataException extends RuntimeException {
    private final int errorCode;

    public DataException(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
