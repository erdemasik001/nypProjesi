package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;

public class SeatReservationPanel extends JFrame {
    private JPanel seatGrid;
    private JLabel occupiedLabel;
    private JLabel emptyLabel;
    private JCheckBox syncCheckbox;
    private JButton[][] seatButtons;
    private boolean[][] seatStatus;
    private static final int ROWS = 30;
    private static final int COLS = 6;
    private String flightNo;

    public SeatReservationPanel(String flightNo) {
        this.flightNo = flightNo;
        this.seatButtons = new JButton[ROWS][COLS];
        this.seatStatus = new boolean[ROWS][COLS];
        initializeUI();
    }

    public SeatReservationPanel() {
        this("TK101");
    }

    private void initializeUI() {
        setTitle("Seat Reservation - " + flightNo);
        setSize(700, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(
                        new GradientPaint(0, 0, new Color(41, 128, 185), 0, getHeight(), new Color(109, 213, 250)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("💺 Seat Map - " + flightNo, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel gridContainer = new JPanel(new BorderLayout());
        gridContainer.setOpaque(false);
        seatGrid = new JPanel(new GridLayout(ROWS, COLS + 1, 3, 3));
        seatGrid.setOpaque(false);
        createSeatGrid();
        JScrollPane scrollPane = new JScrollPane(seatGrid);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        gridContainer.add(scrollPane, BorderLayout.CENTER);
        // Legend
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        legendPanel.setOpaque(false);
        legendPanel.add(createLegendItem(new Color(46, 204, 113), "Empty"));
        legendPanel.add(createLegendItem(new Color(231, 76, 60), "Occupied"));
        legendPanel.add(createLegendItem(new Color(52, 152, 219), "Selected"));
        gridContainer.add(legendPanel, BorderLayout.NORTH);
        mainPanel.add(gridContainer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);
        // Stats panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        statsPanel.setOpaque(false);
        occupiedLabel = new JLabel("Occupied: 0");
        occupiedLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        occupiedLabel.setForeground(Color.WHITE);
        emptyLabel = new JLabel("Empty: 180");
        emptyLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        emptyLabel.setForeground(Color.WHITE);
        statsPanel.add(occupiedLabel);
        statsPanel.add(emptyLabel);
        bottomPanel.add(statsPanel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setOpaque(false);
        syncCheckbox = new JCheckBox("Run Synchronized");
        syncCheckbox.setFont(new Font("Segoe UI", Font.BOLD, 14));
        syncCheckbox.setForeground(Color.WHITE);
        syncCheckbox.setOpaque(false);
        syncCheckbox.setSelected(true);
        controlPanel.add(syncCheckbox);
        JButton simBtn = createStyledButton("Start Simulation (90 Passengers)", new Color(155, 89, 182));
        simBtn.setPreferredSize(new Dimension(250, 45));
        simBtn.addActionListener(e -> startSimulation());
        controlPanel.add(simBtn);
        JButton resetBtn = createStyledButton("Reset", new Color(149, 165, 166));
        resetBtn.setPreferredSize(new Dimension(100, 45));
        resetBtn.addActionListener(e -> resetSeats());
        controlPanel.add(resetBtn);
        JButton backBtn = createStyledButton("Back", new Color(231, 76, 60));
        backBtn.setPreferredSize(new Dimension(80, 45));
        backBtn.addActionListener(e -> dispose());
        controlPanel.add(backBtn);
        bottomPanel.add(controlPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void createSeatGrid() {
        String[] colLabels = { "A", "B", "C", "", "D", "E", "F" };
        for (int row = 0; row < ROWS; row++) {
            int seatCol = 0;
            for (int col = 0; col < 7; col++) {
                if (col == 3) {
                    JLabel aisleLabel = new JLabel(String.valueOf(row + 1), SwingConstants.CENTER);
                    aisleLabel.setForeground(Color.WHITE);
                    aisleLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    seatGrid.add(aisleLabel);
                } else {
                    JButton seatBtn = createSeatButton(row, seatCol, colLabels[col]);
                    seatButtons[row][seatCol] = seatBtn;
                    seatGrid.add(seatBtn);
                    seatCol++;
                }
            }
        }
    }

    private JButton createSeatButton(int row, int col, String colLabel) {
        JButton btn = new JButton((row + 1) + colLabel);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        btn.setBackground(new Color(46, 204, 113));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(45, 25));
        btn.addActionListener(e -> toggleSeat(row, col, btn));
        return btn;
    }

    private void toggleSeat(int row, int col, JButton btn) {
        if (!seatStatus[row][col]) {
            seatStatus[row][col] = true;
            btn.setBackground(new Color(52, 152, 219));
            updateStats();
        }
    }

    private JPanel createLegendItem(Color color, String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);
        JLabel colorBox = new JLabel("■");
        colorBox.setForeground(color);
        colorBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(colorBox);
        panel.add(label);
        return panel;
    }

    private void startSimulation() {
        resetSeats();
        boolean sync = syncCheckbox.isSelected();
        Object lock = new Object();
        Thread[] threads = new Thread[90];
        for (int i = 0; i < 90; i++) {
            threads[i] = new Thread(() -> {
                Random rand = new Random();
                int row, col;
                do {
                    row = rand.nextInt(ROWS);
                    col = rand.nextInt(COLS);
                } while (seatStatus[row][col]);
                if (sync) {
                    synchronized (lock) {
                        reserveSeat(row, col);
                    }
                } else {
                    reserveSeat(row, col);
                }
            });
        }
        for (Thread t : threads)
            t.start();
        new Thread(() -> {
            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                }
            }
            SwingUtilities.invokeLater(() -> {
                updateStats();
                String msg = sync ? "Synchronized execution: Correct result (90 seats occupied)"
                        : "Not synchronized: Possible incorrect result";
                JOptionPane.showMessageDialog(this, msg, "Simulation Completed", JOptionPane.INFORMATION_MESSAGE);
            });
        }).start();
    }

    private void reserveSeat(int row, int col) {
        seatStatus[row][col] = true;
        SwingUtilities.invokeLater(() -> {
            seatButtons[row][col].setBackground(new Color(231, 76, 60));
            updateStats();
        });
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
        }
    }

    private void resetSeats() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                seatStatus[i][j] = false;
                seatButtons[i][j].setBackground(new Color(46, 204, 113));
            }
        }
        updateStats();
    }

    private void updateStats() {
        int occupied = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (seatStatus[i][j])
                    occupied++;
            }
        }
        occupiedLabel.setText("Occupied: " + occupied);
        emptyLabel.setText("Empty: " + (ROWS * COLS - occupied));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bgColor.brighter() : bgColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SeatReservationPanel().setVisible(true));
    }
}
