// domain/entity/Card.java
package com.agilesolutions.card.domain.entity;

import com.agilesolutions.card.domain.enums.CardStatus;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Maps COBOL CARD-RECORD from CVACT03Y.cpy / COCRDUP.cpy
 *
 * COBOL copybook field mapping:
 *   CARD-NUM             PIC X(16)
 *   CARD-ACCT-ID         PIC X(11)
 *   CARD-CVV-CD          PIC X(03)
 *   CARD-EMBOSSED-NAME   PIC X(50)
 *   CARD-EXPIRAION-DATE  PIC X(10)
 *   CARD-ACTIVE-STATUS   PIC X(01)  'Y'/'N'
 */
@Entity
@Table(name = "cards")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // COBOL: CARD-NUM PIC X(16)
    @Column(name = "card_num", nullable = false, unique = true, length = 16)
    private String cardNum;

    // COBOL: CARD-ACCT-ID PIC X(11)
    @Column(name = "card_acct_id", nullable = false, length = 11)
    private String cardAcctId;

    // COBOL: CARD-CVV-CD PIC X(03)
    @Column(name = "card_cvv_cd", length = 3)
    private String cardCvvCd;

    // COBOL: CARD-EMBOSSED-NAME PIC X(50)
    @Column(name = "card_embossed_name", length = 50)
    private String cardEmbossedName;

    // COBOL: CARD-ACTIVE-STATUS PIC X(01)
    @Enumerated(EnumType.STRING)
    @Column(name = "active_status", nullable = false, length = 1)
    private CardStatus activeStatus;

    // COBOL: CARD-CURR-BAL PIC S9(10)V99 COMP-3
    @Column(name = "curr_bal", precision = 10, scale = 2)
    private BigDecimal currBal;

    // COBOL: CARD-CREDIT-LIMIT PIC S9(10)V99 COMP-3
    @Column(name = "credit_limit", precision = 10, scale = 2)
    private BigDecimal creditLimit;

    // COBOL: CARD-CASH-CREDIT-LIMIT PIC S9(10)V99 COMP-3
    @Column(name = "cash_credit_limit", precision = 10, scale = 2)
    private BigDecimal cashCreditLimit;

    // COBOL: CARD-OPEN-DATE PIC X(10)
    @Column(name = "open_date")
    private LocalDate openDate;

    // COBOL: CARD-EXPIRAION-DATE PIC X(10)
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    // COBOL: CARD-REISSUE-DATE PIC X(10)
    @Column(name = "reissue_date")
    private LocalDate reissueDate;

    // COBOL: CARD-CURR-CYC-CREDIT PIC S9(10)V99 COMP-3
    @Column(name = "curr_cycle_credit", precision = 10, scale = 2)
    private BigDecimal currCycleCredit;

    // COBOL: CARD-CURR-CYC-DEBIT PIC S9(10)V99 COMP-3
    @Column(name = "curr_cycle_debit", precision = 10, scale = 2)
    private BigDecimal currCycleDebit;

    // COBOL: CARD-GROUP-ID PIC X(10)
    @Column(name = "group_id", length = 10)
    private String groupId;

    // COBOL: CARD-SLI PIC X(03) (Service Level Indicator)
    @Column(name = "sli", length = 3)
    private String sli;

    // COBOL: CARD-ADDR-ZIP PIC X(10)
    @Column(name = "addr_zip", length = 10)
    private String addrZip;

    // COBOL: CARD-ADDR-STATE PIC X(02)
    @Column(name = "addr_state", length = 2)
    private String addrState;

    // COBOL: CARD-ADDR-COUNTRY PIC X(03)
    @Column(name = "addr_country", length = 3)
    private String addrCountry;

    // COBOL: CARD-ADDR-LINE-1 PIC X(50)
    @Column(name = "addr_line1", length = 50)
    private String addrLine1;

    // COBOL: CARD-ADDR-LINE-2 PIC X(50)
    @Column(name = "addr_line2", length = 50)
    private String addrLine2;

    // COBOL: CARD-PHONE-NUMBER-1 PIC X(15)
    @Column(name = "phone_number_1", length = 15)
    private String phoneNumber1;

    // COBOL: CARD-PHONE-NUMBER-2 PIC X(15)
    @Column(name = "phone_number_2", length = 15)
    private String phoneNumber2;

    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 50)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    // COBOL: COMPUTE AVAIL-CREDIT = CARD-CREDIT-LIMIT - CARD-CURR-BAL
    @Transient
    public BigDecimal getAvailableCredit() {
        if (creditLimit == null || currBal == null) return BigDecimal.ZERO;
        return creditLimit.subtract(currBal);
    }

    // COBOL: EVALUATE TRUE WHEN CARD-CURR-BAL > CARD-CREDIT-LIMIT
    @Transient
    public boolean isOverLimit() {
        if (creditLimit == null || currBal == null) return false;
        return currBal.compareTo(creditLimit) > 0;
    }

    // COBOL: EVALUATE TRUE WHEN CARD-EXPIRAION-DATE < FUNCTION CURRENT-DATE
    @Transient
    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }
}