-- Advanced Analytical Queries for BadmintonManager (15+ Queries)

-- 1. Báo cáo doanh thu tổng hợp hàng tháng (Consolidated Monthly Revenue)
-- Tính tổng doanh thu từ tiền sân, thuê đồ, và dịch vụ
SELECT 
    DATE_FORMAT(b.booking_date, '%Y-%m') AS 'Tháng',
    SUM(b.total_price) AS 'Doanh thu tiền sân',
    IFNULL((SELECT SUM(er.rental_price * er.quantity) 
            FROM Equipment_Rental er 
            JOIN Booking b2 ON er.booking_id = b2.booking_id 
            WHERE DATE_FORMAT(b2.booking_date, '%Y-%m') = DATE_FORMAT(b.booking_date, '%Y-%m')), 0) AS 'Doanh thu thuê đồ',
    IFNULL((SELECT SUM(si.price) 
            FROM Service_Invoice si 
            JOIN Booking b3 ON si.booking_id = b3.booking_id 
            WHERE DATE_FORMAT(b3.booking_date, '%Y-%m') = DATE_FORMAT(b.booking_date, '%Y-%m')), 0) AS 'Doanh thu dịch vụ',
    (SUM(b.total_price) + 
     IFNULL((SELECT SUM(er.rental_price * er.quantity) FROM Equipment_Rental er JOIN Booking b2 ON er.booking_id = b2.booking_id WHERE DATE_FORMAT(b2.booking_date, '%Y-%m') = DATE_FORMAT(b.booking_date, '%Y-%m')), 0) + 
     IFNULL((SELECT SUM(si.price) FROM Service_Invoice si JOIN Booking b3 ON si.booking_id = b3.booking_id WHERE DATE_FORMAT(b3.booking_date, '%Y-%m') = DATE_FORMAT(b.booking_date, '%Y-%m')), 0)
    ) AS 'Tổng doanh thu'
FROM Booking b
WHERE b.status = 'Confirmed'
GROUP BY DATE_FORMAT(b.booking_date, '%Y-%m')
ORDER BY 'Tháng' DESC;

-- 2. Tăng trưởng doanh thu theo tháng (MoM Growth)
-- Sử dụng Window Function (LAG) để Compare tháng trước
SELECT 
    MonthYear,
    TotalRevenue,
    LAG(TotalRevenue) OVER (ORDER BY MonthYear) AS 'Doanh thu tháng trước',
    ROUND(((TotalRevenue - LAG(TotalRevenue) OVER (ORDER BY MonthYear)) / LAG(TotalRevenue) OVER (ORDER BY MonthYear)) * 100, 2) AS 'Tăng trưởng (%)'
FROM (
    SELECT DATE_FORMAT(booking_date, '%Y-%m') AS MonthYear, SUM(total_price) AS TotalRevenue
    FROM Booking WHERE status = 'Confirmed'
    GROUP BY MonthYear
) AS MonthlyData;

-- 3. Top 5 khách hàng chi tiêu nhiều nhất (High-Value Customers)
SELECT 
    c.customer_id, c.full_name, c.membership_type,
    COUNT(b.booking_id) AS 'Số lần đặt sân',
    SUM(p.amount) AS 'Tổng chi tiêu'
FROM Customer c
JOIN Booking b ON c.customer_id = b.customer_id
JOIN Payment p ON b.booking_id = p.booking_id
WHERE p.status = 'Paid'
GROUP BY c.customer_id, c.full_name, c.membership_type
ORDER BY SUM(p.amount) DESC
LIMIT 5;

-- 4. Khách hàng thân thiết (Frequent Customers - Over 3 bookings)
SELECT 
    c.full_name, c.phone_number,
    COUNT(b.booking_id) AS 'Số lần đặt',
    AVG(b.total_price) AS 'Giá trị trung bình/lượt'
FROM Customer c
JOIN Booking b ON c.customer_id = b.customer_id
GROUP BY c.customer_id, c.full_name, c.phone_number
HAVING COUNT(b.booking_id) >= 3
ORDER BY 'Số lần đặt' DESC;

-- 5. Hiệu suất sử dụng sân (%) (Court Utilization)
-- Giả định sân hoạt động 16 tiếng/ngày (06:00 - 22:00)
SELECT 
    c.court_name, c.court_type,
    SUM(TIME_TO_SEC(TIMEDIFF(b.end_time, b.start_time)) / 3600) AS 'Tổng giờ đã đặt',
    (COUNT(DISTINCT b.booking_date) * 16) AS 'Tổng giờ mở cửa',
    ROUND((SUM(TIME_TO_SEC(TIMEDIFF(b.end_time, b.start_time)) / 3600) / (COUNT(DISTINCT b.booking_date) * 16)) * 100, 2) AS 'Tỷ lệ lấp đầy (%)'
FROM Court c
LEFT JOIN Booking b ON c.court_id = b.court_id
WHERE b.status = 'Confirmed'
GROUP BY c.court_id, c.court_name, c.court_type
ORDER BY 'Tỷ lệ lấp đầy (%)' DESC;

-- 6. Doanh thu theo loại sân (Revenue by Court Type)
SELECT 
    court_type,
    COUNT(booking_id) AS 'Lượt đặt',
    SUM(total_price) AS 'Tổng doanh thu',
    AVG(total_price) AS 'Doanh thu trung bình/lượt'
FROM Court c
JOIN Booking b ON c.court_id = b.court_id
WHERE b.status = 'Confirmed'
GROUP BY court_type;

-- 7. Phân tích khung giờ cao điểm (Peak Hours Analysis)
SELECT 
    HOUR(start_time) AS 'Giờ bắt đầu',
    COUNT(*) AS 'Số lượt đặt',
    CASE 
        WHEN HOUR(start_time) BETWEEN 6 AND 10 THEN 'Sáng sớm'
        WHEN HOUR(start_time) BETWEEN 11 AND 14 THEN 'Trưa'
        WHEN HOUR(start_time) BETWEEN 15 AND 17 THEN 'Chiều'
        WHEN HOUR(start_time) BETWEEN 18 AND 22 THEN 'Tối (Cao điểm)'
        ELSE 'Khác'
    END AS 'Giai đoạn'
FROM Booking
WHERE status = 'Confirmed'
GROUP BY HOUR(start_time)
ORDER BY 'Số lượt đặt' DESC;

-- 8. Thống kê thuê thiết bị (Popular Equipment)
SELECT 
    e.equipment_name,
    COUNT(er.rental_id) AS 'Số lượt thuê',
    SUM(er.quantity) AS 'Tổng số lượng thuê',
    SUM(er.rental_price * er.quantity) AS 'Tổng tiền thu được'
FROM Equipment e
JOIN Equipment_Rental er ON e.equipment_id = er.equipment_id
GROUP BY e.equipment_id, e.equipment_name
ORDER BY 'Tổng số lượng thuê' DESC;

-- 9. Tình trạng công nợ (Unpaid Bookings)
-- Những booking 'Confirmed' nhưng chưa có payment 'Paid' hoặc payment chưa đủ
SELECT 
    b.booking_id, c.full_name, b.booking_date, b.total_price,
    IFNULL(SUM(p.amount), 0) AS 'Đã thanh toán',
    (b.total_price - IFNULL(SUM(p.amount), 0)) AS 'Còn lại'
FROM Booking b
JOIN Customer c ON b.customer_id = c.customer_id
LEFT JOIN Payment p ON b.booking_id = p.booking_id AND p.status = 'Paid'
WHERE b.status = 'Confirmed'
GROUP BY b.booking_id, c.full_name, b.booking_date, b.total_price
HAVING (b.total_price - IFNULL(SUM(p.amount), 0)) > 0;

-- 10. Đánh giá chất lượng dịch vụ (Feedback Ratings)
SELECT 
    c.court_name,
    AVG(f.rating) AS 'Rating trung bình',
    COUNT(f.feedback_id) AS 'Số phản hồi',
    GROUP_CONCAT(f.comment SEPARATOR ' | ') AS 'Các ý kiến'
FROM Court c
JOIN Booking b ON c.court_id = b.court_id
JOIN Feedback f ON b.booking_id = f.booking_id
GROUP BY c.court_id, c.court_name
ORDER BY 'Rating trung bình' DESC;

-- 11. Các chương trình khuyến mãi sắp hết hạn (Expiring Promotions)
-- Trong vòng 30 ngày tới
SELECT 
    promo_name, discount_percentage, start_date, end_date,
    DATEDIFF(end_date, CURRENT_DATE()) AS 'Số ngày còn lại'
FROM Promotion
WHERE end_date >= CURRENT_DATE() 
AND DATEDIFF(end_date, CURRENT_DATE()) <= 30
ORDER BY end_date ASC;

-- 12. Phân tích tỷ lệ hủy sân (Cancellation Rate)
SELECT 
    c.full_name,
    COUNT(CASE WHEN b.status = 'Cancelled' THEN 1 END) AS 'Số lần hủy',
    COUNT(b.booking_id) AS 'Tổng lượt đặt',
    ROUND((COUNT(CASE WHEN b.status = 'Cancelled' THEN 1 END) / COUNT(b.booking_id)) * 100, 2) AS 'Tỷ lệ hủy (%)'
FROM Customer c
JOIN Booking b ON c.customer_id = b.customer_id
GROUP BY c.customer_id, c.full_name
ORDER BY 'Tỷ lệ hủy (%)' DESC;

-- 13. Thời gian đặt sân trung bình (Average Booking Duration)
SELECT 
    membership_type,
    ROUND(AVG(TIME_TO_SEC(TIMEDIFF(end_time, start_time)) / 3600), 1) AS 'Giờ chơi trung bình/lượt'
FROM Customer c
JOIN Booking b ON c.customer_id = b.customer_id
WHERE b.status = 'Confirmed'
GROUP BY membership_type;

-- 14. Thiết bị cần bảo trì hoặc bổ sung (Low Stock / Poor Condition)
SELECT equipment_name, quantity, `condition`
FROM Equipment
WHERE quantity < 15 OR `condition` IN ('Fair', 'Poor', 'Maintenance');

-- 15. Thống kê phương thức thanh toán phổ biến (Payment Methods)
SELECT 
    payment_method,
    COUNT(*) AS 'Số giao dịch',
    SUM(amount) AS 'Tổng tiền qua phương thức này',
    ROUND((COUNT(*) * 100.0 / (SELECT COUNT(*) FROM Payment)), 2) AS 'Tỷ lệ %'
FROM Payment
WHERE status = 'Paid'
GROUP BY payment_method
ORDER BY 'Số giao dịch' DESC;

-- 16. Doanh thu dịch vụ (Service Revenue Breakdown)
SELECT 
    service_name,
    COUNT(*) AS 'Số lượt gọi',
    SUM(price) AS 'Tổng tiền thu được'
FROM Service_Invoice
GROUP BY service_name
ORDER BY 'Tổng tiền thu được' DESC;
