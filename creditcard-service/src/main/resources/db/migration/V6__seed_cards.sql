-- -----------------------------------------------------------------------------
-- Clean existing seed data (idempotent)
-- -----------------------------------------------------------------------------
DELETE FROM cards;

INSERT INTO cards (card_num, card_acct_id, active_status, cash_credit_limit, open_date, expiry_date, reissue_date, curr_cycle_credit, curr_cycle_debit, group_id, sli, addr_zip, addr_state, addr_country, addr_line1, addr_line2, phone_number_1, phone_number_2, created_by, created_at, updated_by, updated_at, version)
VALUES
    ('4111111111111111', '12345678901', 'Y', 5000.00, '2020-01-01', '2025-01-01', NULL, 1000.00, 200.00, 'GRP1', 'A', '12345', 'NY', 'USA', '123 Main St', '', '555-1234', '', 'system', CURRENT_TIMESTAMP, NULL, NULL, 0),
    ('5500000000000004', '12345678902', 'Y', 3000.00, '2021-06-15', '2026-06-15', NULL, 500.00, 100.00, 'GRP2', 'B', '54321', 'CA', 'USA', '456 Elm St', '', '555-5678', '', 'system', CURRENT_TIMESTAMP, NULL, NULL, 0),
    ('340000000000009',  '12345678903', 'N', 2000.00, '2019-03-10', '2024-03-10', NULL, 200.00, 50.00,  'GRP1', 'C', '67890', 'TX', 'USA', '789 Oak St', '', '555-9012', '', 'system', CURRENT_TIMESTAMP, NULL, NULL, 0);

