package com.epam.esm.dao.utils;

import com.epam.esm.dao.exception.DataException;
import com.epam.esm.dao.exception.TypeErrorCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class DaoUtils {
    public static void validateEmptyCertificateName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new DataException(TypeErrorCode.CERTIFICATE_EMPTY_NAME.getErrorCode());
        }
    }

    public static void validateEmptyCertificateName(Map<String, Object> parameters) {
        if (parameters.containsKey("name")) {
            if (StringUtils.isBlank((String) parameters.get("name"))) {
                throw new DataException(TypeErrorCode.CERTIFICATE_EMPTY_NAME.getErrorCode());
            }
        }
    }

    public static void validateEmptyTagName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new DataException(TypeErrorCode.TAG_EMPTY_NAME.getErrorCode());
        }
    }
}
