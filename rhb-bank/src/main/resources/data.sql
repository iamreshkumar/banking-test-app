SET SCHEMA rbh_bank;

-- =========================
-- CUSTOMERS
-- =========================
INSERT INTO customers (
    full_name,
    email,
    pan_number,
    mobile_number
)
VALUES
('Amresh Kumar', 'amresh@bank.com', 'ABCDE1234F', '9876543210'),
('Rahul Sharma', 'rahul@bank.com',  'PQRSX5678L', '9123456789');


-- =========================
-- ACCOUNTS
-- =========================
INSERT INTO accounts (
    account_number,
    account_type,
    balance,
    status,
    version,
    customer_id
)
VALUES
('ACC10001', 'SAVINGS',  50000.00, 'ACTIVE', 0, 1),
('ACC10002', 'CURRENT', 150000.00, 'ACTIVE', 0, 1),
('ACC20001', 'SAVINGS',  75000.00, 'ACTIVE', 0, 2);
