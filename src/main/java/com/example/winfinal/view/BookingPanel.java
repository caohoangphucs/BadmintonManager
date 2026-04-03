package com.example.winfinal.view;

import com.example.winfinal.controller.BookingController;
import com.example.winfinal.controller.CustomerController;
import com.example.winfinal.controller.CourtController;
import com.example.winfinal.dto.BookingDTO;
import com.example.winfinal.dto.CustomerDTO;
import com.example.winfinal.dto.CourtDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;

public class BookingPanel extends JPanel {
    private final BookingController bookingController;
    private final CustomerController customerController;
    private final CourtController courtController;
    private JTable table;
    private DefaultTableModel model;

    private JComboBox<String> courtCombo;
    private JComboBox<String> statusCombo;

    public BookingPanel() {
        this.bookingController = new BookingController();
        this.customerController = new CustomerController();
        this.courtController = new CourtController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        initComponents();
        loadData();
    }

    private void initComponents() {
        JPanel northWrapper = new JPanel(new BorderLayout(0, 10));
        northWrapper.setBackground(Color.WHITE);

        // ── Title block wrapper (title + subtitle + separator) ────────────────
        JPanel headerWrapper = new JPanel();
        headerWrapper.setLayout(new BoxLayout(headerWrapper, BoxLayout.Y_AXIS));
        headerWrapper.setBackground(Color.WHITE);

        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title block: title + subtitle stacked
        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setBackground(Color.WHITE);

        JLabel title = new JLabel("Quản lý lượt đặt sân");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(31, 41, 55));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleBlock.add(title);

        JLabel subtitle = new JLabel("Quản lý danh sách đặt sân và trạng thái booking");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(107, 114, 128));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleBlock.add(Box.createVerticalStrut(2));
        titleBlock.add(subtitle);

        bar.add(titleBlock, BorderLayout.WEST);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btns.setBackground(Color.WHITE);
        JButton bookBtn = createButton("Đặt sân", new Color(16, 185, 129));
        JButton cancelBtn = createButton("Hủy đặt sân", new Color(239, 68, 68));
        JButton refreshBtn = createButton("Làm mới", new Color(59, 130, 246));

        bookBtn.addActionListener(e -> showBookCourtDialog());

        cancelBtn.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn lượt đặt sân cần hủy.");
            } else {
                if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn hủy lượt đặt sân này?", "Xác nhận",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    try {
                        bookingController.cancelBooking((Integer) table.getValueAt(r, 0));
                        JOptionPane.showMessageDialog(this, "Hủy đặt sân thành công.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Thông báo", JOptionPane.WARNING_MESSAGE);
                    } finally {
                        loadData();
                    }
                }
            }
        });
        refreshBtn.addActionListener(e -> {
            courtCombo.setSelectedIndex(0);
            statusCombo.setSelectedIndex(0);
            loadData();
        });

        btns.add(bookBtn);
        btns.add(cancelBtn);
        btns.add(refreshBtn);
        bar.add(btns, BorderLayout.EAST);

        headerWrapper.add(bar);
        headerWrapper.add(Box.createVerticalStrut(8));

        // Subtle separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(220, 222, 226));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        headerWrapper.add(sep);

        northWrapper.add(headerWrapper, BorderLayout.NORTH);

        // ── Filter Panel ─────────────────────────────────────────────────────
        JPanel filterCard = new JPanel(new GridBagLayout());
        filterCard.setBackground(new Color(248, 249, 252));
        filterCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(0, 0, 0, 10);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        gc.gridy = 0;

        // --- Sân label ---
        gc.gridx = 0;
        gc.weightx = 0;
        filterCard.add(makeFilterLabel("Sân:"), gc);

        // --- Sân combo ---
        courtCombo = new JComboBox<>(new String[] { "Tất cả" });
        styleCombo(courtCombo, 160);
        gc.gridx = 1;
        filterCard.add(courtCombo, gc);

        // --- separator gap ---
        gc.gridx = 2;
        gc.insets = new Insets(0, 6, 0, 10);
        filterCard.add(Box.createHorizontalStrut(6), gc);
        gc.insets = new Insets(0, 0, 0, 10);

        // --- Trạng thái label ---
        gc.gridx = 3;
        gc.weightx = 0;
        filterCard.add(makeFilterLabel("Trạng thái:"), gc);

        // --- Trạng thái combo ---
        statusCombo = new JComboBox<>(new String[] { "Tất cả", "Đã xác nhận", "Chờ xác nhận", "Đã hủy" });
        styleCombo(statusCombo, 170);
        gc.gridx = 4;
        filterCard.add(statusCombo, gc);

        // --- spacer pushes buttons to the right ---
        gc.gridx = 5;
        gc.weightx = 1.0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        filterCard.add(Box.createHorizontalGlue(), gc);
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0;

        // --- Lọc (primary) ---
        JButton filterBtn = createButton("Lọc", new Color(55, 93, 169));
        filterBtn.setPreferredSize(new Dimension(100, 34));
        gc.gridx = 6;
        gc.insets = new Insets(0, 0, 0, 8);
        filterCard.add(filterBtn, gc);

        // --- Xóa lọc (ghost / secondary) ---
        JButton clearBtn = new GhostButton("Xóa lọc");
        gc.gridx = 7;
        gc.insets = new Insets(0, 0, 0, 0);
        filterCard.add(clearBtn, gc);

        filterBtn.addActionListener(e -> loadData());
        clearBtn.addActionListener(e -> {
            courtCombo.setSelectedIndex(0);
            statusCombo.setSelectedIndex(0);
            loadData();
        });

        northWrapper.add(filterCard, BorderLayout.SOUTH);
        add(northWrapper, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] { "ID", "Khách hàng", "Sân", "Ngày đặt", "Bắt đầu", "Kết thúc",
                "Giá tiền (VND)", "Trạng thái" }, 0);
        table = new com.example.winfinal.view.components.ModernTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        model.setRowCount(0);

        String selectedCourt = courtCombo != null ? (String) courtCombo.getSelectedItem() : "Tất cả";
        String selectedStatus = statusCombo != null ? (String) statusCombo.getSelectedItem() : "Tất cả";

        java.util.List<BookingDTO> data = bookingController.getAll();

        // Populate courtCombo with unique court names if empty
        if (courtCombo != null && courtCombo.getItemCount() == 1) {
            java.util.Set<String> courts = new java.util.HashSet<>();
            data.forEach(b -> courts.add(b.getCourtName()));
            courts.stream().sorted().forEach(courtCombo::addItem);
        }

        data.stream()
                .filter(b -> "Tất cả".equals(selectedCourt) || b.getCourtName().equals(selectedCourt))
                .filter(b -> {
                    if ("Tất cả".equals(selectedStatus))
                        return true;
                    String st = b.getStatus();
                    if ("Đã xác nhận".equals(selectedStatus))
                        return "Confirmed".equals(st);
                    if ("Chờ xác nhận".equals(selectedStatus))
                        return "Pending".equals(st);
                    if ("Đã hủy".equals(selectedStatus))
                        return "Cancelled".equals(st);
                    return true;
                })
                .forEach(b -> {
                    String priceStr = com.example.winfinal.utils.FormatUtils.formatCurrency(b.getTotalPrice());
                    String statusHTML = b.getStatus();
                    if ("Confirmed".equals(statusHTML)) {
                        statusHTML = "<html><font color='#27ae60'><b>Đã xác nhận</b></font></html>";
                    } else if ("Pending".equals(statusHTML)) {
                        statusHTML = "<html><font color='#f39c12'><b>Chờ xác nhận</b></font></html>";
                    } else if ("Cancelled".equals(statusHTML)) {
                        statusHTML = "<html><font color='#c0392b'><b>Đã hủy</b></font></html>";
                    }
                    model.addRow(new Object[] {
                            b.getBookingId(), b.getCustomerFullName(), b.getCourtName(), b.getBookingDate(),
                            b.getStartTime(), b.getEndTime(), priceStr, statusHTML
                    });
                });
    }

    private JButton createButton(String text, Color color) {
        return new com.example.winfinal.view.components.ModernButton(text, color);
    }

    // ── Filter label helper ───────────────────────────────────────────────────
    private JLabel makeFilterLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(71, 85, 105));
        return lbl;
    }

    // ── Combo-box styling helper ──────────────────────────────────────────────
    private void styleCombo(JComboBox<String> combo, int width) {
        combo.setPreferredSize(new Dimension(width, 34));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);
        combo.setForeground(new Color(30, 41, 59));
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                BorderFactory.createEmptyBorder(2, 6, 2, 4)));
    }

    // ── Ghost (outline) button for secondary filter action ────────────────────
    private static class GhostButton extends JButton {
        private boolean hovered = false;

        GhostButton(String text) {
            super(text);
            setPreferredSize(new Dimension(100, 34));
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(new Color(71, 85, 105));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color borderCol = hovered ? new Color(100, 116, 139) : new Color(203, 213, 225);
            Color fillCol = hovered ? new Color(241, 245, 249) : new Color(248, 249, 252);
            g2.setColor(fillCol);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
            g2.setColor(borderCol);
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Float(0.6f, 0.6f, getWidth() - 1.2f, getHeight() - 1.2f, 8, 8));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private void showBookCourtDialog() {
        // ── 1. Load fresh data from DB (courts required; customers used for save-time
        // ID lookup) ─
        List<CustomerDTO> customers = customerController.getAll();
        List<CourtDTO> courts = courtController.getAllCourts();

        if (courts.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Chưa có sân nào trong hệ thống. Vui lòng thêm sân trước.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ── 2. Customer name — free-text input ────────────────────────────────────
        JTextField customerNameField = new JTextField();
        customerNameField.setPreferredSize(new Dimension(240, 30));

        // ── 3. Court combo — typed, renderer-based ───────────────────────────────
        JComboBox<CourtDTO> courtDialogCombo = new JComboBox<>(courts.toArray(new CourtDTO[0]));
        courtDialogCombo.setPreferredSize(new Dimension(240, 30));
        courtDialogCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof CourtDTO ct) {
                    setText(ct.getCourtName() + " — "
                            + com.example.winfinal.utils.FormatUtils.formatCurrency(ct.getPricePerHour()) + " ₫/giờ");
                }
                return this;
            }
        });

        // ── 4. Date field ──────────────────────────────────────────────────────
        JTextField dateField = new JTextField(LocalDate.now().toString());
        dateField.setPreferredSize(new Dimension(240, 30));

        // ── 5. Hour-slot combos (00:00 – 23:00, whole hours only) ────────────────
        String[] hourSlots = new String[24];
        for (int h = 0; h < 24; h++) {
            hourSlots[h] = String.format("%02d:00", h);
        }

        JComboBox<String> startCombo = new JComboBox<>(hourSlots);
        startCombo.setSelectedItem("08:00");
        startCombo.setPreferredSize(new Dimension(240, 30));

        JComboBox<String> endCombo = new JComboBox<>(hourSlots);
        endCombo.setSelectedItem("09:00");
        endCombo.setPreferredSize(new Dimension(240, 30));

        // ── 6. Price field — read-only, auto-calculated ───────────────────────────
        JTextField priceField = new JTextField();
        priceField.setPreferredSize(new Dimension(240, 30));
        priceField.setEditable(false);
        priceField.setBackground(new Color(245, 247, 250));
        priceField.setForeground(new Color(31, 41, 55));
        priceField.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // ── 7. Auto-calculate price ───────────────────────────────────────────────
        Runnable recalcPrice = () -> {
            try {
                CourtDTO sel = (CourtDTO) courtDialogCombo.getSelectedItem();
                if (sel == null || sel.getPricePerHour() == null)
                    return;
                LocalTime st = LocalTime.parse((String) startCombo.getSelectedItem());
                LocalTime et = LocalTime.parse((String) endCombo.getSelectedItem());
                if (!et.isAfter(st)) {
                    priceField.setText("—");
                    return;
                }
                long minutes = java.time.Duration.between(st, et).toMinutes();
                BigDecimal bookedHours = BigDecimal.valueOf(minutes)
                        .divide(BigDecimal.valueOf(60), 4, RoundingMode.HALF_UP);
                BigDecimal total = sel.getPricePerHour()
                        .multiply(bookedHours).setScale(0, RoundingMode.HALF_UP);
                priceField.setText(com.example.winfinal.utils.FormatUtils.formatCurrency(total) + " ₫");
            } catch (Exception ignored) {
            }
        };

        courtDialogCombo.addActionListener(e -> recalcPrice.run());
        startCombo.addActionListener(e -> recalcPrice.run());
        endCombo.addActionListener(e -> recalcPrice.run());
        recalcPrice.run(); // initial fill

        // ── 8. Assemble dialog ────────────────────────────────────────────────────
        Object[] fields = {
                "Tên khách hàng:", customerNameField,
                "Tên sân:", courtDialogCombo,
                "Ngày đặt (YYYY-MM-DD):", dateField,
                "Bắt đầu:", startCombo,
                "Kết thúc:", endCombo,
                "Giá tiền (VND):", priceField
        };

        if (JOptionPane.showConfirmDialog(this, fields, "Đặt sân mới",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                // \u2500\u2500 Resolve or create customer
                // \u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500
                String typedName = customerNameField.getText().trim();
                if (typedName.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Vui l\u00f2ng nh\u1eadp t\u00ean kh\u00e1ch h\u00e0ng.", "Thi\u1ebfu th\u00f4ng tin",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Try to find an existing customer with this exact name (case-insensitive)
                Integer resolvedCustomerId = customers.stream()
                        .filter(c -> c.getFullName().equalsIgnoreCase(typedName))
                        .map(CustomerDTO::getCustomerId)
                        .findFirst().orElse(null);

                if (resolvedCustomerId == null) {
                    // Name not found \u2014 create a new customer record; DB generates the ID
                    // (IDENTITY strategy)
                    customerController.create(CustomerDTO.builder()
                            .fullName(typedName)
                            .membershipType("Standard")
                            .build());

                    // Re-fetch the full list to locate the newly inserted ID
                    resolvedCustomerId = customerController.getAll().stream()
                            .filter(c -> c.getFullName().equalsIgnoreCase(typedName))
                            .map(CustomerDTO::getCustomerId)
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException(
                                    "T\u1ea1o kh\u00e1ch h\u00e0ng m\u1edbi th\u1ea5t b\u1ea1i. Vui l\u00f2ng th\u1eed l\u1ea1i."));
                }

                CourtDTO chosenCourt = (CourtDTO) courtDialogCombo.getSelectedItem();

                LocalTime st = LocalTime.parse((String) startCombo.getSelectedItem());
                LocalTime et = LocalTime.parse((String) endCombo.getSelectedItem());

                // Validate time range before saving
                if (!et.isAfter(st)) {
                    JOptionPane.showMessageDialog(this,
                            "Gi\u1edd k\u1ebft th\u00fac ph\u1ea3i sau gi\u1edd b\u1eaft \u0111\u1ea7u.\nVui l\u00f2ng ch\u1ecdn l\u1ea1i.",
                            "Th\u1eddi gian kh\u00f4ng h\u1ee3p l\u1ec7", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Recompute price from DTOs \u2014 never rely on the display string
                long minutes = java.time.Duration.between(st, et).toMinutes();
                BigDecimal bookedHours = BigDecimal.valueOf(minutes)
                        .divide(BigDecimal.valueOf(60), 4, RoundingMode.HALF_UP);
                BigDecimal totalPrice = chosenCourt.getPricePerHour()
                        .multiply(bookedHours).setScale(0, RoundingMode.HALF_UP);

                BookingDTO dto = BookingDTO.builder()
                        .customerId(resolvedCustomerId)
                        .customerFullName(typedName)
                        .courtId(chosenCourt.getCourtId())
                        .bookingDate(LocalDate.parse(dateField.getText().trim()))
                        .startTime(st)
                        .endTime(et)
                        .totalPrice(totalPrice)
                        .status("Confirmed")
                        .build();
                bookingController.createBooking(dto);
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
