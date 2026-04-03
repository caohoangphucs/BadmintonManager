package com.example.winfinal.dao;

import jakarta.persistence.EntityManager;
import java.util.List;

public class ReportDAO {

    /**
     * 1. Consolidated Monthly Revenue
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getMonthlyRevenueReport() {
        String sql = "SELECT \n" +
                     "    MonthData.Month AS 'Tháng',\n" +
                     "    MonthData.CourtRevenue AS 'Doanh thu sân',\n" +
                     "    IFNULL(EquipData.EquipRevenue, 0) AS 'Doanh thu phụ kiện',\n" +
                     "    IFNULL(ServiceData.ServiceRevenue, 0) AS 'Doanh thu dịch vụ',\n" +
                     "    (MonthData.CourtRevenue + IFNULL(EquipData.EquipRevenue, 0) + IFNULL(ServiceData.ServiceRevenue, 0)) AS 'Tổng doanh thu'\n" +
                     "FROM (\n" +
                     "    SELECT \n" +
                     "        DATE_FORMAT(booking_date, '%Y-%m') AS Month,\n" +
                     "        SUM(total_price) AS CourtRevenue\n" +
                     "    FROM Booking\n" +
                     "    WHERE status = 'Confirmed'\n" +
                     "    GROUP BY Month\n" +
                     ") AS MonthData\n" +
                     "LEFT JOIN (\n" +
                     "    SELECT \n" +
                     "        DATE_FORMAT(b2.booking_date, '%Y-%m') AS Month,\n" +
                     "        SUM(er.rental_price * er.quantity) AS EquipRevenue\n" +
                     "    FROM Equipment_Rental er\n" +
                     "    JOIN Booking b2 ON er.booking_id = b2.booking_id\n" +
                     "    GROUP BY Month\n" +
                     ") AS EquipData ON MonthData.Month = EquipData.Month\n" +
                     "LEFT JOIN (\n" +
                     "    SELECT \n" +
                     "        DATE_FORMAT(b3.booking_date, '%Y-%m') AS Month,\n" +
                     "        SUM(si.price) AS ServiceRevenue\n" +
                     "    FROM Service_Invoice si\n" +
                     "    JOIN Booking b3 ON si.booking_id = b3.booking_id\n" +
                     "    GROUP BY Month\n" +
                     ") AS ServiceData ON MonthData.Month = ServiceData.Month\n" +
                     "ORDER BY MonthData.Month DESC";
        
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createNativeQuery(sql).getResultList();
        }
    }

    /**
     * 2. Monthly Revenue Growth (MoM)
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getMonthlyGrowthReport() {
        String sql = "SELECT \n" +
                     "    MonthYear AS 'Tháng',\n" +
                     "    TotalRevenue AS 'Doanh thu',\n" +
                     "    LAG(TotalRevenue) OVER (ORDER BY MonthYear) AS 'Tháng trước',\n" +
                     "    ROUND(((TotalRevenue - LAG(TotalRevenue) OVER (ORDER BY MonthYear)) / LAG(TotalRevenue) OVER (ORDER BY MonthYear)) * 100, 2) AS '% Tăng trưởng'\n" +
                     "FROM (\n" +
                     "    SELECT DATE_FORMAT(booking_date, '%Y-%m') AS MonthYear, SUM(total_price) AS TotalRevenue\n" +
                     "    FROM Booking WHERE status = 'Confirmed'\n" +
                     "    GROUP BY MonthYear\n" +
                     ") AS MonthlyData";
        
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createNativeQuery(sql).getResultList();
        }
    }

    /**
     * 3. Top 5 High-Value Customers
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getTop5Customers() {
        String sql = "SELECT \n" +
                     "    c.customer_id, c.full_name AS 'Khách hàng', c.membership_type AS 'Hạng TV',\n" +
                     "    COUNT(b.booking_id) AS 'Số lượt đặt',\n" +
                     "    SUM(p.amount) AS 'Tổng chi tiêu'\n" +
                     "FROM Customer c\n" +
                     "JOIN Booking b ON c.customer_id = b.customer_id\n" +
                     "JOIN Payment p ON b.booking_id = p.booking_id\n" +
                     "WHERE p.status = 'Paid'\n" +
                     "GROUP BY c.customer_id, c.full_name, c.membership_type\n" +
                     "ORDER BY SUM(p.amount) DESC\n" +
                     "LIMIT 5";
        
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createNativeQuery(sql).getResultList();
        }
    }

    /**
     * 5. Court Utilization (%)
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getCourtUtilizationReport() {
        String sql = "SELECT \n" +
                     "    c.court_name AS 'Sân', c.court_type AS 'Loại sân',\n" +
                     "    SUM(TIME_TO_SEC(TIMEDIFF(b.end_time, b.start_time)) / 3600) AS 'Tổng giờ đặt',\n" +
                     "    (COUNT(DISTINCT b.booking_date) * 16) AS 'Tổng giờ mở',\n" +
                     "    ROUND((SUM(TIME_TO_SEC(TIMEDIFF(b.end_time, b.start_time)) / 3600) / (COUNT(DISTINCT b.booking_date) * 16)) * 100, 2) AS '% Hiệu suất'\n" +
                     "FROM Court c\n" +
                     "LEFT JOIN Booking b ON c.court_id = b.court_id\n" +
                     "WHERE b.status = 'Confirmed'\n" +
                     "GROUP BY c.court_id, c.court_name, c.court_type\n" +
                     "ORDER BY 5 DESC";
        
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createNativeQuery(sql).getResultList();
        }
    }

    /**
     * 7. Peak Hours Analysis
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getPeakHoursAnalysis() {
        String sql = "SELECT \n" +
                     "    HOUR(start_time) AS 'Giờ',\n" +
                     "    COUNT(*) AS 'Số lượt đặt',\n" +
                     "    CASE \n" +
                     "        WHEN HOUR(start_time) BETWEEN 6 AND 10 THEN 'Sáng sớm'\n" +
                     "        WHEN HOUR(start_time) BETWEEN 11 AND 14 THEN 'Trưa'\n" +
                     "        WHEN HOUR(start_time) BETWEEN 15 AND 17 THEN 'Chiều'\n" +
                     "        WHEN HOUR(start_time) BETWEEN 18 AND 22 THEN 'Tối (Cao điểm)'\n" +
                     "        ELSE 'Khác'\n" +
                     "    END AS 'Giai đoạn'\n" +
                     "FROM Booking \n" +
                     "WHERE status = 'Confirmed'\n" +
                     "GROUP BY HOUR(start_time), 3\n" +
                     "ORDER BY 2 DESC";
        
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createNativeQuery(sql).getResultList();
        }
    }

    /**
     * 8. Popular Equipment
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getPopularEquipmentReport() {
        String sql = "SELECT \n" +
                     "    e.equipment_name,\n" +
                     "    COUNT(er.rental_id) AS 'RentalCount',\n" +
                     "    SUM(er.quantity) AS 'TotalQuantity',\n" +
                     "    SUM(er.rental_price * er.quantity) AS 'TotalRevenue'\n" +
                     "FROM Equipment e\n" +
                     "JOIN Equipment_Rental er ON e.equipment_id = er.equipment_id\n" +
                     "GROUP BY e.equipment_id, e.equipment_name\n" +
                     "ORDER BY 3 DESC";
        
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createNativeQuery(sql).getResultList();
        }
    }

    /**
     * 9. Unpaid Bookings
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getUnpaidBookings() {
        String sql = "SELECT \n" +
                     "    b.booking_id, c.full_name, b.booking_date, b.total_price,\n" +
                     "    IFNULL(SUM(p.amount), 0) AS 'PaidAmount',\n" +
                     "    (b.total_price - IFNULL(SUM(p.amount), 0)) AS 'RemainingAmount'\n" +
                     "FROM Booking b\n" +
                     "JOIN Customer c ON b.customer_id = c.customer_id\n" +
                     "LEFT JOIN Payment p ON b.booking_id = p.booking_id AND p.status = 'Paid'\n" +
                     "WHERE b.status = 'Confirmed'\n" +
                     "GROUP BY b.booking_id, c.full_name, b.booking_date, b.total_price\n" +
                     "HAVING (b.total_price - IFNULL(SUM(p.amount), 0)) > 0";
        
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createNativeQuery(sql).getResultList();
        }
    }

    /**
     * 10. Feedback Ratings
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getFeedbackRatingsReport() {
        String sql = "SELECT \n" +
                     "    c.court_name,\n" +
                     "    AVG(f.rating) AS 'AvgRating',\n" +
                     "    COUNT(f.feedback_id) AS 'FeedbackCount',\n" +
                     "    GROUP_CONCAT(f.comment SEPARATOR ' | ') AS 'Comments'\n" +
                     "FROM Court c\n" +
                     "JOIN Booking b ON c.court_id = b.court_id\n" +
                     "JOIN Feedback f ON b.booking_id = f.booking_id\n" +
                     "GROUP BY c.court_id, c.court_name\n" +
                     "ORDER BY 2 DESC";
        
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createNativeQuery(sql).getResultList();
        }
    }

    /**
     * 11. Expiring Promotions
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getExpiringPromotions() {
        String sql = "SELECT \n" +
                     "    promo_name, discount_percentage, start_date, end_date,\n" +
                     "    DATEDIFF(end_date, CURRENT_DATE()) AS 'DaysLeft'\n" +
                     "FROM Promotion\n" +
                     "WHERE end_date >= CURRENT_DATE() \n" +
                     "AND DATEDIFF(end_date, CURRENT_DATE()) <= 30\n" +
                     "ORDER BY end_date ASC";
        
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createNativeQuery(sql).getResultList();
        }
    }

    /**
     * 12. Cancellation Rate
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getCancellationRateReport() {
        String sql = "SELECT \n" +
                     "    c.full_name,\n" +
                     "    COUNT(CASE WHEN b.status = 'Cancelled' THEN 1 END) AS 'CancelCount',\n" +
                     "    COUNT(b.booking_id) AS 'TotalBookings',\n" +
                     "    ROUND((COUNT(CASE WHEN b.status = 'Cancelled' THEN 1 END) / COUNT(b.booking_id)) * 100, 2) AS 'CancelRate'\n" +
                     "FROM Customer c\n" +
                     "JOIN Booking b ON c.customer_id = b.customer_id\n" +
                     "GROUP BY c.customer_id, c.full_name\n" +
                     "ORDER BY 4 DESC";
        
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createNativeQuery(sql).getResultList();
        }
    }

    /**
     * 15. Payment Methods Stats
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getPaymentMethodStats() {
        String sql = "SELECT \n" +
                     "    payment_method,\n" +
                     "    COUNT(*) AS 'TransactionCount',\n" +
                     "    SUM(amount) AS 'TotalAmount',\n" +
                     "    ROUND((COUNT(*) * 100.0 / (SELECT COUNT(*) FROM Payment)), 2) AS 'Percentage'\n" +
                     "FROM Payment\n" +
                     "WHERE status = 'Paid'\n" +
                     "GROUP BY payment_method\n" +
                     "ORDER BY 2 DESC";
        
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createNativeQuery(sql).getResultList();
        }
    }

    /**
     * KPI: Total Revenue (all time, confirmed bookings)
     */
    public Object[] getTotalRevenueSummary() {
        String sql = "SELECT " +
                     "    IFNULL(SUM(b.total_price), 0) AS TotalCourtRevenue, " +
                     "    IFNULL((SELECT SUM(er.rental_price * er.quantity) FROM Equipment_Rental er " +
                     "           JOIN Booking b2 ON er.booking_id = b2.booking_id WHERE b2.status = 'Confirmed'), 0) AS TotalEquipRevenue, " +
                     "    IFNULL((SELECT SUM(si.price) FROM Service_Invoice si " +
                     "           JOIN Booking b3 ON si.booking_id = b3.booking_id WHERE b3.status = 'Confirmed'), 0) AS TotalServiceRevenue " +
                     "FROM Booking b WHERE b.status = 'Confirmed'";
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            List<?> result = em.createNativeQuery(sql).getResultList();
            if (!result.isEmpty()) return (Object[]) result.get(0);
            return new Object[]{0, 0, 0};
        }
    }

    /**
     * KPI: Total Bookings count and breakdown
     */
    public Object[] getTotalBookingsSummary() {
        String sql = "SELECT " +
                     "    COUNT(*) AS TotalBookings, " +
                     "    COUNT(CASE WHEN status = 'Confirmed' THEN 1 END) AS Confirmed, " +
                     "    COUNT(CASE WHEN status = 'Pending' THEN 1 END) AS Pending, " +
                     "    COUNT(CASE WHEN status = 'Cancelled' THEN 1 END) AS Cancelled " +
                     "FROM Booking";
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            List<?> result = em.createNativeQuery(sql).getResultList();
            if (!result.isEmpty()) return (Object[]) result.get(0);
            return new Object[]{0, 0, 0, 0};
        }
    }

    /**
     * KPI: Active courts count
     */
    public Object[] getActiveCourtsSummary() {
        String sql = "SELECT " +
                     "    COUNT(*) AS TotalCourts, " +
                     "    COUNT(CASE WHEN status = 'Available' THEN 1 END) AS ActiveCourts " +
                     "FROM Court";
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            List<?> result = em.createNativeQuery(sql).getResultList();
            if (!result.isEmpty()) return (Object[]) result.get(0);
            return new Object[]{0, 0};
        }
    }

    /**
     * KPI: Expiring promotions count (within 30 days)
     */
    public Object[] getExpiringPromotionsSummary() {
        String sql = "SELECT " +
                     "    COUNT(*) AS ExpiringCount, " +
                     "    COUNT(CASE WHEN DATEDIFF(end_date, CURRENT_DATE()) <= 3 THEN 1 END) AS UrgentCount " +
                     "FROM Promotion " +
                     "WHERE end_date >= CURRENT_DATE() AND DATEDIFF(end_date, CURRENT_DATE()) <= 30";
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            List<?> result = em.createNativeQuery(sql).getResultList();
            if (!result.isEmpty()) return (Object[]) result.get(0);
            return new Object[]{0, 0};
        }
    }
}

