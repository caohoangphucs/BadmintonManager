package com.example.winfinal.view;

import com.example.winfinal.controller.BookingController;
import com.example.winfinal.dto.BookingDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BookingPanel extends JPanel {
    private final BookingController bookingController;
    private JTable table;
    private DefaultTableModel model;

    public BookingPanel() {
        this.bookingController = new BookingController();
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        setBackground(Color.WHITE);
        initComponents();
        loadData();
    }

    private void initComponents() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        JLabel title = new JLabel("Quản lý lượt đặt sân");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        bar.add(title, BorderLayout.WEST);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.setBackground(Color.WHITE);
        JButton bookBtn = createButton("Đặt sân", new Color(46, 204, 113));
        JButton cancelBtn = createButton("Hủy đặt sân", new Color(231, 76, 60));
        JButton refreshBtn = createButton("Làm mới", new Color(52, 152, 219));

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
        refreshBtn.addActionListener(e -> loadData());

        btns.add(bookBtn);
        btns.add(cancelBtn);
        btns.add(refreshBtn);
        bar.add(btns, BorderLayout.EAST);
        add(bar, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Khách hàng", "Sân", "Ngày đặt", "Bắt đầu", "Kết thúc", "Giá tiền (VND)", "Trạng thái"}, 0);
        table = new com.example.winfinal.view.components.ModernTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        model.setRowCount(0);
        bookingController.getAll().forEach(b -> {
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
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
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
