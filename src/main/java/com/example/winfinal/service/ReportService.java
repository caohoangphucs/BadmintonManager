package com.example.winfinal.service;

import com.example.winfinal.dao.ReportDAO;
import java.util.List;

public class ReportService {
    private final ReportDAO reportDAO;

    public ReportService() {
        this.reportDAO = new ReportDAO();
    }

    public List<Object[]> getMonthlyRevenue() {
        return reportDAO.getMonthlyRevenueReport();
    }

    public List<Object[]> getTopCustomers() {
        return reportDAO.getTop5Customers();
    }

    public List<Object[]> getCourtUtilization() {
        return reportDAO.getCourtUtilizationReport();
    }

    public List<Object[]> getPeakHours() {
        return reportDAO.getPeakHoursAnalysis();
    }

    public List<Object[]> getPopularEquipment() {
        return reportDAO.getPopularEquipmentReport();
    }

    public List<Object[]> getUnpaidBookings() {
        return reportDAO.getUnpaidBookings();
    }

    public List<Object[]> getMonthlyGrowth() {
        return reportDAO.getMonthlyGrowthReport();
    }

    public List<Object[]> getFeedbackRatings() {
        return reportDAO.getFeedbackRatingsReport();
    }

    public List<Object[]> getExpiringPromotions() {
        return reportDAO.getExpiringPromotions();
    }

    public List<Object[]> getCancellationRates() {
        return reportDAO.getCancellationRateReport();
    }

    public List<Object[]> getPaymentMethodStats() {
        return reportDAO.getPaymentMethodStats();
    }
}
