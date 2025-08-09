CREATE TABLE paystack_payment(
    id               SERIAL PRIMARY KEY,
    user_id          UUID NOT NULL REFERENCES users (id),
    reference        VARCHAR(255),
    amount           NUMERIC(15, 4),
    gateway_response TEXT,
    paid_at          VARCHAR(255),
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    created_on       TIMESTAMP WITHOUT TIME ZONE
);

ALTER TABLE users
    ALTER COLUMN username TYPE VARCHAR(155),
    ALTER COLUMN real_name TYPE VARCHAR(155),
    ALTER COLUMN phone TYPE VARCHAR(155),
    ALTER COLUMN occupation TYPE VARCHAR(155),
    ALTER COLUMN nationality TYPE VARCHAR(155);