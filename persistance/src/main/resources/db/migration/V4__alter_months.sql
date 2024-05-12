ALTER TABLE transactions
DROP COLUMN month;
ALTER TABLE transactions ADD COLUMN month VARCHAR(15);