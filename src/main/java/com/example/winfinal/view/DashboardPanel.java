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
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel header = new JLabel("Báo cáo phân tích");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerPanel.add(header, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(Color.WHITE);

        JButton refreshBtn = new JButton("Làm mới");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        refreshBtn.addActionListener(e -> {
            refreshBtn.setEnabled(false);
            refreshBtn.setText("Đang tải...");
            
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                List<Object[]> d0, d1, d2, d3, d4, d5;
                Exception ex;
                @Override
                protected Void doInBackground() {
                    try {
                        d0 = reportController.getMonthlyRevenue();
                        d1 = reportController.getTopCustomers();
                        d2 = reportController.getCourtUtilization();
                        d3 = reportController.getPeakHours();
                        d4 = reportController.getCancellationRates();
                        d5 = reportController.getExpiringPromotions();
                    } catch (Exception err) {
                        ex = err;
                    }
                    return null;
                }
                @Override
                protected void done() {
                    if (ex != null) {
                        JOptionPane.showMessageDialog(DashboardPanel.this, "Lỗi tải dữ liệu báo cáo: " + ex.getMessage());
                    } else if (d0 != null && d1 != null && d2 != null && d3 != null && d4 != null && d5 != null) {
                        updateTableData(0, d0);
                        updateTableData(1, d1);
                        updateTableData(2, d2);
                        updateTableData(3, d3);
                        updateTableData(4, d4);
                        updateTableData(5, d5);
                    }
                    refreshBtn.setText("Làm mới");
                    refreshBtn.setEnabled(true);
                }
            };
            worker.execute();
        });

        actions.add(refreshBtn);
        headerPanel.add(actions, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

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
        JTable table = new com.example.winfinal.view.components.ModernTable(model);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scroll.getViewport().setBackground(Color.WHITE);
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
