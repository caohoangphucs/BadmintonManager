-- Expanded Seed data for BadmintonManager

-- Clear existing data
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE `Booking_Promotion`;
TRUNCATE TABLE `Feedback`;
TRUNCATE TABLE `Service_Invoice`;
TRUNCATE TABLE `Equipment_Rental`;
TRUNCATE TABLE `Payment`;
TRUNCATE TABLE `Booking`;
TRUNCATE TABLE `Staff`;
TRUNCATE TABLE `Customer`;
TRUNCATE TABLE `Court`;
TRUNCATE TABLE `Equipment`;
TRUNCATE TABLE `Promotion`;
SET FOREIGN_KEY_CHECKS = 1;

-- Insert into Court (10 Courts)
INSERT INTO `Court` (`court_name`, `court_type`, `status`, `price_per_hour`) VALUES
('Sân số 1', 'Thảm Hải Yến', 'Available', 80000.00),
('Sân số 2', 'Thảm Hải Yến', 'Available', 80000.00),
('Sân số 3', 'Thảm Hải Yến', 'Available', 80000.00),
('Sân số 4', 'Thảm Yonex', 'Available', 100000.00),
('Sân số 5', 'Thảm Yonex', 'Available', 100000.00),
('Sân số 6', 'Thảm Yonex', 'Available', 100000.00),
('Sân số 7', 'Sân Gỗ', 'Available', 60000.00),
('Sân số 8', 'Sân Gỗ', 'Available', 60000.00),
('Sân số 9', 'Sân Gỗ', 'Maintenance', 60000.00),
('Sân số 10', 'Thảm Đặc Biệt', 'Available', 120000.00);

-- Insert into Customer (20 Customers)
INSERT INTO `Customer` (`full_name`, `phone_number`, `email`, `membership_type`) VALUES
('Nguyễn Văn A', '0901234567', 'vana@gmail.com', 'Gold'),
('Trần Thị B', '0907654321', 'thib@yahoo.com', 'Silver'),
('Lê Văn C', '0912345678', 'vanc@outlook.com', 'Regular'),
('Phạm Thị D', '0987654321', 'thid@gmail.com', 'Gold'),
('Hoàng Văn E', '0934567890', 'vane@gmail.com', 'Regular'),
('Đỗ Minh F', '0945678901', 'minhf@gmail.com', 'Silver'),
('Vũ Thị G', '0956789012', 'thig@gmail.com', 'Regular'),
('Phan Văn H', '0967890123', 'vanh@gmail.com', 'Gold'),
('Trần Văn I', '0978901234', 'vani@gmail.com', 'Silver'),
('Ngô Thị K', '0989012345', 'thik@gmail.com', 'Regular'),
('Lý Văn L', '0990123456', 'vanl@gmail.com', 'Regular'),
('Đặng Thị M', '0900988776', 'thim@gmail.com', 'Regular'),
('Bùi Văn N', '0911223344', 'vann@gmail.com', 'Gold'),
('Hồ Thị O', '0922334455', 'thio@gmail.com', 'Silver'),
('Vương Văn P', '0933445566', 'vanp@gmail.com', 'Regular'),
('Trương Thị Q', '0944556677', 'thiq@gmail.com', 'Regular'),
('Lương Văn R', '0955667788', 'vanr@gmail.com', 'Silver'),
('Dương Thị S', '0966778899', 'this@gmail.com', 'Gold'),
('Mai Văn T', '0977889900', 'vant@gmail.com', 'Regular'),
('Đào Thị U', '0988990011', 'thiu@gmail.com', 'Silver');

-- Insert into Staff (8 Staff)
INSERT INTO `Staff` (`full_name`, `role`, `phone_number`, `email`) VALUES
('Admin Manager', 'Manager', '0123456789', 'admin@badminton.com'),
('Nguyễn Văn Bảo', 'Receptionist', '0944556677', 'bao.nv@badminton.com'),
('Trần Lê Vy', 'Receptionist', '0955667788', 'vy.tl@badminton.com'),
('Lê Minh Tâm', 'Maintenance', '0966778899', 'tam.lm@badminton.com'),
('Phạm Hoàng Nam', 'Receptionist', '0977881122', 'nam.ph@badminton.com'),
('Hoàng Thị Lan', 'Cleaning', '0988992233', 'lan.ht@badminton.com'),
('Lý Trung Kiên', 'Security', '0999003344', 'kien.lt@badminton.com'),
('Vũ Văn Đạt', 'Manager', '0900114455', 'dat.vv@badminton.com');

-- Insert into Equipment (10 Items)
INSERT INTO `Equipment` (`equipment_name`, `quantity`, `condition`, `price`) VALUES
('Vợt Yonex Astrox 88D', 15, 'Good', 15000.00),
('Vợt Victor Thruster F', 10, 'Excellent', 20000.00),
('Giày cầu lông Lining', 20, 'Good', 10000.00),
('Giày cầu lông Mizuno', 10, 'Excellent', 15000.00),
('Ống cầu Thành Công', 100, 'New', 5000.00),
('Quấn cán Yonex', 50, 'New', 15000.00),
('Áo thun CLB', 30, 'New', 120000.00),
('Quần ngắn Lining', 20, 'New', 80000.00),
('Túi đựng vợt Yonex', 5, 'Good', 25000.00),
('Bình nước giữ nhiệt', 15, 'New', 50000.00);

-- Insert into Promotion (5 Promotions)
INSERT INTO `Promotion` (`promo_name`, `discount_percentage`, `start_date`, `end_date`) VALUES
('Chào Hè 2026', 10.00, '2026-04-01', '2026-06-30'),
('Tri ân Khách hàng Gold', 20.00, '2026-01-01', '2026-12-31'),
('Giờ Vàng Giá Sốc', 15.00, '2026-04-01', '2026-04-30'),
('Khai trương sân 10', 50.00, '2026-04-01', '2026-04-07'),
('Cuối tuần sôi động', 5.00, '2026-01-01', '2026-12-31');

-- Insert into Booking (30+ Bookings for multiple days)
-- 2026-04-02
INSERT INTO `Booking` (`customer_id`, `court_id`, `booking_date`, `start_time`, `end_time`, `total_price`, `status`) VALUES
(1, 1, '2026-04-02', '08:00:00', '10:00:00', 160000.00, 'Confirmed'),
(2, 2, '2026-04-02', '08:00:00', '10:00:00', 160000.00, 'Confirmed'),
(3, 4, '2026-04-02', '17:00:00', '19:00:00', 200000.00, 'Confirmed'),
(4, 5, '2026-04-02', '18:00:00', '20:00:00', 200000.00, 'Confirmed'),
(5, 7, '2026-04-02', '19:00:00', '21:00:00', 120000.00, 'Confirmed'),
(6, 10, '2026-04-02', '20:00:00', '22:00:00', 240000.00, 'Confirmed'),
-- 2026-04-03
(7, 1, '2026-04-03', '06:00:00', '08:00:00', 160000.00, 'Confirmed'),
(8, 2, '2026-04-03', '06:00:00', '08:00:00', 160000.00, 'Confirmed'),
(9, 3, '2026-04-03', '07:00:00', '09:00:00', 160000.00, 'Confirmed'),
(10, 4, '2026-04-03', '17:00:00', '19:00:00', 200000.00, 'Confirmed'),
(11, 5, '2026-04-03', '18:00:00', '20:00:00', 200000.00, 'Pending'),
(12, 6, '2026-04-03', '19:00:00', '21:00:00', 200000.00, 'Cancelled'),
(13, 8, '2026-04-03', '20:00:00', '22:00:00', 120000.00, 'Confirmed'),
(14, 10, '2026-04-03', '20:00:00', '21:00:00', 120000.00, 'Confirmed'),
-- 2026-04-04 (Weekend)
(15, 1, '2026-04-04', '08:00:00', '10:00:00', 160000.00, 'Confirmed'),
(16, 2, '2026-04-04', '08:00:00', '10:00:00', 160000.00, 'Confirmed'),
(17, 3, '2026-04-04', '10:00:00', '12:00:00', 160000.00, 'Confirmed'),
(18, 4, '2026-04-04', '14:00:00', '16:00:00', 200000.00, 'Confirmed'),
(19, 5, '2026-04-04', '16:00:00', '18:00:00', 200000.00, 'Confirmed'),
(20, 6, '2026-04-04', '18:00:00', '20:00:00', 200000.00, 'Confirmed'),
(1, 4, '2026-04-04', '19:00:00', '21:00:00', 200000.00, 'Confirmed'),
(2, 5, '2026-04-04', '20:00:00', '22:00:00', 200000.00, 'Confirmed'),
-- 2026-04-05
(3, 1, '2026-04-05', '07:00:00', '09:00:00', 160000.00, 'Confirmed'),
(4, 2, '2026-04-05', '07:00:00', '09:00:00', 160000.00, 'Confirmed'),
(5, 3, '2026-04-05', '09:00:00', '11:00:00', 160000.00, 'Confirmed'),
(6, 4, '2026-04-05', '13:00:00', '15:00:00', 200000.00, 'Confirmed'),
(7, 5, '2026-04-05', '15:00:00', '17:00:00', 200000.00, 'Confirmed'),
(8, 6, '2026-04-05', '17:00:00', '19:00:00', 200000.00, 'Confirmed'),
(9, 7, '2026-04-05', '19:00:00', '21:00:00', 120000.00, 'Confirmed'),
(10, 8, '2026-04-05', '20:00:00', '22:00:00', 120000.00, 'Confirmed');

-- Insert into Payment
INSERT INTO `Payment` (`booking_id`, `payment_date`, `amount`, `payment_method`, `status`) VALUES
(1, '2026-04-01 10:00:00', 160000.00, 'Cash', 'Paid'),
(2, '2026-04-01 11:30:00', 160000.00, 'Momo', 'Paid'),
(3, '2026-04-02 09:00:00', 200000.00, 'Bank Transfer', 'Paid'),
(4, '2026-04-02 10:00:00', 200000.00, 'VNPay', 'Paid'),
(5, '2026-04-02 11:00:00', 120000.00, 'Cash', 'Paid'),
(6, '2026-04-02 15:00:00', 240000.00, 'Momo', 'Paid'),
(7, '2026-04-02 16:00:00', 160000.00, 'Bank Transfer', 'Paid'),
(8, '2026-04-03 08:30:00', 160000.00, 'Cash', 'Paid'),
(9, '2026-04-03 10:00:00', 160000.00, 'VNPay', 'Paid'),
(10, '2026-04-03 12:00:00', 200000.00, 'Cash', 'Paid'),
(13, '2026-04-03 14:00:00', 120000.00, 'Momo', 'Paid'),
(14, '2026-04-03 15:30:00', 120000.00, 'Bank Transfer', 'Paid'),
(15, '2026-04-04 07:00:00', 160000.00, 'Cash', 'Paid'),
(16, '2026-04-04 07:30:00', 160000.00, 'VNPay', 'Paid'),
(17, '2026-04-04 09:00:00', 160000.00, 'Momo', 'Paid'),
(18, '2026-04-04 13:00:00', 200000.00, 'Cash', 'Paid'),
(19, '2026-04-04 15:00:00', 200000.00, 'Bank Transfer', 'Paid');

-- Insert into Equipment_Rental
INSERT INTO `Equipment_Rental` (`equipment_id`, `booking_id`, `quantity`, `rental_price`) VALUES
(1, 1, 2, 30000.00),
(5, 1, 1, 5000.00),
(2, 2, 1, 20000.00),
(5, 2, 2, 10000.00),
(3, 3, 2, 20000.00),
(4, 3, 1, 15000.00),
(1, 10, 4, 60000.00),
(5, 10, 3, 15000.00),
(2, 15, 2, 40000.00),
(5, 15, 2, 10000.00),
(6, 18, 1, 15000.00),
(1, 25, 2, 30000.00);

-- Insert into Service_Invoice
INSERT INTO `Service_Invoice` (`booking_id`, `service_name`, `price`) VALUES
(1, 'Nước suối Aquafina', 10000.00),
(1, 'Nước tăng lực Revive', 15000.00),
(2, 'Trà ô long', 12000.00),
(3, 'Nước suối Aquafina', 10000.00),
(4, 'Nước Cam Ep', 20000.00),
(5, 'Trà sữa trân châu', 35000.00),
(10, 'Nước suối Aquafina', 30000.00),
(15, 'Trà gừng ấm', 20000.00),
(20, 'Nước suối Aquafina', 10000.00),
(25, 'Khăn lạnh', 5000.00),
(30, 'Trà xanh không độ', 15000.00);

-- Insert into Feedback
INSERT INTO `Feedback` (`customer_id`, `booking_id`, `rating`, `comment`, `feedback_date`) VALUES
(1, 1, 5, 'Sân rất đẹp và sạch!', '2026-04-02 11:00:00'),
(2, 2, 4, 'Mọi thứ tốt, trừ việc bãi gửi xe hơi chật.', '2026-04-02 11:30:00'),
(3, 4, 5, 'Phục vụ tận tình, sân Yonex thảm êm thật.', '2026-04-02 20:00:00'),
(4, 5, 3, 'Giá hơi cao vào giờ cao điểm.', '2026-04-02 22:00:00'),
(5, 7, 5, 'Cảm ơn clb đã phục vụ chu đáo!', '2026-04-03 09:00:00'),
(6, 6, 2, 'Sân 10 đẹp nhưng giá quá đắt.', '2026-04-02 23:00:00'),
(7, 10, 4, 'Ok, sẽ quay lại thường xuyên.', '2026-04-03 20:00:00');

-- Insert into Booking_Promotion
INSERT INTO `Booking_Promotion` (`booking_id`, `promo_id`) VALUES
(1, 1),
(1, 3),
(4, 2),
(6, 4),
(10, 1),
(15, 5),
(20, 5),
(25, 5);
