-- 清除现有数据
DELETE FROM PASSENGER;
DELETE FROM BOOKING;
DELETE FROM FLIGHT;
DELETE FROM AIRPORT;
DELETE FROM USER;

-- Sample Data

-- USER Table
INSERT INTO USER (user_id, email, password, first_name, last_name, country, phone) VALUES
(1, 'user1@example.com', '$2a$10$aMtm1CxOMPtKJgLBUSjTsOiSiwRfRXge2FawpXW.2coX.DuAV1iCG', 'John', 'Doe', 'USA', '111-111-1111'),
(2, 'user2@example.com', '$2a$10$aMtm1CxOMPtKJgLBUSjTsOiSiwRfRXge2FawpXW.2coX.DuAV1iCG', 'Jane', 'Smith', 'Canada', '222-222-2222'),
(3, 'user3@example.com', '$2a$10$aMtm1CxOMPtKJgLBUSjTsOiSiwRfRXge2FawpXW.2coX.DuAV1iCG', 'Peter', 'Jones', 'UK', '333-333-3333'),
(4, 'user4@example.com', '$2a$10$aMtm1CxOMPtKJgLBUSjTsOiSiwRfRXge2FawpXW.2coX.DuAV1iCG', 'Mary', 'Brown', 'Australia', '444-444-4444'),
(5, 'user5@example.com', '$2a$10$aMtm1CxOMPtKJgLBUSjTsOiSiwRfRXge2FawpXW.2coX.DuAV1iCG', 'David', 'White', 'Germany', '555-555-5555');

-- AIRPORT Table
INSERT INTO AIRPORT (airport_id, code, name, city) VALUES
(101, 'JFK', 'John F. Kennedy International Airport', 'New York'),
(102, 'LAX', 'Los Angeles International Airport', 'Los Angeles'),
(103, 'LHR', 'Heathrow Airport', 'London'),
(104, 'HND', 'Haneda Airport', 'Tokyo'),
(105, 'CDG', 'Charles de Gaulle Airport', 'Paris');

-- FLIGHT Table
INSERT INTO FLIGHT (flight_id, flight_number, departure_airport_id, destination_airport_id, departure_date, departure_time, arrival_time, airline, origin, destination, available_seats, price, status, created_at, updated_at) VALUES
(1001, 'UA101', 101, 102, '2025-06-10', '08:00:00', '11:00:00', 'United Airlines', 'JFK', 'LAX', 150, 350.00, 'SCHEDULED', NOW(), NOW()),
(1002, 'DL202', 102, 103, '2025-06-11', '14:00:00', '22:00:00', 'Delta Air Lines', 'LAX', 'LHR', 200, 600.00, 'SCHEDULED', NOW(), NOW()),
(1003, 'BA303', 103, 104, '2025-06-12', '20:00:00', '06:00:00', 'British Airways', 'LHR', 'HND', 180, 750.00, 'SCHEDULED', NOW(), NOW()),
(1004, 'JL404', 104, 105, '2025-06-13', '09:00:00', '17:00:00', 'Japan Airlines', 'HND', 'CDG', 160, 550.00, 'SCHEDULED', NOW(), NOW()),
(1005, 'AF505', 105, 101, '2025-06-14', '11:00:00', '14:00:00', 'Air France', 'CDG', 'JFK', 190, 700.00, 'SCHEDULED', NOW(), NOW()),
(1006, 'UA102', 102, 101, '2025-06-11', '08:00:00', '11:00:00', 'Delta Airlines', 'LAX', 'JFK', 150, 350.00, 'SCHEDULED', NOW(), NOW());

-- BOOKING Table
INSERT INTO BOOKING (booking_id, user_id, flight_id, booking_number, passenger_name, status, booking_time, total_price) VALUES
(1, 1, 1001, 'REF001', 'John Doe', 'CONFIRMED', NOW(), 350.00),
(2, 2, 1002, 'REF002', 'Jane Smith', 'CONFIRMED', NOW(), 600.00),
(3, 3, 1003, 'REF003', 'Peter Jones', 'PENDING', NOW(), 750.00),
(4, 4, 1004, 'REF004', 'Mary Brown', 'CONFIRMED', NOW(), 550.00),
(5, 5, 1005, 'REF005', 'David White', 'CANCELLED', NOW(), 700.00);

-- PASSENGER Table
INSERT INTO PASSENGER (passenger_id, booking_id, first_name, last_name, email) VALUES
(1, 1, 'John', 'Doe', 'user1@example.com'),
(2, 2, 'Jane', 'Smith', 'user2@example.com'),
(3, 3, 'Peter', 'Jones', 'user3@example.com'),
(4, 4, 'Mary', 'Brown', 'user4@example.com'),
(5, 5, 'David', 'White', 'user5@example.com');
