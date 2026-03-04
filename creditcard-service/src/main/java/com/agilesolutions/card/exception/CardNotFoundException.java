// exception/CardNotFoundException.java
package com.agilesolutions.card.exception;

import lombok.Getter;

/**
 * Thrown when COBOL READ CARDDAT returns FILE STATUS '23' (NOT FOUND)
 */
@Getter
public class CardNotFoundException extends RuntimeException {

    private final String errorCode;

    public CardNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}