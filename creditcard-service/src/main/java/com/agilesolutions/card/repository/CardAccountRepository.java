// repository/CardAccountRepository.java
package com.agilesolutions.card.repository;

import com.agilesolutions.card.domain.entity.CardAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for COBOL ACCTDAT cross-reference reads:
 *   READ ACCTDAT KEY = CARD-ACCT-ID  -> findByAcctId()
 */
@Repository
public interface CardAccountRepository extends JpaRepository<CardAccount, Long> {
    Optional<CardAccount> findByAcctId(String acctId);
    boolean existsByAcctId(String acctId);
}