CREATE DATABASE IF NOT EXISTS scd_records
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE scd_records;

-- Student records table
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    reg_no VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    cgpa DECIMAL(3,2) NOT NULL,
    CHECK (cgpa BETWEEN 0.00 AND 4.00)
);

-- User authentication table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- Application database user
-- Replace the placeholder before running on a new system.
CREATE USER IF NOT EXISTS 'student_app'@'localhost'
IDENTIFIED BY 'REPLACE_WITH_STRONG_PASSWORD';

-- Student record permissions
GRANT SELECT, INSERT, UPDATE, DELETE
ON scd_records.students
TO 'student_app'@'localhost';

-- Authentication permissions
GRANT SELECT, INSERT
ON scd_records.users
TO 'student_app'@'localhost';
