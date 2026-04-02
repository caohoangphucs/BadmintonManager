package com.example.winfinal.dao;

import jakarta.persistence.EntityManager;
import java.util.List;

public class ReportDAO {

    /**
     * 1. Consolidated Monthly Revenue
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getMonthlyRevenueReport() {
        String sql = "SELECT " +
                     "    DATE_FORMAT(b.booking_date, '%Y-%m') AS 'Month',\n" +
                     "    SUM(b.total_price) AS 'CourtRevenue',\n" +
                     "    IFNULL((SELECT SUM(er.rental_price * er.quantity) \n" +
                     "            FROM Equipment_Rental er \n" +
                     "            JOIN Booking b2 ON er.booking_id = b2.booking_id \n" +
                     "            WHERE DATE_FORMAT(b2.booking_date, '%Y-%m') = DATE_FORMAT(b.booking_date, '%Y-%m')), 0) AS 'EquipRevenue',\n" +
                     "    IFNULL((SELECT SUM(si.price) \n" +
                     "            FROM Service_Invoice si \n" +
                     "            JOIN Booking b3 ON si.booking_id = b3.booking_id \n" +
                     "            WHERE DATE_FORMAT(b3.booking_date, '%Y-%m') = DATE_FORMAT(b.booking_date, '%Y-%m')), 0) AS 'ServiceRevenue',\n" +
                     "    (SUM(b.total_price) + \n" +
                     "     IFNULL((SELECT SUM(er.rental_price * er.quantity) FROM Equipment_Rental er JOIN Booking b2 ON er.booking_id = b2.booking_id WHERE DATE_FORMAT(b2.booking_date, '%Y-%m') = DATE_FORMAT(b.booking_date, '%Y-%m')), 0) + \n" +
                     "     IFNULL((SELECT SUM(si.price) FROM Service_Invoice si JOIN Booking b3 ON si.booking_id = b3.booking_id WHERE DATE_FORMAT(b3.booking_date, '%Y-%m') = DATE_FORMAT(b.booking_date, '%Y-%m')), 0)\n" +
                     "    ) AS 'TotalRevenue'\n" +
                     "FROM Booking b\n" +
                     "WHERE b.status = 'Confirmed'\n" +
                     "GROUP BY DATE_FORMAT(b.booking_date, '%Y-%m')\n" +
                     "ORDER BY 1 DESC";
        
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
                     "    MonthYear,\n" +
                     "    TotalRevenue,\n" +
                     "    LAG(TotalRevenue) OVER (ORDER BY MonthYear) AS 'LastMonthRevenue',\n" +
                     "    ROUND(((TotalRevenue - LAG(TotalRevenue) OVER (ORDER BY MonthYear)) / LAG(TotalRevenue) OVER (ORDER BY MonthYear)) * 100, 2) AS 'GrowthPercent'\n" +
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
                     "    c.customer_id, c.full_name, c.membership_type,\n" +
                     "    COUNT(b.booking_id) AS 'BookingCount',\n" +
                     "    SUM(p.amount) AS 'TotalSpending'\n" +
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
                     "    c.court_name, c.court_type,\n" +
                     "    SUM(TIME_TO_SEC(TIMEDIFF(b.end_time, b.start_time)) / 3600) AS 'TotalHours',\n" +
                     "    (COUNT(DISTINCT b.booking_date) * 16) AS 'OpenHours',\n" +
                     "    ROUND((SUM(TIME_TO_SEC(TIMEDIFF(b.end_time, b.start_time)) / 3600) / (COUNT(DISTINCT b.booking_date) * 16)) * 100, 2) AS 'UtilizationPercent'\n" +
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
                     "    HOUR(start_time) AS 'Hour',\n" +
                     "    COUNT(*) AS 'BookingCount',\n" +
                     "    CASE \n" +
                     "        WHEN HOUR(start_time) BETWEEN 6 AND 10 THEN 'Morning'\n" +
                     "        WHEN HOUR(start_time) BETWEEN 11 AND 14 THEN 'Noon'\n" +
                     "        WHEN HOUR(start_time) BETWEEN 15 AND 17 THEN 'Afternoon'\n" +
                     "        WHEN HOUR(start_time) BETWEEN 18 AND 22 THEN 'Evening (Peak)'\n" +
                     "        ELSE 'Other'\n" +
                     "    END AS 'Period'\n" +
                     "FROM Booking\n" +
                     "WHERE status = 'Confirmed'\n" +
                     "GROUP BY HOUR(start_time)\n" +
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
                     "JOIN Feedback f ON b.booking_id = f.feedback_id\n" +
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
}
