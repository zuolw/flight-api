SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS PASSENGER;
DROP TABLE IF EXISTS BOOKING;
DROP TABLE IF EXISTS FLIGHT;
DROP TABLE IF EXISTS AIRPORT;
DROP TABLE IF EXISTS USER;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE USER (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    country VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE AIRPORT (
    airport_id BIGINT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL
);

CREATE TABLE FLIGHT (
    flight_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    flight_number VARCHAR(10) NOT NULL UNIQUE,
    departure_airport_id BIGINT,
    destination_airport_id BIGINT,
    departure_date DATE NOT NULL,
    departure_time TIME NOT NULL,
    arrival_time TIME NOT NULL,
    airline VARCHAR(50),
    origin VARCHAR(10) NOT NULL,
    destination VARCHAR(10) NOT NULL,
    available_seats INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (departure_airport_id) REFERENCES AIRPORT(airport_id),
    FOREIGN KEY (destination_airport_id) REFERENCES AIRPORT(airport_id)
);

CREATE TABLE BOOKING (
    booking_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    flight_id BIGINT,
    booking_number VARCHAR(20) NOT NULL UNIQUE,
    passenger_name VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    booking_time TIMESTAMP NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES USER(user_id),
    FOREIGN KEY (flight_id) REFERENCES FLIGHT(flight_id)
);

CREATE TABLE PASSENGER (
    passenger_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES BOOKING(booking_id)
);