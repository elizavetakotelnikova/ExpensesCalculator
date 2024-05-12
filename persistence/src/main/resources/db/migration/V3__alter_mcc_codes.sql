ALTER TABLE mcc_codes
DROP COLUMN mcc_code;
ALTER TABLE mcc_codes ADD COLUMN mcc_code int;

ALTER TABLE transactions
DROP COLUMN mcc_code;
ALTER TABLE transactions ADD COLUMN mcc_code int;