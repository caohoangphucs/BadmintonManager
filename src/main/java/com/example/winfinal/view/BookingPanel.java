package com.example.winfinal.view;

import com.example.winfinal.controller.BookingController;
import com.example.winfinal.dto.BookingDTO;
import javax.swing.*;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class BookingPanel extends JPanel {
    private final BookingController bookingController;
    private JTable table;
    private DefaultTableModel model;
    
    private JComboBox<String> courtCombo;
    private JComboBox<String> statusCombo;

    public BookingPanel() {
        this.bookingController = new BookingController();
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        setBackground(Color.WHITE);
        initComponents();
        loadData();
    }

    private void initComponents() {
        JPanel northWrapper = new JPanel(new BorderLayout(0, 10));
        northWrapper.setBackground(Color.WHITE);

        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        JLabel title = new JLabel("Quản lý lượt đặt sân");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        bar.add(title, BorderLayout.WEST);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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
                if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn hủy lượt đặt sân này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
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
        northWrapper.add(bar, BorderLayout.NORTH);

        // ── Filter Panel ─────────────────────────────────────────────────────
        JPanel filterCard = new JPanel(new GridBagLayout());
        filterCard.setBackground(new Color(248, 249, 252));
        filterCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(0, 0, 0, 10);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        gc.gridy = 0;

        // --- Sân label ---
        gc.gridx = 0; gc.weightx = 0;
        filterCard.add(makeFilterLabel("Sân:"), gc);

        // --- Sân combo ---
        courtCombo = new JComboBox<>(new String[]{"Tất cả"});
        styleCombo(courtCombo, 160);
        gc.gridx = 1;
        filterCard.add(courtCombo, gc);

        // --- separator gap ---
        gc.gridx = 2; gc.insets = new Insets(0, 6, 0, 10);
        filterCard.add(Box.createHorizontalStrut(6), gc);
        gc.insets = new Insets(0, 0, 0, 10);

        // --- Trạng thái label ---
        gc.gridx = 3; gc.weightx = 0;
        filterCard.add(makeFilterLabel("Trạng thái:"), gc);

        // --- Trạng thái combo ---
        statusCombo = new JComboBox<>(new String[]{"Tất cả", "Đã xác nhận", "Chờ xác nhận", "Đã hủy"});
        styleCombo(statusCombo, 170);
        gc.gridx = 4;
        filterCard.add(statusCombo, gc);

        // --- spacer pushes buttons to the right ---
        gc.gridx = 5; gc.weightx = 1.0; gc.fill = GridBagConstraints.HORIZONTAL;
        filterCard.add(Box.createHorizontalGlue(), gc);
        gc.fill = GridBagConstraints.NONE; gc.weightx = 0;

        // --- Lọc (primary) ---
        JButton filterBtn = createButton("Lọc", new Color(55, 93, 169));
        filterBtn.setPreferredSize(new Dimension(100, 34));
        gc.gridx = 6; gc.insets = new Insets(0, 0, 0, 8);
        filterCard.add(filterBtn, gc);

        // --- Xóa lọc (ghost / secondary) ---
        JButton clearBtn = new GhostButton("Xóa lọc");
        gc.gridx = 7; gc.insets = new Insets(0, 0, 0, 0);
        filterCard.add(clearBtn, gc);

        filterBtn.addActionListener(e -> loadData());
        clearBtn.addActionListener(e -> {
            courtCombo.setSelectedIndex(0);
            statusCombo.setSelectedIndex(0);
            loadData();
        });

        northWrapper.add(filterCard, BorderLayout.SOUTH);
        add(northWrapper, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Khách hàng", "Sân", "Ngày đặt", "Bắt đầu", "Kết thúc", "Giá tiền (VND)", "Trạng thái"}, 0);
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
                if ("Tất cả".equals(selectedStatus)) return true;
                String st = b.getStatus();
                if ("Đã xác nhận".equals(selectedStatus)) return "Confirmed".equals(st);
                if ("Chờ xác nhận".equals(selectedStatus)) return "Pending".equals(st);
                if ("Đã hủy".equals(selectedStatus)) return "Cancelled".equals(st);
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
                model.addRow(new Object[]{
                    b.getBookingId(), b.getCustomerFullName(), b.getCourtName(), b.getBookingDate(), b.getStartTime(), b.getEndTime(), priceStr, statusHTML
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
            BorderFactory.createEmptyBorder(2, 6, 2, 4)
        ));
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
                @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color borderCol = hovered ? new Color(100, 116, 139) : new Color(203, 213, 225);
            Color fillCol   = hovered ? new Color(241, 245, 249) : new Color(248, 249, 252);
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
        JTextField custId = new JTextField();
        JTextField courtId = new JTextField();
        JTextField date = new JTextField(java.time.LocalDate.now().toString());
        JTextField startTime = new JTextField("08:00");
        JTextField endTime = new JTextField("09:00");
        JTextField price = new JTextField();

        Object[] fields = {
            "ID Khách hàng:", custId,
            "ID Sân:", courtId,
            "Ngày đặt (YYYY-MM-DD):", date,
            "Bắt đầu (HH:MM):", startTime,
            "Kết thúc (HH:MM):", endTime,
            "Giá tiền:", price
        };
        
        if (JOptionPane.showConfirmDialog(this, fields, "Đặt sân mới", JOptionPane.OK_CANCEL_OPTION) == 0) {
            try {
                BookingDTO dto = BookingDTO.builder()
                    .customerId(Integer.parseInt(custId.getText()))
                    .courtId(Integer.parseInt(courtId.getText()))
                    .bookingDate(java.time.LocalDate.parse(date.getText()))
                    .startTime(java.time.LocalTime.parse(startTime.getText()))
                    .endTime(java.time.LocalTime.parse(endTime.getText()))
                    .totalPrice(new java.math.BigDecimal(price.getText()))
                    .status("Confirmed")
                    .build();
                bookingController.createBooking(dto);
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: " + ex.getMessage());
            }
        }
    }
}
