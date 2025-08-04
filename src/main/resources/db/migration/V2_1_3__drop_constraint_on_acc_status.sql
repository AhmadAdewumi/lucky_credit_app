ALTER TABLE accounts
    ALTER COLUMN account_status DROP NOT NULL;
ALTER TABLE accounts
    ALTER COLUMN account_status SET DEFAULT 'ACTIVE';
