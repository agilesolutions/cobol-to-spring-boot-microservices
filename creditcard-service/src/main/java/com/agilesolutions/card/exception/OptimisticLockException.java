// exception/OptimisticLockException.java
package com.agilesolutions.card.exception;

import lombok.Getter;

/**
 * Thrown on concurrent update conflict.
 * Mirrors COBOL FILE STATUS '09' (concurrent record locked).
 */
@Getter
public class OptimisticLockException extends RuntimeException {

    private final String errorCode;

    public OptimisticLockException(String message) {
        super(message);
        this.errorCode = "ERR_CONCURRENT_UPDATE";
    }
}