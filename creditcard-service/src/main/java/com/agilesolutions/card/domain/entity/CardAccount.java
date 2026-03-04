// domain/entity/CardAccount.java
package com.agilesolutions.card.domain.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Maps COBOL ACCOUNT-RECORD from CVCUS01Y.cpy
 * Used for cross-reference validation during card update:
 *   COBOL paragraph: VALIDATE-ACCOUNT-DATA
 *     READ ACCTDAT KEY = CARD-ACCT-ID
 */
@Entity
@Table(name = "card_accounts")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class CardAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // COBOL: ACCT-ID PIC X(11)
    @Column(name = "acct_id", nullable = false, unique = true, length = 11)
    private String acctId;

    // COBOL: ACCT-ENTITY-CD PIC X(50)
    @Column(name = "acct_entity_cd", length = 50)
    private String acctEntityCd;

    // COBOL: ACCT-TYPE-CD PIC X(01)
    @Column(name = "acct_type_cd", nullable = false, length = 1)
    private String acctTypeCd;

    // COBOL: ACCT-ACTIVE-STATUS PIC X(01)
    @Column(name = "acct_active_status", nullable = false, length = 1)
    private String acctActiveStatus;

    // COBOL: ACCT-CURR-BAL PIC S9(10)V99 COMP-3
    @Column(name = "acct_curr_bal", precision = 10, scale = 2)
    private BigDecimal acctCurrBal;

    // COBOL: ACCT-CREDIT-LIMIT PIC S9(10)V99 COMP-3
    @Column(name = "acct_credit_limit", precision = 10, scale = 2)
    private BigDecimal acctCreditLimit;

    // COBOL: ACCT-CASH-CREDIT-LIMIT PIC S9(10)V99 COMP-3
    @Column(name = "acct_cash_credit_limit", precision = 10, scale = 2)
    private BigDecimal acctCashCreditLimit;

    // COBOL: ACCT-OPEN-DATE PIC X(10)
    @Column(name = "acct_open_date")
    private LocalDate acctOpenDate;

    // COBOL: ACCT-EXPIRY-DATE PIC X(10)
    @Column(name = "acct_expiry_date")
    private LocalDate acctExpiryDate;

    // COBOL: ACCT-GROUP-ID PIC X(10)
    @Column(name = "acct_group_id", length = 10)
    private String acctGroupId;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;
}