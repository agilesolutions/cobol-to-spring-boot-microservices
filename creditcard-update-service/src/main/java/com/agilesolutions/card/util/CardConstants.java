// util/CardConstants.java
package com.agilesolutions.card.util;

/**
 * Constants replacing COBOL 01-level WORKING-STORAGE literals
 *
 * COBOL equivalents:
 *   01 WS-MISC-STORAGE.
 *     05 WS-RETURN-CODE      PIC X(04) VALUE SPACES.
 *     05 WS-CARDNUM-ERR-FLG  PIC X(1)  VALUE 'N'.
 *     05 WS-ACCTID-ERR-FLG   PIC X(1)  VALUE 'N'.
 *     05 WS-DATE-ERR-FLG     PIC X(1)  VALUE 'N'.
 *     05 WS-LIMIT-ERR-FLG    PIC X(1)  VALUE 'N'.
 */
public final class CardConstants {

    private CardConstants() {}

    // ─── COBOL FILE STATUS codes ──────────────────────────────────────────────
    public static final String FS_SUCCESS       = "00";
    public static final String FS_NOT_FOUND     = "23";
    public static final String FS_DUPLICATE_KEY = "22";
    public static final String FS_LOCKED        = "09";

    // ─── Application error codes (WS-RETURN-CODE values) ─────────────────────
    public static final String ERR_CARD_NOT_FOUND       = "CARD-0001";
    public static final String ERR_CARD_EXISTS          = "CARD-0002";
    public static final String ERR_CARD_INACTIVE        = "CARD-0003";
    public static final String ERR_CARD_EXPIRED         = "CARD-0004";
    public static final String ERR_VALIDATION_FAILED    = "CARD-0005";
    public static final String ERR_ACCOUNT_NOT_FOUND    = "CARD-0006";
    public static final String ERR_CONCURRENT_UPDATE    = "CARD-0007";
    public static final String ERR_INTERNAL             = "CARD-9999";

    // ─── Indicator values (COBOL PIC X(1) flag fields) ───────────────────────
    public static final String IND_YES = "Y";
    public static final String IND_NO  = "N";

    // ─── Card status codes ────────────────────────────────────────────────────
    public static final String STATUS_ACTIVE   = "Y";
    public static final String STATUS_INACTIVE = "N";

    // ─── Field lengths (COBOL PIC clauses) ───────────────────────────────────
    public static final int CARD_NUM_LENGTH     = 16;   // PIC X(16)
    public static final int CARD_ACCT_LENGTH    = 11;   // PIC X(11)
    public static final int CARD_CVV_LENGTH     = 3;    // PIC X(03)
    public static final int CARD_NAME_LENGTH    = 50;   // PIC X(50)
    public static final int CARD_SLI_LENGTH     = 3;    // PIC X(03)
    public static final int ADDR_ZIP_LENGTH     = 10;   // PIC X(10)
    public static final int ADDR_STATE_LENGTH   = 2;    // PIC X(02)
    public static final int ADDR_COUNTRY_LENGTH = 3;    // PIC X(03)
    public static final int PHONE_LENGTH        = 15;   // PIC X(15)

    // ─── Pagination defaults ──────────────────────────────────────────────────
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE     = 100;
}