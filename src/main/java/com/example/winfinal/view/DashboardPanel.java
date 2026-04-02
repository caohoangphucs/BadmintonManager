package com.example.winfinal.view;

import com.example.winfinal.controller.ReportController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {
    private final ReportController reportController;
    private JTabbedPane reportTabs;

    public DashboardPanel() {
        this.reportController = new ReportController();
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setBackground(Color.WHITE);

        initComponents();
        loadData();
    }

    private void initComponents() {
        // Header
        JLabel header = new JLabel("Báo cáo phân tích");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        add(header, BorderLayout.NORTH);

        // Sidebar or TabbedPane for Reports
        reportTabs = new JTabbedPane();
        reportTabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        addReportTab("Doanh thu hàng tháng", new String[]{"Tháng", "Doanh thu sân", "Phụ kiện", "Dịch vụ", "Tổng cộng"});
        addReportTab("Top khách hàng", new String[]{"ID", "Khách hàng", "Hạng TV", "Lượt đặt", "Tổng chi tiêu"});
        addReportTab("Hiệu suất sân", new String[]{"Sân", "Loại", "Giờ đặt", "Giờ mở", "% Hiệu suất"});
        addReportTab("Giờ cao điểm", new String[]{"Giờ", "Lượt đặt", "Giai đoạn"});
        addReportTab("Tỷ lệ hủy sân", new String[]{"Khách hàng", "Số lần hủy", "Tổng lượt", "% Tỷ lệ hủy"});
        addReportTab("Khuyến mãi sắp hết hạn", new String[]{"Khuyến mãi", "% Giảm", "Bắt đầu", "Kết thúc", "Ngày còn lại"});

        add(reportTabs, BorderLayout.CENTER);
    }

    private void addReportTab(String title, String[] columns) {
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        reportTabs.addTab(title, scroll);
    }

    private void loadData() {
        try {
            updateTableData(0, reportController.getMonthlyRevenue());
            updateTableData(1, reportController.getTopCustomers());
            updateTableData(2, reportController.getCourtUtilization());
            updateTableData(3, reportController.getPeakHours());
            updateTableData(4, reportController.getCancellationRates());
            updateTableData(5, reportController.getExpiringPromotions());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu báo cáo: " + e.getMessage());
        }
    }

    private void updateTableData(int tabIndex, List<Object[]> data) {
        JScrollPane scroll = (JScrollPane) reportTabs.getComponentAt(tabIndex);
        JTable table = (JTable) scroll.getViewport().getView();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
}
