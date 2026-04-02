SET NAMES utf8mb4;

CREATE TABLE `Court` (
  `court_id` int PRIMARY KEY AUTO_INCREMENT,
  `court_name` varchar(50),
  `court_type` varchar(20),
  `status` varchar(20),
  `price_per_hour` decimal(10,2)
);

CREATE TABLE `Customer` (
  `customer_id` int PRIMARY KEY AUTO_INCREMENT,
  `full_name` varchar(100),
  `phone_number` varchar(20),
  `email` varchar(100),
  `membership_type` varchar(20)
);

CREATE TABLE `Staff` (
  `staff_id` int PRIMARY KEY AUTO_INCREMENT,
  `full_name` varchar(100),
  `role` varchar(50),
  `phone_number` varchar(20),
  `email` varchar(100)
);

CREATE TABLE `Booking` (
  `booking_id` int PRIMARY KEY AUTO_INCREMENT,
  `customer_id` int,
  `court_id` int,
  `booking_date` date,
  `start_time` time,
  `end_time` time,
  `total_price` decimal(10,2),
  `status` varchar(20)
);

CREATE TABLE `Payment` (
  `payment_id` int PRIMARY KEY AUTO_INCREMENT,
  `booking_id` int,
  `payment_date` datetime,
  `amount` decimal(10,2),
  `payment_method` varchar(20),
  `status` varchar(20)
);

CREATE TABLE `Equipment` (
  `equipment_id` int PRIMARY KEY AUTO_INCREMENT,
  `equipment_name` varchar(50),
  `quantity` int,
  `condition` varchar(20),
  `price` decimal(10,2)
);

CREATE TABLE `Equipment_Rental` (
  `rental_id` int PRIMARY KEY AUTO_INCREMENT,
  `equipment_id` int,
  `booking_id` int,
  `quantity` int,
  `rental_price` decimal(10,2)
);

CREATE TABLE `Service_Invoice` (
  `service_id` int PRIMARY KEY AUTO_INCREMENT,
  `booking_id` int,
  `service_name` varchar(50),
  `price` decimal(10,2)
);

CREATE TABLE `Feedback` (
  `feedback_id` int PRIMARY KEY AUTO_INCREMENT,
  `customer_id` int,
  `booking_id` int,
  `rating` int,
  `comment` text,
  `feedback_date` datetime
);

CREATE TABLE `Promotion` (
  `promo_id` int PRIMARY KEY AUTO_INCREMENT,
  `promo_name` varchar(50),
  `discount_percentage` decimal(5,2),
  `start_date` date,
  `end_date` date
);

CREATE TABLE `Booking_Promotion` (
  `booking_id` int,
  `promo_id` int,
  PRIMARY KEY (`booking_id`, `promo_id`)
);


ALTER TABLE `Booking` ADD FOREIGN KEY (`customer_id`) REFERENCES `Customer` (`customer_id`);

ALTER TABLE `Booking` ADD FOREIGN KEY (`court_id`) REFERENCES `Court` (`court_id`);

ALTER TABLE `Payment` ADD FOREIGN KEY (`booking_id`) REFERENCES `Booking` (`booking_id`);

ALTER TABLE `Equipment_Rental` ADD FOREIGN KEY (`equipment_id`) REFERENCES `Equipment` (`equipment_id`);

ALTER TABLE `Equipment_Rental` ADD FOREIGN KEY (`booking_id`) REFERENCES `Booking` (`booking_id`);

ALTER TABLE `Service_Invoice` ADD FOREIGN KEY (`booking_id`) REFERENCES `Booking` (`booking_id`);

ALTER TABLE `Feedback` ADD FOREIGN KEY (`customer_id`) REFERENCES `Customer` (`customer_id`);

ALTER TABLE `Feedback` ADD FOREIGN KEY (`booking_id`) REFERENCES `Booking` (`booking_id`);

ALTER TABLE `Booking_Promotion` ADD FOREIGN KEY (`booking_id`) REFERENCES `Booking` (`booking_id`);

ALTER TABLE `Booking_Promotion` ADD FOREIGN KEY (`promo_id`) REFERENCES `Promotion` (`promo_id`);
