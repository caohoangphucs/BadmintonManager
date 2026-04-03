package com.example.winfinal.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {
    private Color normalColor;
    private Color hoverColor;
    private Color pressedColor;
    private boolean isHovered = false;
    private boolean isPressed = false;

    public ModernButton(String text, Color baseColor) {
        super(text);
        this.normalColor = baseColor;
        this.hoverColor = brighten(baseColor, 0.15f);
        this.pressedColor = darken(baseColor, 0.1f);

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(getPreferredSize().width + 30, 36));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw flat main background
        if (isPressed) {
            g2.setColor(pressedColor);
        } else if (isHovered) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(normalColor);
        }
        
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

        g2.dispose();
        super.paintComponent(g);
    }

    private Color brighten(Color c, float fraction) {
        int r = Math.min(255, (int)(c.getRed() + (255 - c.getRed()) * fraction));
        int g = Math.min(255, (int)(c.getGreen() + (255 - c.getGreen()) * fraction));
        int b = Math.min(255, (int)(c.getBlue() + (255 - c.getBlue()) * fraction));
        return new Color(r, g, b);
    }

    private Color darken(Color c, float fraction) {
        int r = Math.max(0, (int)(c.getRed() * (1 - fraction)));
        int g = Math.max(0, (int)(c.getGreen() * (1 - fraction)));
        int b = Math.max(0, (int)(c.getBlue() * (1 - fraction)));
        return new Color(r, g, b);
    }
}
