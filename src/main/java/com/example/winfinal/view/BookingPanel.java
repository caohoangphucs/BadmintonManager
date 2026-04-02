package com.example.winfinal.view;

import com.example.winfinal.controller.BookingController;
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
        JButton cancelBtn = createButton("Hủy đặt sân", new Color(231, 76, 60));
        JButton refreshBtn = createButton("Làm mới", new Color(52, 152, 219));

        cancelBtn.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r != -1) {
                bookingController.cancelBooking((Integer) table.getValueAt(r, 0));
                loadData();
            }
        });
        refreshBtn.addActionListener(e -> loadData());

        btns.add(cancelBtn);
        btns.add(refreshBtn);
        bar.add(btns, BorderLayout.EAST);
        add(bar, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Khách hàng", "Sân", "Ngày đặt", "Bắt đầu", "Kết thúc", "Giá tiền", "Trạng thái"}, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadData() {
        model.setRowCount(0);
        bookingController.getAll().forEach(b -> model.addRow(new Object[]{
            b.getBookingId(), b.getCustomerFullName(), b.getCourtName(), b.getBookingDate(), b.getStartTime(), b.getEndTime(), b.getTotalPrice(), b.getStatus()
        }));
    }

    private JButton createButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return b;
    }
}
