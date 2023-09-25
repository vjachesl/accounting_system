INSERT INTO currency (id, short_name, long_name)
VALUES (1, 'UAH', 'Ukrainian Hryvna'),
       (2, 'USD', 'United States Dollar'),
       (3, 'EUR', 'Euro');

INSERT INTO company (id, company_name, company_code, email_address, phone_number, created_at)
VALUES (1, 'Super company', 12345678, 'contact@super-company.com', '+380443453456', NOW()),
       (2, 'Hyper company', 87654321, 'contact@hyper-company.com', '+380444563434', NOW()),
       (3, 'Small company', 43214321, 'contact@small-company.com', '+380443222323', NOW());

INSERT INTO address (id, city, street, company_id)
VALUES (1, 'Kyiv', 'vul Prorizna, build 1', 1),
       (2, 'Kyiv', 'vul Khreshatyk, build 34', 2),
       (3, 'Kyiv', 'vul Sahaidachnoho, build 2', 3);

INSERT INTO account (id, account_number, account_name, currency_id, amount, company_id)
VALUES (1, 'UA345334634567450464576', 'Super company main account', 1, 100000, 1),
       (2, 'UA678789345290876787967', 'Hyper company main account', 1, 900000, 2),
       (3, 'UA465478568432341235678', 'Small company main account', 2, 10000, 3);
