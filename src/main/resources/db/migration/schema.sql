DROP SCHEMA IF EXISTS payments CASCADE;
CREATE SCHEMA payments;
USE payments; 

CREATE SEQUENCE account_seq START WITH 10 INCREMENT BY 1;

CREATE SEQUENCE address_seq START WITH 10 INCREMENT BY 1;

CREATE SEQUENCE company_seq START WITH 10 INCREMENT BY 1;

CREATE SEQUENCE currency_seq START WITH 10 INCREMENT BY 1;

CREATE SEQUENCE operation_seq START WITH 10 INCREMENT BY 1;

CREATE SEQUENCE payment_document_seq START WITH 10 INCREMENT BY 1;

CREATE TABLE account
(
    id             BIGINT       NOT NULL,
    account_number VARCHAR(255) NOT NULL,
    account_name   VARCHAR(255) NOT NULL,
    currency_id    BIGINT       NOT NULL,
    amount         DECIMAL      NOT NULL,
    company_id     BIGINT       NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (id)
);

CREATE TABLE address
(
    id         BIGINT       NOT NULL,
    city       VARCHAR(255) NOT NULL,
    street     VARCHAR(255) NOT NULL,
    company_id BIGINT,
    CONSTRAINT pk_address PRIMARY KEY (id)
);

CREATE TABLE company
(
    id            BIGINT       NOT NULL,
    company_name  VARCHAR(255) NOT NULL,
    company_code  INT          NOT NULL,
    email_address VARCHAR(255) NOT NULL,
    phone_number  VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP    NOT NULL,
    CONSTRAINT pk_company PRIMARY KEY (id)
);

CREATE TABLE currency
(
    id         BIGINT NOT NULL,
    short_name VARCHAR(255),
    long_name  VARCHAR(255),
    CONSTRAINT pk_currency PRIMARY KEY (id)
);

CREATE TABLE operation
(
    id              BIGINT       NOT NULL,
    document_id     BIGINT,
    operation_type  VARCHAR(255) NOT NULL,
    account         BIGINT       NOT NULL,
    corresp_account BIGINT       NOT NULL,
    credit_amount   DECIMAL,
    debit_amount    DECIMAL,
    currency_id     BIGINT       NOT NULL,
    created_at      TIMESTAMP    NOT NULL,
    CONSTRAINT pk_operation PRIMARY KEY (id)
);

CREATE TABLE payment_document
(
    id              BIGINT       NOT NULL,
    operation_type  VARCHAR(255) NOT NULL,
    account         BIGINT       NOT NULL,
    corresp_account BIGINT       NOT NULL,
    amount          DECIMAL      NOT NULL,
    currency_id     BIGINT       NOT NULL,
    document_status VARCHAR(255) NOT NULL,
    created_at      TIMESTAMP    NOT NULL,
    CONSTRAINT pk_payment_document PRIMARY KEY (id)
);

ALTER TABLE account
    ADD CONSTRAINT FK_ACCOUNT_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE account
    ADD CONSTRAINT FK_ACCOUNT_ON_CURRENCY FOREIGN KEY (currency_id) REFERENCES currency (id);

ALTER TABLE address
    ADD CONSTRAINT FK_ADDRESS_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE operation
    ADD CONSTRAINT FK_OPERATION_ON_ACCOUNT FOREIGN KEY (account) REFERENCES account (id);

ALTER TABLE operation
    ADD CONSTRAINT FK_OPERATION_ON_CORRESP_ACCOUNT FOREIGN KEY (corresp_account) REFERENCES account (id);

ALTER TABLE operation
    ADD CONSTRAINT FK_OPERATION_ON_CURRENCY FOREIGN KEY (currency_id) REFERENCES currency (id);

ALTER TABLE operation
    ADD CONSTRAINT FK_OPERATION_ON_DOCUMENT FOREIGN KEY (document_id) REFERENCES payment_document (id);

ALTER TABLE payment_document
    ADD CONSTRAINT FK_PAYMENT_DOCUMENT_ON_ACCOUNT FOREIGN KEY (account) REFERENCES account (id);

ALTER TABLE payment_document
    ADD CONSTRAINT FK_PAYMENT_DOCUMENT_ON_CORRESP_ACCOUNT FOREIGN KEY (corresp_account) REFERENCES account (id);

ALTER TABLE payment_document
    ADD CONSTRAINT FK_PAYMENT_DOCUMENT_ON_CURRENCY FOREIGN KEY (currency_id) REFERENCES currency (id);
