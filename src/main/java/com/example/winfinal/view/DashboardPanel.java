package com.example.winfinal.view;

import com.example.winfinal.controller.ReportController;
import com.example.winfinal.utils.FormatUtils;
import com.example.winfinal.view.components.ModernButton;
import com.example.winfinal.view.components.ModernTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class DashboardPanel extends JPanel {
    private final ReportController reportController;

    // KPI Card Labels
    private JLabel lblTotalRevenue;
    private JLabel lblTotalRevenueNote;
    private JLabel lblTotalBookings;
    private JLabel lblTotalBookingsNote;
    private JLabel lblCancelRate;
    private JLabel lblCancelRateNote;
    private JLabel lblActiveCourts;
    private JLabel lblActiveCourtsNote;
    private JLabel lblExpiringPromos;
    private JLabel lblExpiringPromosNote;

    // Chart containers — Page 1
    private JPanel revenueChartContainer;
    private JPanel cancellationChartContainer;

    // Chart containers — Page 2
    private JPanel peakHoursChartContainer;
    private JPanel topCustomersChartContainer;
    private JPanel courtEfficiencyChartContainer;
    private JPanel promotionsContainer;

    // Tab pane for tables (kept for backward compatibility)
    private JTabbedPane reportTabs;

    // Color palette
    private static final Color BG_COLOR = new Color(245, 247, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(31, 41, 55);
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private static final Color BLUE = new Color(59, 130, 246);
    private static final Color GREEN = new Color(16, 185, 129);
    private static final Color ORANGE = new Color(245, 158, 11);
    private static final Color RED = new Color(239, 68, 68);
    private static final Color TEAL = new Color(20, 184, 166);
    private static final Color PURPLE = new Color(139, 92, 246);
    private static final Color INDIGO = new Color(99, 102, 241);

    // Section tab navigation
    private JPanel page1Panel;
    private JPanel page2Panel;
    private JButton btnPage1;
    private JButton btnPage2;
    private CardLayout sectionCardLayout;
    private JPanel sectionContainer;

    public DashboardPanel() {
        this.reportController = new ReportController();
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        initComponents();
        loadData();
    }

    private void initComponents() {
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(24, 28, 0, 28));

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setBackground(BG_COLOR);

        JLabel header = new JLabel("Tổng quan");
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setForeground(TEXT_PRIMARY);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleBlock.add(header);

        JLabel subtitle = new JLabel("Bảng điều khiển quản lý sân cầu lông");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleBlock.add(Box.createVerticalStrut(2));
        titleBlock.add(subtitle);

        headerPanel.add(titleBlock, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actions.setBackground(BG_COLOR);
        JButton refreshBtn = new ModernButton("Làm mới", BLUE);
        refreshBtn.addActionListener(e -> refreshDashboard(refreshBtn));
        actions.add(refreshBtn);
        headerPanel.add(actions, BorderLayout.EAST);

        // Section navigation tabs below the header
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        navBar.setBackground(BG_COLOR);
        navBar.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        btnPage1 = createNavTab("Tổng quan & Doanh thu", true);
        btnPage2 = createNavTab("Phân tích chi tiết", false);

        btnPage1.addActionListener(e -> switchToPage("PAGE1", btnPage1, btnPage2));
        btnPage2.addActionListener(e -> switchToPage("PAGE2", btnPage2, btnPage1));

        navBar.add(btnPage1);
        navBar.add(Box.createHorizontalStrut(4));
        navBar.add(btnPage2);

        JPanel headerWrapper = new JPanel();
        headerWrapper.setLayout(new BoxLayout(headerWrapper, BoxLayout.Y_AXIS));
        headerWrapper.setBackground(BG_COLOR);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        navBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerWrapper.add(headerPanel);

        // Add a wrapper for navBar so it's properly padded
        JPanel navBarWrapper = new JPanel(new BorderLayout());
        navBarWrapper.setBackground(BG_COLOR);
        navBarWrapper.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));
        navBarWrapper.add(navBar, BorderLayout.WEST);
        navBarWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerWrapper.add(navBarWrapper);

        // Separator line
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(220, 222, 226));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        headerWrapper.add(sep);

        add(headerWrapper, BorderLayout.NORTH);

        // ===========================
        // Section card layout
        // ===========================
        sectionCardLayout = new CardLayout();
        sectionContainer = new JPanel(sectionCardLayout);
        sectionContainer.setBackground(BG_COLOR);

        // PAGE 1: KPI + Revenue + Cancellation
        page1Panel = buildPage1();
        sectionContainer.add(page1Panel, "PAGE1");

        // PAGE 2: Peak Hours + Customers + Court Efficiency + Promotions + Tables
        page2Panel = buildPage2();
        sectionContainer.add(page2Panel, "PAGE2");

        add(sectionContainer, BorderLayout.CENTER);
    }

    // ===========================
    // NAV TAB BUTTON
    // ===========================
    private JButton createNavTab(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 14));
        btn.setForeground(active ? BLUE : TEXT_SECONDARY);
        btn.setBackground(BG_COLOR);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, active ? 3 : 0, 0, active ? BLUE : BG_COLOR),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!btn.getFont().isBold()) {
                    btn.setForeground(TEXT_PRIMARY);
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!btn.getFont().isBold()) {
                    btn.setForeground(TEXT_SECONDARY);
                }
            }
        });

        return btn;
    }

    private void switchToPage(String pageName, JButton activeBtn, JButton inactiveBtn) {
        sectionCardLayout.show(sectionContainer, pageName);

        activeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        activeBtn.setForeground(BLUE);
        activeBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, BLUE),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));

        inactiveBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inactiveBtn.setForeground(TEXT_SECONDARY);
        inactiveBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 0, BG_COLOR),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
    }

    // ===========================
    // PAGE 1: KPI + Revenue + Cancellation Donut
    // ===========================
    private JPanel buildPage1() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_COLOR);
        content.setBorder(BorderFactory.createEmptyBorder(20, 28, 28, 28));

        // KPI Cards Row
        JPanel kpiRow = createKPICards();
        kpiRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(kpiRow);
        content.add(Box.createVerticalStrut(20));

        // Monthly Revenue chart — FULL WIDTH, generous height
        revenueChartContainer = createChartCard("Doanh thu hàng tháng");
        revenueChartContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        revenueChartContainer.setPreferredSize(new Dimension(0, 420));
        revenueChartContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 420));
        content.add(revenueChartContainer);
        content.add(Box.createVerticalStrut(16));

        // Cancellation donut — FULL WIDTH
        cancellationChartContainer = createChartCard("Tỷ lệ trạng thái đặt sân");
        cancellationChartContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        cancellationChartContainer.setPreferredSize(new Dimension(0, 380));
        cancellationChartContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 380));
        content.add(cancellationChartContainer);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(BG_COLOR);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_COLOR);
        wrapper.add(scrollPane, BorderLayout.CENTER);
        return wrapper;
    }

    // ===========================
    // PAGE 2: Peak Hours + Customers + Court Efficiency + Promos + Tables
    // ===========================
    private JPanel buildPage2() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_COLOR);
        content.setBorder(BorderFactory.createEmptyBorder(20, 28, 28, 28));

        // Peak Hours chart — FULL WIDTH
        peakHoursChartContainer = createChartCard("Giờ cao điểm");
        peakHoursChartContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        peakHoursChartContainer.setPreferredSize(new Dimension(0, 380));
        peakHoursChartContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 380));
        content.add(peakHoursChartContainer);
        content.add(Box.createVerticalStrut(16));

        // Row: Top Customers + Court Efficiency — side by side with more height
        JPanel row2 = new JPanel(new GridLayout(1, 2, 16, 0));
        row2.setBackground(BG_COLOR);
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);
        row2.setPreferredSize(new Dimension(0, 400));
        row2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        topCustomersChartContainer = createChartCard("Top khách hàng");
        courtEfficiencyChartContainer = createChartCard("Hiệu suất sân");
        row2.add(topCustomersChartContainer);
        row2.add(courtEfficiencyChartContainer);
        content.add(row2);
        content.add(Box.createVerticalStrut(16));

        // Promotions expiring — FULL WIDTH
        promotionsContainer = createChartCard("Khuyến mãi sắp hết hạn");
        promotionsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        promotionsContainer.setPreferredSize(new Dimension(0, 360));
        promotionsContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 360));
        content.add(promotionsContainer);
        content.add(Box.createVerticalStrut(20));

        // Detailed Data Tabs — kept from original
        JPanel tabSection = new JPanel(new BorderLayout());
        tabSection.setBackground(CARD_BG);
        tabSection.setBorder(createRoundedBorder());
        tabSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabSection.setPreferredSize(new Dimension(0, 400));
        tabSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        JLabel tabTitle = new JLabel("  Dữ liệu chi tiết");
        tabTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabTitle.setForeground(TEXT_PRIMARY);
        tabTitle.setBorder(BorderFactory.createEmptyBorder(14, 12, 8, 12));
        tabSection.add(tabTitle, BorderLayout.NORTH);

        reportTabs = new JTabbedPane();
        reportTabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reportTabs.setBackground(CARD_BG);

        addReportTab("Doanh thu hàng tháng", new String[]{"Tháng", "Doanh thu sân (VND)", "Phụ kiện (VND)", "Dịch vụ (VND)", "Tổng cộng (VND)"});
        addReportTab("Top khách hàng", new String[]{"ID", "Khách hàng", "Hạng TV", "Lượt đặt", "Tổng chi tiêu (VND)"});
        addReportTab("Hiệu suất sân", new String[]{"Sân", "Loại", "Giờ đặt", "Giờ mở", "% Hiệu suất"});
        addReportTab("Giờ cao điểm", new String[]{"Giờ", "Lượt đặt", "Giai đoạn"});
        addReportTab("Tỷ lệ hủy sân", new String[]{"Khách hàng", "Số lần hủy", "Tổng lượt", "% Tỷ lệ hủy"});
        addReportTab("Khuyến mãi sắp hết hạn", new String[]{"Khuyến mãi", "% Giảm", "Bắt đầu", "Kết thúc", "Ngày còn lại"});

        tabSection.add(reportTabs, BorderLayout.CENTER);
        content.add(tabSection);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(BG_COLOR);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_COLOR);
        wrapper.add(scrollPane, BorderLayout.CENTER);
        return wrapper;
    }

    // ===========================
    // KPI CARDS
    // ===========================
    private JPanel createKPICards() {
        JPanel kpiPanel = new JPanel(new GridLayout(1, 5, 14, 0));
        kpiPanel.setBackground(BG_COLOR);
        kpiPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        kpiPanel.setPreferredSize(new Dimension(0, 110));

        lblTotalRevenue = new JLabel("0");
        lblTotalRevenueNote = new JLabel("Tổng doanh thu");
        kpiPanel.add(createKPICard("Tổng doanh thu", lblTotalRevenue, lblTotalRevenueNote, BLUE));

        lblTotalBookings = new JLabel("0");
        lblTotalBookingsNote = new JLabel("lượt");
        kpiPanel.add(createKPICard("Lượt đặt sân", lblTotalBookings, lblTotalBookingsNote, GREEN));

        lblCancelRate = new JLabel("0%");
        lblCancelRateNote = new JLabel("tỷ lệ hủy");
        kpiPanel.add(createKPICard("Tỷ lệ hủy", lblCancelRate, lblCancelRateNote, RED));

        lblActiveCourts = new JLabel("0");
        lblActiveCourtsNote = new JLabel("sân");
        kpiPanel.add(createKPICard("Sân hoạt động", lblActiveCourts, lblActiveCourtsNote, TEAL));

        lblExpiringPromos = new JLabel("0");
        lblExpiringPromosNote = new JLabel("khuyến mãi");
        kpiPanel.add(createKPICard("KM sắp hết hạn", lblExpiringPromos, lblExpiringPromosNote, ORANGE));

        return kpiPanel;
    }

    private JPanel createKPICard(String title, JLabel valueLabel, JLabel noteLabel, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                createRoundedBorder(),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)
        ));

        // Accent indicator bar
        JPanel accentBar = new JPanel();
        accentBar.setMaximumSize(new Dimension(40, 4));
        accentBar.setPreferredSize(new Dimension(40, 4));
        accentBar.setBackground(accentColor);
        accentBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(accentBar);
        card.add(Box.createVerticalStrut(10));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLbl.setForeground(TEXT_SECONDARY);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLbl);
        card.add(Box.createVerticalStrut(6));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(TEXT_PRIMARY);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(4));

        noteLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        noteLabel.setForeground(accentColor);
        noteLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(noteLabel);

        return card;
    }

    // ===========================
    // CHART CARD CONTAINER
    // ===========================
    private JPanel createChartCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(createRoundedBorder());

        JLabel titleLbl = new JLabel("  " + title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLbl.setForeground(TEXT_PRIMARY);
        titleLbl.setBorder(BorderFactory.createEmptyBorder(14, 12, 4, 12));
        card.add(titleLbl, BorderLayout.NORTH);

        JPanel placeholder = new JPanel(new BorderLayout());
        placeholder.setBackground(CARD_BG);
        JLabel loadLbl = new JLabel("Đang tải dữ liệu...", SwingConstants.CENTER);
        loadLbl.setForeground(TEXT_SECONDARY);
        loadLbl.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        placeholder.add(loadLbl, BorderLayout.CENTER);
        card.add(placeholder, BorderLayout.CENTER);

        return card;
    }

    // ===========================
    // DATA LOADING
    // ===========================
    private void loadData() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            Object[] revenueSummary, bookingSummary, courtsSummary, promosSummary;
            List<Object[]> monthlyRevenue, topCustomers, courtUtil, peakHours, cancelRates, expiringPromos;
            Exception ex;

            @Override
            protected Void doInBackground() {
                try {
                    revenueSummary = reportController.getTotalRevenueSummary();
                    bookingSummary = reportController.getTotalBookingsSummary();
                    courtsSummary = reportController.getActiveCourtsSummary();
                    promosSummary = reportController.getExpiringPromotionsSummary();

                    monthlyRevenue = reportController.getMonthlyRevenue();
                    topCustomers = reportController.getTopCustomers();
                    courtUtil = reportController.getCourtUtilization();
                    peakHours = reportController.getPeakHours();
                    cancelRates = reportController.getCancellationRates();
                    expiringPromos = reportController.getExpiringPromotions();
                } catch (Exception err) {
                    ex = err;
                }
                return null;
            }

            @Override
            protected void done() {
                if (ex != null) {
                    JOptionPane.showMessageDialog(DashboardPanel.this,
                            "Lỗi tải dữ liệu báo cáo: " + ex.getMessage());
                    return;
                }

                updateKPICards(revenueSummary, bookingSummary, courtsSummary, promosSummary);

                updateRevenueChart(monthlyRevenue);
                updateCancellationChart(bookingSummary);
                updatePeakHoursChart(peakHours);
                updateTopCustomersChart(topCustomers);
                updateCourtEfficiencyChart(courtUtil);
                updatePromotionsSection(expiringPromos);

                if (monthlyRevenue != null) updateTableData(0, monthlyRevenue);
                if (topCustomers != null) updateTableData(1, topCustomers);
                if (courtUtil != null) updateTableData(2, courtUtil);
                if (peakHours != null) updateTableData(3, peakHours);
                if (cancelRates != null) updateTableData(4, cancelRates);
                if (expiringPromos != null) updateTableData(5, expiringPromos);
            }
        };
        worker.execute();
    }

    private void refreshDashboard(JButton refreshBtn) {
        refreshBtn.setEnabled(false);
        refreshBtn.setText("Đang tải...");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            Object[] revenueSummary, bookingSummary, courtsSummary, promosSummary;
            List<Object[]> d0, d1, d2, d3, d4, d5;
            Exception ex;

            @Override
            protected Void doInBackground() {
                try {
                    revenueSummary = reportController.getTotalRevenueSummary();
                    bookingSummary = reportController.getTotalBookingsSummary();
                    courtsSummary = reportController.getActiveCourtsSummary();
                    promosSummary = reportController.getExpiringPromotionsSummary();

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
                    JOptionPane.showMessageDialog(DashboardPanel.this,
                            "Lỗi tải dữ liệu báo cáo: " + ex.getMessage());
                } else {
                    updateKPICards(revenueSummary, bookingSummary, courtsSummary, promosSummary);
                    updateRevenueChart(d0);
                    updateCancellationChart(bookingSummary);
                    updatePeakHoursChart(d3);
                    updateTopCustomersChart(d1);
                    updateCourtEfficiencyChart(d2);
                    updatePromotionsSection(d5);

                    if (d0 != null) updateTableData(0, d0);
                    if (d1 != null) updateTableData(1, d1);
                    if (d2 != null) updateTableData(2, d2);
                    if (d3 != null) updateTableData(3, d3);
                    if (d4 != null) updateTableData(4, d4);
                    if (d5 != null) updateTableData(5, d5);
                }
                refreshBtn.setText("Làm mới");
                refreshBtn.setEnabled(true);
            }
        };
        worker.execute();
    }

    // ===========================
    // KPI CARD UPDATES
    // ===========================
    private void updateKPICards(Object[] revenue, Object[] bookings, Object[] courts, Object[] promos) {
        if (revenue != null) {
            double courtRev = toDouble(revenue[0]);
            double equipRev = toDouble(revenue[1]);
            double serviceRev = toDouble(revenue[2]);
            double total = courtRev + equipRev + serviceRev;
            lblTotalRevenue.setText(FormatUtils.formatCurrency(total) + " ₫");
            lblTotalRevenueNote.setText("Sân: " + FormatUtils.formatCurrency(courtRev) + " | Phụ kiện: " + FormatUtils.formatCurrency(equipRev));
        }

        if (bookings != null) {
            long totalBookings = toLong(bookings[0]);
            long confirmed = toLong(bookings[1]);
            long pending = toLong(bookings[2]);
            long cancelled = toLong(bookings[3]);
            lblTotalBookings.setText(String.valueOf(totalBookings));
            lblTotalBookingsNote.setText("Xác nhận: " + confirmed + " | Chờ: " + pending + " | Hủy: " + cancelled);

            if (totalBookings > 0) {
                double cancelRate = (cancelled * 100.0) / totalBookings;
                lblCancelRate.setText(String.format("%.1f%%", cancelRate));
                lblCancelRateNote.setText(cancelled + " / " + totalBookings + " lượt đặt");
            }
        }

        if (courts != null) {
            long totalCourts = toLong(courts[0]);
            long activeCourts = toLong(courts[1]);
            lblActiveCourts.setText(activeCourts + " / " + totalCourts);
            lblActiveCourtsNote.setText("sân đang hoạt động");
        }

        if (promos != null) {
            long expiringCount = toLong(promos[0]);
            long urgentCount = toLong(promos[1]);
            lblExpiringPromos.setText(String.valueOf(expiringCount));
            if (urgentCount > 0) {
                lblExpiringPromosNote.setText(urgentCount + " khuyến mãi sắp hết hạn (≤3 ngày)");
                lblExpiringPromosNote.setForeground(RED);
            } else {
                lblExpiringPromosNote.setText("trong 30 ngày tới");
            }
        }
    }

    // ===========================
    // CHART BUILDERS
    // ===========================
    private void updateRevenueChart(List<Object[]> data) {
        if (data == null || data.isEmpty()) {
            setEmptyState(revenueChartContainer, "Chưa có dữ liệu doanh thu");
            return;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = data.size() - 1; i >= 0; i--) {
            Object[] row = data.get(i);
            String month = String.valueOf(row[0]);
            if (month.length() > 5) month = month.substring(5);
            dataset.addValue(toDouble(row[1]), "Doanh thu sân", month);
            dataset.addValue(toDouble(row[2]), "Phụ kiện", month);
            dataset.addValue(toDouble(row[3]), "Dịch vụ", month);
        }

        JFreeChart chart = ChartFactory.createStackedBarChart(
                null, null, "VND", dataset,
                PlotOrientation.VERTICAL, true, true, false);
        styleChart(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, BLUE);
        renderer.setSeriesPaint(1, TEAL);
        renderer.setSeriesPaint(2, PURPLE);
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setMaximumBarWidth(0.06);
        renderer.setShadowVisible(false);

        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.STANDARD);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setNumberFormatOverride(new DecimalFormat("#,###"));

        replaceChartContent(revenueChartContainer, chart);
    }

    private void updatePeakHoursChart(List<Object[]> data) {
        if (data == null || data.isEmpty()) {
            setEmptyState(peakHoursChartContainer, "Chưa có dữ liệu giờ cao điểm");
            return;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        data.sort((a, b) -> {
            int hourA = ((Number) a[0]).intValue();
            int hourB = ((Number) b[0]).intValue();
            return Integer.compare(hourA, hourB);
        });

        for (Object[] row : data) {
            int hour = ((Number) row[0]).intValue();
            double count = toDouble(row[1]);
            dataset.addValue(count, "Lượt đặt", hour + "h");
        }

        JFreeChart chart = ChartFactory.createBarChart(
                null, "Giờ", "Lượt đặt", dataset,
                PlotOrientation.VERTICAL, false, true, false);
        styleChart(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setMaximumBarWidth(0.05);
        renderer.setShadowVisible(false);
        renderer.setSeriesPaint(0, INDIGO);

        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", NumberFormat.getIntegerInstance()));
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelFont(new Font("Segoe UI", Font.BOLD, 11));

        replaceChartContent(peakHoursChartContainer, chart);
    }

    private void updateTopCustomersChart(List<Object[]> data) {
        if (data == null || data.isEmpty()) {
            setEmptyState(topCustomersChartContainer, "Chưa có dữ liệu khách hàng");
            return;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int limit = Math.min(data.size(), 5);
        for (int i = limit - 1; i >= 0; i--) {
            Object[] row = data.get(i);
            String name = String.valueOf(row[1]);
            if (name.length() > 18) name = name.substring(0, 18) + "...";
            dataset.addValue(toDouble(row[4]), "Chi tiêu", name);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                null, null, "VND", dataset,
                PlotOrientation.HORIZONTAL, false, true, false);
        styleChart(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setSeriesPaint(0, GREEN);
        renderer.setShadowVisible(false);
        renderer.setMaximumBarWidth(0.15);

        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("#,###")));
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelFont(new Font("Segoe UI", Font.PLAIN, 11));

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setNumberFormatOverride(new DecimalFormat("#,###"));

        replaceChartContent(topCustomersChartContainer, chart);
    }

    private void updateCourtEfficiencyChart(List<Object[]> data) {
        if (data == null || data.isEmpty()) {
            setEmptyState(courtEfficiencyChartContainer, "Chưa có dữ liệu hiệu suất sân");
            return;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Object[] row : data) {
            String courtName = String.valueOf(row[0]);
            double efficiency = toDouble(row[4]);
            dataset.addValue(efficiency, "Hiệu suất", courtName);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                null, null, "% Hiệu suất", dataset,
                PlotOrientation.HORIZONTAL, false, true, false);
        styleChart(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                double val = dataset.getValue(row, column).doubleValue();
                if (val >= 70) return GREEN;
                else if (val >= 40) return ORANGE;
                else return RED;
            }
        };
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setMaximumBarWidth(0.15);
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}%", new DecimalFormat("#.#")));
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelFont(new Font("Segoe UI", Font.BOLD, 11));
        plot.setRenderer(renderer);

        replaceChartContent(courtEfficiencyChartContainer, chart);
    }

    private void updateCancellationChart(Object[] bookingSummary) {
        if (bookingSummary == null) {
            setEmptyState(cancellationChartContainer, "Chưa có dữ liệu");
            return;
        }

        long confirmed = toLong(bookingSummary[1]);
        long pending = toLong(bookingSummary[2]);
        long cancelled = toLong(bookingSummary[3]);

        if (confirmed + pending + cancelled == 0) {
            setEmptyState(cancellationChartContainer, "Chưa có dữ liệu đặt sân");
            return;
        }

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("Đã xác nhận (" + confirmed + ")", confirmed);
        dataset.setValue("Chờ xác nhận (" + pending + ")", pending);
        dataset.setValue("Đã hủy (" + cancelled + ")", cancelled);

        JFreeChart chart = ChartFactory.createRingChart(
                null, dataset, true, true, false);
        styleChart(chart);

        RingPlot plot = (RingPlot) chart.getPlot();
        plot.setSectionPaint("Đã xác nhận (" + confirmed + ")", GREEN);
        plot.setSectionPaint("Chờ xác nhận (" + pending + ")", ORANGE);
        plot.setSectionPaint("Đã hủy (" + cancelled + ")", RED);
        plot.setSectionDepth(0.35);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}\n{2}"));
        plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 200));
        plot.setLabelShadowPaint(null);
        plot.setShadowPaint(null);
        plot.setOutlineVisible(false);
        plot.setBackgroundPaint(CARD_BG);
        plot.setSimpleLabels(true);
        plot.setInsets(new RectangleInsets(10, 40, 10, 40));

        replaceChartContent(cancellationChartContainer, chart);
    }

    private void updatePromotionsSection(List<Object[]> data) {
        if (data == null || data.isEmpty()) {
            setEmptyState(promotionsContainer, "Không có khuyến mãi sắp hết hạn");
            return;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int limit = Math.min(data.size(), 10);
        for (int i = limit - 1; i >= 0; i--) {
            Object[] row = data.get(i);
            String name = String.valueOf(row[0]);
            if (name.length() > 22) name = name.substring(0, 22) + "...";
            double daysLeft = toDouble(row[4]);
            dataset.addValue(daysLeft, "Ngày còn lại", name);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                null, null, "Ngày còn lại", dataset,
                PlotOrientation.HORIZONTAL, false, true, false);
        styleChart(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                double val = dataset.getValue(row, column).doubleValue();
                if (val <= 3) return RED;
                else if (val <= 7) return ORANGE;
                else return BLUE;
            }
        };
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setMaximumBarWidth(0.10);
        renderer.setDefaultItemLabelGenerator(
                new StandardCategoryItemLabelGenerator("{2} ngày", new DecimalFormat("#")));
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelFont(new Font("Segoe UI", Font.BOLD, 11));
        plot.setRenderer(renderer);

        replaceChartContent(promotionsContainer, chart);
    }

    // ===========================
    // CHART HELPERS
    // ===========================
    private void styleChart(JFreeChart chart) {
        chart.setBackgroundPaint(CARD_BG);
        chart.setBorderVisible(false);
        chart.setPadding(new RectangleInsets(4, 4, 4, 4));

        if (chart.getLegend() != null) {
            chart.getLegend().setBackgroundPaint(CARD_BG);
            chart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 12));
        }

        if (chart.getPlot() instanceof CategoryPlot) {
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            plot.setBackgroundPaint(CARD_BG);
            plot.setOutlineVisible(false);
            plot.setRangeGridlinePaint(new Color(230, 230, 230));
            plot.setDomainGridlinesVisible(false);
            plot.setInsets(new RectangleInsets(10, 10, 10, 20));

            CategoryAxis domainAxis = plot.getDomainAxis();
            domainAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
            domainAxis.setLabelFont(new Font("Segoe UI", Font.PLAIN, 12));

            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));
            rangeAxis.setLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
        }
    }

    private void replaceChartContent(JPanel container, JFreeChart chart) {
        BorderLayout layout = (BorderLayout) container.getLayout();
        Component center = layout.getLayoutComponent(BorderLayout.CENTER);
        if (center != null) container.remove(center);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(CARD_BG);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        chartPanel.setMouseWheelEnabled(false);
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);
        chartPanel.setPopupMenu(null);
        chartPanel.setMinimumDrawWidth(300);
        chartPanel.setMinimumDrawHeight(200);
        chartPanel.setMaximumDrawWidth(4000);
        chartPanel.setMaximumDrawHeight(2000);
        container.add(chartPanel, BorderLayout.CENTER);
        container.revalidate();
        container.repaint();
    }

    private void setEmptyState(JPanel container, String message) {
        BorderLayout layout = (BorderLayout) container.getLayout();
        Component center = layout.getLayoutComponent(BorderLayout.CENTER);
        if (center != null) container.remove(center);

        JPanel emptyPanel = new JPanel(new BorderLayout());
        emptyPanel.setBackground(CARD_BG);
        JLabel emptyLbl = new JLabel(message, SwingConstants.CENTER);
        emptyLbl.setForeground(TEXT_SECONDARY);
        emptyLbl.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        emptyPanel.add(emptyLbl, BorderLayout.CENTER);
        container.add(emptyPanel, BorderLayout.CENTER);
        container.revalidate();
        container.repaint();
    }

    // ===========================
    // TABLE METHODS (backward compatibility)
    // ===========================
    private void addReportTab(String title, String[] columns) {
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new ModernTable(model);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scroll.getViewport().setBackground(Color.WHITE);
        reportTabs.addTab(title, scroll);
    }

    private void updateTableData(int tabIndex, List<Object[]> data) {
        JScrollPane scroll = (JScrollPane) reportTabs.getComponentAt(tabIndex);
        JTable table = (JTable) scroll.getViewport().getView();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Object[] row : data) {
            Object[] formattedRow = new Object[row.length];
            System.arraycopy(row, 0, formattedRow, 0, row.length);

            if (tabIndex == 0) {
                if (formattedRow.length > 1) formattedRow[1] = FormatUtils.formatCurrency(row[1]);
                if (formattedRow.length > 2) formattedRow[2] = FormatUtils.formatCurrency(row[2]);
                if (formattedRow.length > 3) formattedRow[3] = FormatUtils.formatCurrency(row[3]);
                if (formattedRow.length > 4) formattedRow[4] = FormatUtils.formatCurrency(row[4]);
            } else if (tabIndex == 1) {
                if (formattedRow.length > 4) formattedRow[4] = FormatUtils.formatCurrency(row[4]);
            } else if (tabIndex == 2) {
                if (formattedRow.length > 4) formattedRow[4] = FormatUtils.formatPercentage(row[4]);
            } else if (tabIndex == 4) {
                if (formattedRow.length > 3) formattedRow[3] = FormatUtils.formatPercentage(row[3]);
            } else if (tabIndex == 5) {
                if (formattedRow.length > 1) formattedRow[1] = FormatUtils.formatPercentage(row[1]);
            }

            model.addRow(formattedRow);
        }
    }

    // ===========================
    // UTILITIES
    // ===========================
    private javax.swing.border.Border createRoundedBorder() {
        return BorderFactory.createCompoundBorder(
                new RoundedBorder(10, new Color(230, 232, 236)),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        );
    }

    private double toDouble(Object val) {
        if (val == null) return 0;
        if (val instanceof Number) return ((Number) val).doubleValue();
        try { return Double.parseDouble(val.toString()); } catch (Exception e) { return 0; }
    }

    private long toLong(Object val) {
        if (val == null) return 0;
        if (val instanceof Number) return ((Number) val).longValue();
        try { return Long.parseLong(val.toString()); } catch (Exception e) { return 0; }
    }

    /**
     * Custom rounded border
     */
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, 2, 2);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = 2;
            return insets;
        }
    }
}
