package com.epam.esm.dao.exception;

public enum TypeErrorCode {

    CERTIFICATE_NOT_FOUND(40401),
    TAG_NOT_FOUND(40402),
    CERTIFICATE_EMPTY_NAME(40001),
    TAG_EMPTY_NAME(40002),
    TAG_NAME_DUPLICATE(40003);

    TypeErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    private final int errorCode;

    public int getErrorCode() {
        return errorCode;
    }
}
