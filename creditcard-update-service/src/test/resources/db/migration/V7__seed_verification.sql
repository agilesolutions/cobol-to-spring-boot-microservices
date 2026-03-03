-- V7__seed_verification.sql
DO $$
DECLARE
    v_card_count     INTEGER;
    v_active_cards   INTEGER;
    v_inactive_cards INTEGER;
    v_acct_count     INTEGER;
    v_user_count     INTEGER;
    v_over_limit     INTEGER;
    v_expired        INTEGER;
BEGIN
    SELECT COUNT(*) INTO v_card_count     FROM cards;
    SELECT COUNT(*) INTO v_active_cards   FROM cards WHERE active_status = 'Y';
    SELECT COUNT(*) INTO v_inactive_cards FROM cards WHERE active_status = 'N';
    SELECT COUNT(*) INTO v_acct_count     FROM card_accounts;
    SELECT COUNT(*) INTO v_user_count     FROM users;
    SELECT COUNT(*) INTO v_over_limit
        FROM cards WHERE curr_bal > credit_limit AND credit_limit > 0;
    SELECT COUNT(*) INTO v_expired
        FROM cards WHERE expiry_date < CURRENT_DATE;

    IF v_card_count  < 10 THEN
        RAISE EXCEPTION 'Seed FAILED: expected >= 10 cards, found %',  v_card_count;
    END IF;
    IF v_acct_count  < 6  THEN
        RAISE EXCEPTION 'Seed FAILED: expected >= 6 accounts, found %', v_acct_count;
    END IF;
    IF v_user_count  < 4  THEN
        RAISE EXCEPTION 'Seed FAILED: expected >= 4 users, found %',   v_user_count;
    END IF;
    IF v_over_limit  < 1  THEN
        RAISE EXCEPTION 'Seed FAILED: expected >= 1 over-limit card,  found %', v_over_limit;
    END IF;
    IF v_expired     < 1  THEN
        RAISE EXCEPTION 'Seed FAILED: expected >= 1 expired card, found %', v_expired;
    END IF;

    RAISE NOTICE '=== Card Seed Verification PASSED ===';
    RAISE NOTICE 'Cards total    : %', v_card_count;
    RAISE NOTICE '  Active       : %', v_active_cards;
    RAISE NOTICE '  Inactive     : %', v_inactive_cards;
    RAISE NOTICE '  Over-limit   : %', v_over_limit;
    RAISE NOTICE '  Expired      : %', v_expired;
    RAISE NOTICE 'Card accounts  : %', v_acct_count;
    RAISE NOTICE 'Users          : %', v_user_count;
END;
$$;