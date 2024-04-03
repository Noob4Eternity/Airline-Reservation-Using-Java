create database airline_reservation;
select * from passengers;

CREATE TABLE Admin (
	admin_id int,
    admin_username VARCHAR(20),
    admin_password VARCHAR(20),
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    email VARCHAR(20),
    address VARCHAR(50),
    phone_number VARCHAR(15),
    primary key(admin_id));

INSERT INTO admin (admin_id, admin_username, admin_password, first_name, last_name, email, address, phone_number)
VALUES
(1,'user1', 'pass1', 'John', 'Doe', 'user1@example.com', '123 Main St', '555-123-4567'),
(2,'user2', 'pass2', 'Jane', 'Doe', 'user2@example.com', '456 Main St', '555-123-4568'),
(3,'user3', 'pass3', 'Jim', 'Brown', 'user3@example.com', '789 Main St', '555-123-4569'),
(4,'user4', 'pass4', 'Jack', 'Black', 'user4@example.com', '321 Main St', '555-123-4560'),
(5,'user5', 'pass5', 'Ann', 'Green', 'user5@example.com', '654 Main St', '555-123-4561');

select * from admin_login_time;

truncate table flights;

CREATE TABLE IF NOT EXISTS admin_login_time (
    id INT AUTO_INCREMENT PRIMARY KEY,
    admin_username VARCHAR(100),
    login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
