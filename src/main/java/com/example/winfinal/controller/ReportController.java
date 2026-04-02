package com.example.winfinal.controller;

import com.example.winfinal.service.ReportService;
import java.util.List;

public class ReportController {
    private final ReportService reportService;

    public ReportController() {
        this.reportService = new ReportService();
    }

    public List<Object[]> getMonthlyRevenue() {
        return reportService.getMonthlyRevenue();
    }

    public List<Object[]> getTopCustomers() {
        return reportService.getTopCustomers();
    }

    public List<Object[]> getCourtUtilization() {
        return reportService.getCourtUtilization();
    }

    public List<Object[]> getPeakHours() {
        return reportService.getPeakHours();
    }

    public List<Object[]> getPopularEquipment() {
        return reportService.getPopularEquipment();
    }

    public List<Object[]> getUnpaidBookings() {
        return reportService.getUnpaidBookings();
    }

    public List<Object[]> getMonthlyGrowth() {
        return reportService.getMonthlyGrowth();
    }

    public List<Object[]> getFeedbackRatings() {
        return reportService.getFeedbackRatings();
    }

    public List<Object[]> getExpiringPromotions() {
        return reportService.getExpiringPromotions();
    }

    public List<Object[]> getCancellationRates() {
        return reportService.getCancellationRates();
    }

    public List<Object[]> getPaymentMethodStats() {
        return reportService.getPaymentMethodStats();
    }
}
