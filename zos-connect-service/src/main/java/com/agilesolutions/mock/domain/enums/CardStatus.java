// domain/enums/CardStatus.java
package com.agilesolutions.mock.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Maps COBOL CARD-ACTIVE-STATUS PIC X(01)
 * COBOL VALUES: 'Y' = active, 'N' = inactive
 */
@Getter
@RequiredArgsConstructor
public enum CardStatus {Y("Y"), N("N");

    private final String code;

    @JsonCreator
    public static CardStatus fromCode(String code) {
        if (code == null) throw new IllegalArgumentException("Card status code is null");
        for (CardStatus s : values()) {
            if (s.code.equalsIgnoreCase(code) || s.name().equalsIgnoreCase(code))
                return s;
        }
        throw new IllegalArgumentException("Unknown card status: " + code);
    }

    @JsonValue
    public String toJson() { return this.name(); }
}