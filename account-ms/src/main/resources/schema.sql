CREATE SCHEMA IF NOT EXISTS account;
SET SCHEMA 'account';

CREATE TABLE IF NOT EXISTS account
(
    id     SERIAL PRIMARY KEY,
    account_number VARCHAR(36) NOT NULL,
    customer_id    varchar(36) NOT NULL,
    account_type   varchar(45) NOT NULL,
    account_status varchar(45) NOT NULL,
    created_at     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS event
(
    id              BIGSERIAL PRIMARY KEY NOT NULL,
    data            VARCHAR(255),
    status          VARCHAR(100),
    created_at      TIMESTAMP,
    last_updated_at TIMESTAMP
);