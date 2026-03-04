// repository/CardRepository.java
package com.agilesolutions.card.repository;

import com.agilesolutions.card.domain.entity.Card;
import com.agilesolutions.card.domain.enums.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repository replacing COBOL VSAM file operations on CARDDAT:
 *
 *   READ CARDDAT KEY = CARD-NUM         -> findByCardNum()
 *   READ CARDDAT WITH LOCK              -> @Version optimistic lock
 *   WRITE CARDDAT FROM WS-CARD-RECORD   -> save() INSERT
 *   REWRITE CARDDAT FROM WS-CARD-RECORD -> save() UPDATE
 *   DELETE CARDDAT (logical)            -> activeStatus = INACTIVE
 *   START CARDDAT / READ NEXT           -> findAll(Pageable)
 */
@Repository
public interface CardRepository
        extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {

    // COBOL: READ CARDDAT KEY = CARD-NUM
    Optional<Card> findByCardNum(String cardNum);

    boolean existsByCardNum(String cardNum);

    // COBOL: READ CARDDAT KEY = CARD-ACCT-ID (multiple cards per account)
    Page<Card> findByCardAcctId(String cardAcctId, Pageable pageable);

    // COBOL: READ CARDDAT WITH ACTIVE-STATUS = 'Y'
    Page<Card> findByActiveStatus(CardStatus activeStatus, Pageable pageable);

    // COBOL: READ CARDDAT WITH ACTIVE-STATUS AND ACCT-ID
    Page<Card> findByCardAcctIdAndActiveStatus(
            String cardAcctId, CardStatus activeStatus, Pageable pageable);

    // COBOL: EVALUATE WHEN CARD-EXPIRAION-DATE < FUNCTION CURRENT-DATE
    @Query("SELECT c FROM Card c WHERE c.expiryDate < :today")
    Page<Card> findExpiredCards(@Param("today") LocalDate today, Pageable pageable);

    // COBOL: EVALUATE WHEN CARD-CURR-BAL > CARD-CREDIT-LIMIT
    @Query("SELECT c FROM Card c WHERE c.currBal > c.creditLimit AND c.creditLimit > 0")
    Page<Card> findOverLimitCards(Pageable pageable);

    // COBOL: Cards expiring within N days
    @Query("SELECT c FROM Card c WHERE c.expiryDate BETWEEN :today AND :future")
    Page<Card> findCardsExpiringBetween(
            @Param("today")  LocalDate today,
            @Param("future") LocalDate future,
            Pageable pageable);

    // COBOL: Name search (partial match on CARD-EMBOSSED-NAME)
    @Query("SELECT c FROM Card c WHERE UPPER(c.cardEmbossedName) LIKE UPPER(CONCAT('%',:name,'%'))")
    Page<Card> findByEmbossedNameContaining(@Param("name") String name, Pageable pageable);

    // COBOL: Group browse
    Page<Card> findByGroupId(String groupId, Pageable pageable);
}