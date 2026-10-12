-- ساخت دیتابیس و جدول مخاطبین
-- این فایل را در phpMyAdmin (تب Import) یا با دستور mysql اجرا کنید.

CREATE DATABASE IF NOT EXISTS contacts_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE contacts_db;

CREATE TABLE IF NOT EXISTS contacts (
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(100) NOT NULL,
    phone VARCHAR(20)  NOT NULL,
    email VARCHAR(100) NOT NULL DEFAULT ''
) ENGINE = InnoDB;

-- چند رکورد نمونه برای تست
INSERT INTO contacts (name, phone, email) VALUES
    ('علی رضایی',   '09120000001', 'ali@example.com'),
    ('سارا محمدی',  '09120000002', 'sara@example.com'),
    ('رضا کریمی',   '09120000003', '');
