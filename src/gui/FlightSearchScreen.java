package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightSearchScreen extends JFrame {

    private JComboBox<String> departureCombo;
    private JComboBox<String> arrivalCombo;
    private JSpinner dateSpinner;
    private JTable flightTable;
    private DefaultTableModel tableModel;

    // City list
    private String[] cities = { "Istanbul", "Ankara", "Izmir", "Antalya", "Trabzon", "Adana", "Bursa", "Konya" };

    public FlightSearchScreen() {
        initializeUI();
        loadMockFlights();
    }

    private void initializeUI() {
        setTitle("Flight Search and Booking");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel - gradient background
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
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("✈ Flight Search", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Search panel
        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.WEST);

        // Results panel
        JPanel resultsPanel = createResultsPanel();
        mainPanel.add(resultsPanel, BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(250, 400));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Departure city
        JLabel depLabel = new JLabel("Departure City:");
        depLabel.setForeground(Color.WHITE);
        depLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        depLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(depLabel);
        panel.add(Box.createVerticalStrut(5));

        departureCombo = new JComboBox<>(cities);
        departureCombo.setMaximumSize(new Dimension(220, 35));
        departureCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(departureCombo);
        panel.add(Box.createVerticalStrut(15));

        // Arrival city
        JLabel arrLabel = new JLabel("Arrival City:");
        arrLabel.setForeground(Color.WHITE);
        arrLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        arrLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(arrLabel);
        panel.add(Box.createVerticalStrut(5));

        arrivalCombo = new JComboBox<>(cities);
        arrivalCombo.setSelectedIndex(1);
        arrivalCombo.setMaximumSize(new Dimension(220, 35));
        arrivalCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(arrivalCombo);
        panel.add(Box.createVerticalStrut(15));

        // Date
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(dateLabel);
        panel.add(Box.createVerticalStrut(5));

        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setMaximumSize(new Dimension(220, 35));
        dateSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(dateSpinner);
        panel.add(Box.createVerticalStrut(25));

        // Search button
        JButton searchBtn = createStyledButton("Search Flights", new Color(46, 204, 113));
        searchBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchBtn.setMaximumSize(new Dimension(220, 45));
        searchBtn.addActionListener(e -> searchFlights());
        panel.add(searchBtn);

        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        JLabel resultsLabel = new JLabel("Flight Results");
        resultsLabel.setForeground(Color.WHITE);
        resultsLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(resultsLabel, BorderLayout.NORTH);

        // Table
        String[] columns = { "Flight No", "Departure", "Arrival", "Date", "Time", "Price", "Available Seats" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        flightTable = new JTable(tableModel);
        flightTable.setRowHeight(30);
        flightTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        flightTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        flightTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(flightTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setOpaque(false);

        JButton selectSeatBtn = createStyledButton("Select Seat", new Color(52, 152, 219));
        selectSeatBtn.setPreferredSize(new Dimension(150, 45));
        selectSeatBtn.addActionListener(e -> openSeatSelection());
        panel.add(selectSeatBtn);

        JButton reserveBtn = createStyledButton("Make Reservation", new Color(155, 89, 182));
        reserveBtn.setPreferredSize(new Dimension(180, 45));
        reserveBtn.addActionListener(e -> makeReservation());
        panel.add(reserveBtn);

        JButton backBtn = createStyledButton("Go Back", new Color(149, 165, 166));
        backBtn.setPreferredSize(new Dimension(120, 45));
        backBtn.addActionListener(e -> dispose());
        panel.add(backBtn);

        return panel;
    }

    private void loadMockFlights() {
        // Mock flight data
        Object[][] mockData = {
                { "TK101", "Istanbul", "Ankara", "06/01/2026", "08:00", "1500 TL", 45 },
                { "TK102", "Istanbul", "Ankara", "06/01/2026", "12:30", "1350 TL", 23 },
                { "TK103", "Istanbul", "Ankara", "06/01/2026", "17:00", "1600 TL", 67 },
                { "TK201", "Ankara", "Izmir", "06/01/2026", "09:15", "1200 TL", 89 },
                { "TK202", "Izmir", "Antalya", "06/01/2026", "14:45", "950 TL", 112 },
                { "TK301", "Trabzon", "Istanbul", "07/01/2026", "06:30", "1100 TL", 56 }
        };

        for (Object[] row : mockData) {
            tableModel.addRow(row);
        }
    }

    private void searchFlights() {
        String departure = (String) departureCombo.getSelectedItem();
        String arrival = (String) arrivalCombo.getSelectedItem();

        if (departure.equals(arrival)) {
            JOptionPane.showMessageDialog(this,
                    "Departure and arrival cities cannot be the same!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mock search - backend will be called in real implementation
        tableModel.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = sdf.format((Date) dateSpinner.getValue());

        // Mock result
        tableModel.addRow(new Object[] { "TK" + (100 + (int) (Math.random() * 900)),
                departure, arrival, dateStr,
                String.format("%02d:%02d", 6 + (int) (Math.random() * 14), (int) (Math.random() * 60)),
                (800 + (int) (Math.random() * 1000)) + " TL",
                (int) (Math.random() * 150) });

        tableModel.addRow(new Object[] { "TK" + (100 + (int) (Math.random() * 900)),
                departure, arrival, dateStr,
                String.format("%02d:%02d", 6 + (int) (Math.random() * 14), (int) (Math.random() * 60)),
                (800 + (int) (Math.random() * 1000)) + " TL",
                (int) (Math.random() * 150) });

        JOptionPane.showMessageDialog(this,
                departure + " - " + arrival + ": " + tableModel.getRowCount() + " flights found.",
                "Search Result", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openSeatSelection() {
        int selectedRow = flightTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a flight!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String flightNo = (String) tableModel.getValueAt(selectedRow, 0);
        SeatReservationPanel seatPanel = new SeatReservationPanel(flightNo);
        seatPanel.setVisible(true);
    }

    private void makeReservation() {
        int selectedRow = flightTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a flight!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get passenger info
        JTextField nameField = new JTextField();
        JTextField surnameField = new JTextField();

        Object[] fields = {
                "First Name:", nameField,
                "Last Name:", surnameField
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
                "Passenger Information", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();

            if (name.isEmpty() || surname.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "First name and last name are required!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String flightNo = (String) tableModel.getValueAt(selectedRow, 0);
            String reservationCode = "RES" + System.currentTimeMillis() % 100000;

            JOptionPane.showMessageDialog(this,
                    "Reservation Successful!\n\n" +
                            "Reservation Code: " + reservationCode + "\n" +
                            "Flight: " + flightNo + "\n" +
                            "Passenger: " + name + " " + surname,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
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

                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), textX, textY);

                g2d.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FlightSearchScreen().setVisible(true);
        });
    }
}
