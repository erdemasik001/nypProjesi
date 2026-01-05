package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LoginScreen extends JFrame {

    private JButton staffButton;
    private JButton userButton;

    public LoginScreen() {
        initializeUI();
    }

    private void initializeUI() {

        setTitle("Airline Reservation System - Login");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(41, 128, 185),
                        0, getHeight(), new Color(109, 213, 250));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);

        JPanel logoPanel = new JPanel();
        logoPanel.setBounds(175, 50, 150, 150);
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new BorderLayout());

        JLabel logoLabel = new JLabel("✈", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        logoLabel.setForeground(Color.WHITE);
        logoPanel.add(logoLabel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Airline Reservation System", SwingConstants.CENTER);
        titleLabel.setBounds(50, 220, 400, 40);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Please select your login type", SwingConstants.CENTER);
        subtitleLabel.setBounds(50, 260, 400, 30);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));

        staffButton = createStyledButton("Staff Login", new Color(231, 76, 60));
        staffButton.setBounds(100, 330, 300, 55);

        userButton = createStyledButton("User Login", new Color(46, 204, 113));
        userButton.setBounds(100, 405, 300, 55);

        JLabel footerLabel = new JLabel("BLM2012 - OOP Project 2025-2026", SwingConstants.CENTER);
        footerLabel.setBounds(50, 520, 400, 30);
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerLabel.setForeground(new Color(255, 255, 255, 150));

        mainPanel.add(logoPanel);
        mainPanel.add(titleLabel);
        mainPanel.add(subtitleLabel);
        mainPanel.add(staffButton);
        mainPanel.add(userButton);
        mainPanel.add(footerLabel);

        add(mainPanel);

        setupButtonActions();
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isRollover()) {
                    g2d.setColor(backgroundColor.brighter());
                } else {
                    g2d.setColor(backgroundColor);
                }

                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), textX, textY);

                g2d.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.repaint();
            }
        });

        return button;
    }

    private void setupButtonActions() {
        // Staff Login -> Admin Screen
        staffButton.addActionListener(e -> {
            String password = JOptionPane.showInputDialog(this, "Staff Password:", "Login", JOptionPane.PLAIN_MESSAGE);
            if (password != null && password.equals("admin")) {
                dispose();
                new AdminScreen().setVisible(true);
            } else if (password != null) {
                JOptionPane.showMessageDialog(this, "Wrong password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // User Login -> User Menu
        userButton.addActionListener(e -> {
            String[] options = { "Search Flights", "My Reservations", "Seat Demo", "Cancel" };
            int choice = JOptionPane.showOptionDialog(this,
                    "Please select an action:", "User Menu",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);

            switch (choice) {
                case 0:
                    new FlightSearchScreen().setVisible(true);
                    break;
                case 1:
                    new ReservationManagementScreen().setVisible(true);
                    break;
                case 2:
                    new SeatReservationPanel().setVisible(true);
                    break;
            }
        });
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.setVisible(true);
        });
    }
}
