package com.example.winfinal.view;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel sidebar;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private java.util.List<JButton> sidebarButtons = new java.util.ArrayList<>();

    public MainFrame() {
        setTitle("Quản lý sân cầu lông - Badminton Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        initTheme();
        initComponents();
    }

    private void initTheme() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            // Customize some colors if needed
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Sidebar
        sidebar = new JPanel();
        sidebar.setBackground(new Color(45, 52, 54));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        addSidebarLabel("  BADMINTON PRO", new Font("Segoe UI", Font.BOLD, 22), Color.WHITE);
        sidebar.add(Box.createVerticalStrut(30));

        addSidebarButton("Tổng quan", "DASHBOARD");
        addSidebarButton("Quản lý sân", "COURT");
        addSidebarButton("Lượt đặt sân", "BOOKING");
        addSidebarButton("Khách hàng", "CUSTOMER");
        addSidebarButton("Trang thiết bị", "EQUIPMENT");
        addSidebarButton("Khuyến mãi", "PROMOTION");

        add(sidebar, BorderLayout.WEST);

        // Content panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        contentPanel.add(new DashboardPanel(), "DASHBOARD");
        contentPanel.add(new CourtPanel(), "COURT");
        contentPanel.add(new CustomerPanel(), "CUSTOMER");
        contentPanel.add(new BookingPanel(), "BOOKING");
        // Other panels will be added here
        contentPanel.add(new EquipmentPanel(), "EQUIPMENT");
        contentPanel.add(new PromotionPanel(), "PROMOTION");

        add(contentPanel, BorderLayout.CENTER);

        if (!sidebarButtons.isEmpty()) {
            setActiveButton(sidebarButtons.get(0));
        }
    }

    private void setActiveButton(JButton activeBtn) {
        for (JButton btn : sidebarButtons) {
            if (btn == activeBtn) {
                btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
                btn.setForeground(Color.WHITE);
                btn.setBackground(new Color(60, 68, 70));
            } else {
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                btn.setForeground(Color.LIGHT_GRAY);
                btn.setBackground(new Color(45, 52, 54));
            }
        }
    }

    private void addSidebarLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        sidebar.add(label);
    }

    private void addSidebarButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(230, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(45, 52, 54));
        btn.setForeground(Color.LIGHT_GRAY);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(9, 132, 227));
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.getFont().isBold()) {
                    btn.setBackground(new Color(60, 68, 70));
                    btn.setForeground(Color.WHITE);
                } else {
                    btn.setBackground(new Color(45, 52, 54));
                    btn.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

        btn.addActionListener(e -> {
            cardLayout.show(contentPanel, cardName);
            setActiveButton(btn);
        });

        sidebarButtons.add(btn);
        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(10));
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
