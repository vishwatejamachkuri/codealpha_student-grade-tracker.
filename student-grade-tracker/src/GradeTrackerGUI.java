import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

/**
 * Modern, highly vibrant Graphical User Interface for the Student Grade Tracker.
 * Features:
 * - Purple-Indigo gradient header with gold accents.
 * - 5 colorful metric cards with tinted backgrounds and icons.
 * - Live graphical Grade Distribution bar chart panel with custom colored bars.
 * - Table with alternating row colors and rounded color-coded Grade pill badges.
 * - Vibrant hover-responsive action buttons.
 * - Search filter and comprehensive summary report dialog.
 */
public class GradeTrackerGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private final transient GradeTracker tracker;
    private DefaultTableModel tableModel;
    private JTable studentTable;
    private transient TableRowSorter<DefaultTableModel> tableSorter;

    // Stat card value labels
    private JLabel lblTotalStudents;
    private JLabel lblClassAverage;
    private JLabel lblHighestScore;
    private JLabel lblLowestScore;
    private JLabel lblTopStudent;

    // Distribution visual bars panel
    private GradeDistributionPanel distributionPanel;

    public GradeTrackerGUI() {
        this.tracker = new GradeTracker();

        // Populate with rich initial demo records
        initializeDemoData();

        // Configure main application window
        setTitle("✨ Student Grade Tracker - Analytics & Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 780);
        setMinimumSize(new Dimension(950, 620));
        setLocationRelativeTo(null);

        // Build GUI layout
        initComponents();
        refreshAllViews();
    }

    /**
     * Pre-populates sample student data across various score ranges.
     */
    private void initializeDemoData() {
        ArrayList<Double> aliceMarks = new ArrayList<>();
        aliceMarks.add(95.0); aliceMarks.add(98.0); aliceMarks.add(92.0);
        tracker.addStudent(new Student("101", "Alice Johnson", aliceMarks));

        ArrayList<Double> rahulMarks = new ArrayList<>();
        rahulMarks.add(88.0); rahulMarks.add(91.0); rahulMarks.add(85.0);
        tracker.addStudent(new Student("102", "Rahul Sharma", rahulMarks));

        ArrayList<Double> dianaMarks = new ArrayList<>();
        dianaMarks.add(78.0); dianaMarks.add(74.0); dianaMarks.add(81.0);
        tracker.addStudent(new Student("103", "Diana Prince", dianaMarks));

        ArrayList<Double> bobMarks = new ArrayList<>();
        bobMarks.add(64.0); bobMarks.add(68.0); bobMarks.add(62.0);
        tracker.addStudent(new Student("104", "Bob Martin", bobMarks));

        ArrayList<Double> charlieMarks = new ArrayList<>();
        charlieMarks.add(54.0); charlieMarks.add(58.0); charlieMarks.add(52.0);
        tracker.addStudent(new Student("105", "Charlie Brown", charlieMarks));
    }

    private void initComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout(0, 14));
        rootPanel.setBackground(new Color(241, 245, 249)); // Slate 100

        // =========================================================================
        // 1. TOP SECTION: Gradient Header Banner + 5 Colorful Stat Cards
        // =========================================================================
        JPanel topContainer = new JPanel(new BorderLayout(0, 12));
        topContainer.setOpaque(false);

        // Gradient Header
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(79, 70, 229),      // Indigo 600
                        getWidth(), getHeight(), new Color(147, 51, 234) // Purple 600
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel titleLabel = new JLabel("🎓 Student Grade Tracker & Performance Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Real-time score calculation, visual grade distributions, and performance analytics");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(224, 231, 255)); // Indigo 100

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        topContainer.add(headerPanel, BorderLayout.NORTH);

        // 5 Stat Cards Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 5, 12, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        lblTotalStudents = new JLabel("0", SwingConstants.CENTER);
        lblClassAverage = new JLabel("0.00%", SwingConstants.CENTER);
        lblHighestScore = new JLabel("0.00", SwingConstants.CENTER);
        lblLowestScore = new JLabel("0.00", SwingConstants.CENTER);
        lblTopStudent = new JLabel("N/A", SwingConstants.CENTER);

        // Colorful metric cards with tinted backgrounds, colored borders, and vibrant numbers
        statsPanel.add(createColorfulCard("👥 Total Students", lblTotalStudents, new Color(239, 246, 255), new Color(147, 197, 253), new Color(29, 78, 216)));
        statsPanel.add(createColorfulCard("📈 Class Average", lblClassAverage, new Color(236, 253, 245), new Color(110, 231, 183), new Color(4, 120, 87)));
        statsPanel.add(createColorfulCard("🏆 Highest Mark", lblHighestScore, new Color(245, 243, 255), new Color(196, 181, 253), new Color(109, 40, 217)));
        statsPanel.add(createColorfulCard("📉 Lowest Mark", lblLowestScore, new Color(254, 243, 199), new Color(252, 211, 77), new Color(180, 83, 9)));
        statsPanel.add(createColorfulCard("⭐ Top Performer", lblTopStudent, new Color(255, 241, 242), new Color(253, 164, 175), new Color(190, 18, 60)));

        topContainer.add(statsPanel, BorderLayout.SOUTH);
        rootPanel.add(topContainer, BorderLayout.NORTH);

        // =========================================================================
        // 2. CENTER SECTION: Table (Left/Center) + Grade Distribution Panel (Right)
        // =========================================================================
        JPanel centerContainer = new JPanel(new BorderLayout(14, 0));
        centerContainer.setOpaque(false);
        centerContainer.setBorder(new EmptyBorder(0, 20, 0, 20));

        // Left Panel: Search Bar + Table
        JPanel tablePanel = new JPanel(new BorderLayout(0, 8));
        tablePanel.setOpaque(false);

        // Search Bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        searchBar.setBackground(Color.WHITE);
        searchBar.setBorder(new CompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(4, 10, 4, 10)
        ));

        JLabel searchIcon = new JLabel("🔍 Search Student:");
        searchIcon.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchIcon.setForeground(new Color(71, 85, 105));

        JTextField txtSearch = new JTextField(22);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setBorder(new CompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(4, 8, 4, 8)
        ));

        JButton btnClearSearch = createColorButton("Clear", new Color(148, 163, 184), Color.WHITE, new Color(100, 116, 139));
        btnClearSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnClearSearch.addActionListener(e -> txtSearch.setText(""));

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filterTable(); }

            private void filterTable() {
                String text = txtSearch.getText().trim();
                if (text.isEmpty()) {
                    tableSorter.setRowFilter(null);
                } else {
                    tableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        searchBar.add(searchIcon);
        searchBar.add(txtSearch);
        searchBar.add(btnClearSearch);
        tablePanel.add(searchBar, BorderLayout.NORTH);

        // Student JTable
        String[] columnNames = {"ID", "Student Name", "Subject Marks", "Total", "Average", "Highest", "Lowest", "Grade"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.setRowHeight(32);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setSelectionBackground(new Color(224, 231, 255)); // Soft Indigo Selection
        studentTable.setSelectionForeground(new Color(30, 41, 59));
        studentTable.setGridColor(new Color(241, 245, 249));
        studentTable.setShowVerticalLines(false);
        studentTable.setFillsViewportHeight(true);

        // Header Styling
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        studentTable.getTableHeader().setBackground(new Color(238, 242, 255)); // Light Indigo
        studentTable.getTableHeader().setForeground(new Color(49, 46, 129));
        studentTable.getTableHeader().setPreferredSize(new Dimension(0, 36));

        // Column Renderers
        setupTableRenderers();

        tableSorter = new TableRowSorter<>(tableModel);
        studentTable.setRowSorter(tableSorter);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        centerContainer.add(tablePanel, BorderLayout.CENTER);

        // Right Panel: Visual Grade Distribution Chart
        distributionPanel = new GradeDistributionPanel();
        distributionPanel.setPreferredSize(new Dimension(280, 0));
        centerContainer.add(distributionPanel, BorderLayout.EAST);

        rootPanel.add(centerContainer, BorderLayout.CENTER);

        // =========================================================================
        // 3. BOTTOM SECTION: Colorful Action Buttons Toolbar
        // =========================================================================
        JPanel toolbarWrapper = new JPanel(new BorderLayout());
        toolbarWrapper.setOpaque(false);
        toolbarWrapper.setBorder(new EmptyBorder(8, 20, 14, 20));

        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        toolbarPanel.setBackground(Color.WHITE);
        toolbarPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(4, 10, 4, 10)
        ));

        // Vibrant distinct action buttons with custom graphics rendering
        VibrantButton btnAdd = new VibrantButton("➕ Add Student", new Color(16, 185, 129), new Color(5, 150, 105)); // Emerald Green
        VibrantButton btnUpdate = new VibrantButton("✏️ Update Grades", new Color(79, 70, 229), new Color(67, 56, 202)); // Royal Indigo
        VibrantButton btnDelete = new VibrantButton("🗑️ Remove Student", new Color(239, 68, 68), new Color(220, 38, 38)); // Crimson Red
        VibrantButton btnReport = new VibrantButton("📊 Summary Report", new Color(245, 158, 11), new Color(217, 119, 6)); // Amber Gold
        VibrantButton btnRefresh = new VibrantButton("🔄 Refresh", new Color(6, 182, 212), new Color(8, 145, 178)); // Ocean Cyan

        btnAdd.addActionListener(e -> showAddStudentDialog());
        btnUpdate.addActionListener(e -> showUpdateGradesDialog());
        btnDelete.addActionListener(e -> handleDeleteStudent());
        btnReport.addActionListener(e -> showSummaryReportDialog());
        btnRefresh.addActionListener(e -> refreshAllViews());

        toolbarPanel.add(btnAdd);
        toolbarPanel.add(btnUpdate);
        toolbarPanel.add(btnDelete);
        toolbarPanel.add(btnReport);
        toolbarPanel.add(btnRefresh);

        toolbarWrapper.add(toolbarPanel, BorderLayout.CENTER);
        rootPanel.add(toolbarWrapper, BorderLayout.SOUTH);

        setContentPane(rootPanel);
    }

    /**
     * Creates a styled table cell renderer with zebra striping and custom alignment.
     */
    private DefaultTableCellRenderer createZebraRenderer(int horizontalAlignment) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    c.setForeground(new Color(30, 41, 59));
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return c;
            }
        };
        renderer.setHorizontalAlignment(horizontalAlignment);
        return renderer;
    }

    /**
     * Creates a renderer for average percentage scores with color-coded thresholds.
     */
    private DefaultTableCellRenderer createAverageScoreRenderer() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    c.setForeground(new Color(30, 41, 59));

                    if (value != null) {
                        try {
                            String str = value.toString().replace("%", "").trim();
                            double val = Double.parseDouble(str);
                            if (val >= 90.0) setForeground(new Color(5, 150, 105));      // Green
                            else if (val >= 80.0) setForeground(new Color(37, 99, 235));  // Blue
                            else if (val >= 70.0) setForeground(new Color(217, 119, 6));  // Amber
                            else if (val >= 60.0) setForeground(new Color(234, 88, 12));  // Orange
                            else setForeground(new Color(220, 38, 38));                   // Red
                            setFont(getFont().deriveFont(Font.BOLD));
                        } catch (NumberFormatException ignored) {}
                    }
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return c;
            }
        };
        renderer.setHorizontalAlignment(SwingConstants.RIGHT);
        return renderer;
    }

    /**
     * Configures custom cell renderers with alternating row striping and colored Grade Badges.
     */
    private void setupTableRenderers() {
        DefaultTableCellRenderer leftAlign = createZebraRenderer(SwingConstants.LEFT);
        DefaultTableCellRenderer centerAlign = createZebraRenderer(SwingConstants.CENTER);
        DefaultTableCellRenderer rightAlign = createZebraRenderer(SwingConstants.RIGHT);
        DefaultTableCellRenderer avgRenderer = createAverageScoreRenderer();

        // Grade Column Badge Renderer
        studentTable.getColumnModel().getColumn(0).setCellRenderer(centerAlign); // ID
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(55);

        studentTable.getColumnModel().getColumn(1).setCellRenderer(leftAlign);   // Name
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(150);

        studentTable.getColumnModel().getColumn(2).setCellRenderer(leftAlign);   // Marks
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(150);

        studentTable.getColumnModel().getColumn(3).setCellRenderer(rightAlign);  // Total
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(75);

        studentTable.getColumnModel().getColumn(4).setCellRenderer(avgRenderer); // Average %
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(85);

        studentTable.getColumnModel().getColumn(5).setCellRenderer(rightAlign);  // Highest
        studentTable.getColumnModel().getColumn(5).setPreferredWidth(75);

        studentTable.getColumnModel().getColumn(6).setCellRenderer(rightAlign);  // Lowest
        studentTable.getColumnModel().getColumn(6).setPreferredWidth(75);

        studentTable.getColumnModel().getColumn(7).setCellRenderer(new GradeBadgeCellRenderer()); // Grade Pill
        studentTable.getColumnModel().getColumn(7).setPreferredWidth(70);
    }

    /**
     * Custom renderer that paints a colorful rounded pill badge for student grades.
     */
    private static class GradeBadgeCellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            String grade = (value != null) ? value.toString() : "-";
            Color bgColor;
            Color fgColor;

            switch (grade) {
                case "A" -> {
                    bgColor = new Color(220, 252, 231); // Green 100
                    fgColor = new Color(21, 128, 61);   // Green 700
                }
                case "B" -> {
                    bgColor = new Color(219, 234, 254); // Blue 100
                    fgColor = new Color(29, 78, 216);   // Blue 700
                }
                case "C" -> {
                    bgColor = new Color(254, 243, 199); // Amber 100
                    fgColor = new Color(180, 83, 9);    // Amber 700
                }
                case "D" -> {
                    bgColor = new Color(255, 237, 213); // Orange 100
                    fgColor = new Color(194, 65, 12);   // Orange 700
                }
                default -> {
                    bgColor = new Color(254, 226, 226); // Red 100
                    fgColor = new Color(185, 28, 28);   // Red 700
                }
            }

            JPanel pillPanel = new JPanel(new GridBagLayout()) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(bgColor);
                    g2.fill(new RoundRectangle2D.Float(12, 4, getWidth() - 24, getHeight() - 8, 14, 14));
                    g2.setColor(fgColor);
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.draw(new RoundRectangle2D.Float(12, 4, getWidth() - 24, getHeight() - 8, 14, 14));
                    g2.dispose();
                }
            };
            pillPanel.setBackground(isSelected ? table.getSelectionBackground() : (row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252)));

            JLabel label = new JLabel(grade);
            label.setFont(new Font("Segoe UI", Font.BOLD, 13));
            label.setForeground(fgColor);
            pillPanel.add(label);

            return pillPanel;
        }
    }

    /**
     * Builds a colorful stat card with tinted background, colored border, and vibrant typography.
     */
    private JPanel createColorfulCard(String title, JLabel valueLabel, Color bgColor, Color borderColor, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(0, 4)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(accentColor.darker());

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(accentColor);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    /**
     * Custom JButton that guarantees vibrant colors, smooth gradient depth,
     * tactile shadows, and hover transitions across all platforms (including Windows L&F).
     */
    public static class VibrantButton extends JButton {
        private static final long serialVersionUID = 1L;
        private final Color baseColor;
        private final Color hoverColor;
        private final Color pressedColor;
        private boolean isHovered = false;
        private boolean isPressed = false;

        public VibrantButton(String text, Color baseColor, Color hoverColor) {
            super(text);
            this.baseColor = baseColor;
            this.hoverColor = hoverColor;
            this.pressedColor = baseColor.darker();

            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(8, 16, 8, 16));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    isPressed = false;
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

            Color currentBg = isPressed ? pressedColor : (isHovered ? hoverColor : baseColor);

            int width = getWidth();
            int height = getHeight();
            int arc = 12;

            // Draw subtle drop shadow below button
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(1, 2, width - 2, height - 2, arc, arc);

            // Draw vibrant gradient button background
            GradientPaint gp = new GradientPaint(
                    0, 0, currentBg.brighter(),
                    0, height, currentBg
            );
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, width - 1, height - 2, arc, arc);

            // Draw refined crisp border
            g2.setColor(currentBg.darker());
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, width - 1, height - 2, arc, arc);

            // Centered Text Rendering
            FontMetrics fm = g2.getFontMetrics();
            Rectangle stringBounds = fm.getStringBounds(getText(), g2).getBounds();
            int textX = (width - stringBounds.width) / 2;
            int textY = (height - stringBounds.height) / 2 + fm.getAscent() - 1;

            // Text shadow for high readability
            g2.setColor(new Color(0, 0, 0, 60));
            g2.drawString(getText(), textX, textY + 1);

            // Crisp text foreground
            g2.setColor(getForeground());
            g2.drawString(getText(), textX, textY);

            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension(Math.max(d.width + 16, 110), Math.max(d.height, 36));
        }
    }

    /**
     * Factory method creating a colorful VibrantButton with custom text color.
     */
    private VibrantButton createColorButton(String text, Color baseColor, Color fgColor, Color hoverColor) {
        VibrantButton btn = new VibrantButton(text, baseColor, hoverColor);
        if (fgColor != null) {
            btn.setForeground(fgColor);
        }
        return btn;
    }

    /**
     * Refreshes table, stats cards, and the grade distribution chart simultaneously.
     */
    private void refreshAllViews() {
        refreshTableData();
        refreshStatisticsCards();
        distributionPanel.updateDistribution(tracker);
    }

    private void refreshTableData() {
        tableModel.setRowCount(0);
        for (Student s : tracker.getStudents()) {
            StringBuilder marksStr = new StringBuilder();
            ArrayList<Double> marks = s.getMarks();
            for (int i = 0; i < marks.size(); i++) {
                marksStr.append(String.format("%.1f%s", marks.get(i), (i == marks.size() - 1) ? "" : ", "));
            }

            Object[] row = {
                    s.getId(),
                    s.getName(),
                    marksStr.toString(),
                    String.format("%.2f", s.calculateTotal()),
                    String.format("%.2f%%", s.calculateAverage()),
                    String.format("%.2f", s.getHighestScore()),
                    String.format("%.2f", s.getLowestScore()),
                    String.valueOf(s.calculateGrade())
            };
            tableModel.addRow(row);
        }
    }

    private void refreshStatisticsCards() {
        int count = tracker.getStudentCount();
        lblTotalStudents.setText(String.valueOf(count));

        if (count == 0) {
            lblClassAverage.setText("0.00%");
            lblHighestScore.setText("0.00");
            lblLowestScore.setText("0.00");
            lblTopStudent.setText("N/A");
            return;
        }

        lblClassAverage.setText(String.format("%.2f%%", tracker.getClassAverage()));
        lblHighestScore.setText(String.format("%.2f", tracker.getClassHighestScore()));
        lblLowestScore.setText(String.format("%.2f", tracker.getClassLowestScore()));

        Student top = tracker.getTopStudent();
        if (top != null) {
            lblTopStudent.setText(String.format("%s (%.1f%%)", top.getName(), top.calculateAverage()));
        } else {
            lblTopStudent.setText("N/A");
        }
    }

    /**
     * Modal Dialog for adding a student.
     */
    private void showAddStudentDialog() {
        JDialog dialog = new JDialog(this, "➕ Add New Student", true);
        dialog.setSize(440, 330);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 14));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(20, 24, 10, 24));

        JLabel lblId = new JLabel("Student ID / Roll No:");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JTextField txtId = new JTextField();

        JLabel lblName = new JLabel("Student Full Name:");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JTextField txtName = new JTextField();

        JLabel lblMarks = new JLabel("Subject Marks (0 - 100):");
        lblMarks.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JTextField txtMarks = new JTextField();

        JLabel lblHelp = new JLabel("e.g. 95, 88.5, 92, 84");
        lblHelp.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHelp.setForeground(new Color(100, 116, 139));

        panel.add(lblId);
        panel.add(txtId);
        panel.add(lblName);
        panel.add(txtName);
        panel.add(lblMarks);
        panel.add(txtMarks);
        panel.add(new JLabel("Format:"));
        panel.add(lblHelp);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(new Color(241, 245, 249));

        JButton btnSave = createColorButton("Save Student", new Color(16, 185, 129), Color.WHITE, new Color(5, 150, 105));
        JButton btnCancel = createColorButton("Cancel", new Color(148, 163, 184), Color.WHITE, new Color(100, 116, 139));

        btnSave.addActionListener(e -> {
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            String marksText = txtMarks.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Student ID cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (tracker.idExists(id)) {
                JOptionPane.showMessageDialog(dialog, "Student ID '" + id + "' already exists! ID must be unique.", "Duplicate ID", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Student name cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (marksText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter at least one subject mark.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ArrayList<Double> marksList = parseMarksInput(dialog, marksText);
            if (marksList == null) return;

            Student student = new Student(id, name, marksList);
            if (tracker.addStudent(student)) {
                refreshAllViews();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "🎉 Student '" + name + "' added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, "Could not add student.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Modal Dialog for updating student grades.
     */
    private void showUpdateGradesDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = studentTable.convertRowIndexToModel(selectedRow);
        String id = (String) tableModel.getValueAt(modelRow, 0);
        Student student = tracker.findStudentById(id);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Selected student could not be found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "✏️ Update Grades - " + student.getName(), true);
        dialog.setSize(440, 270);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 14));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(20, 24, 10, 24));

        panel.add(new JLabel("Student ID:"));
        JLabel valId = new JLabel(student.getId());
        valId.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(valId);

        panel.add(new JLabel("Student Name:"));
        JLabel valName = new JLabel(student.getName());
        valName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(valName);

        panel.add(new JLabel("New Subject Marks:"));
        StringBuilder sb = new StringBuilder();
        ArrayList<Double> currentMarks = student.getMarks();
        for (int i = 0; i < currentMarks.size(); i++) {
            sb.append(currentMarks.get(i)).append((i == currentMarks.size() - 1) ? "" : ", ");
        }
        JTextField txtMarks = new JTextField(sb.toString());
        panel.add(txtMarks);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(new Color(241, 245, 249));

        JButton btnUpdate = createColorButton("Update Marks", new Color(79, 70, 229), Color.WHITE, new Color(67, 56, 202));
        JButton btnCancel = createColorButton("Cancel", new Color(148, 163, 184), Color.WHITE, new Color(100, 116, 139));

        btnUpdate.addActionListener(e -> {
            String marksText = txtMarks.getText().trim();
            if (marksText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Subject marks cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ArrayList<Double> newMarks = parseMarksInput(dialog, marksText);
            if (newMarks == null) return;

            tracker.updateStudent(id, newMarks);
            refreshAllViews();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "✓ Grades for '" + student.getName() + "' updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnUpdate);

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Handles student removal with confirmation.
     */
    private void handleDeleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table to remove.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = studentTable.convertRowIndexToModel(selectedRow);
        String id = (String) tableModel.getValueAt(modelRow, 0);
        String name = (String) tableModel.getValueAt(modelRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to permanently remove student:\n" + name + " (ID: " + id + ")?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean removed = tracker.removeStudent(id);
            if (removed) {
                refreshAllViews();
                JOptionPane.showMessageDialog(this, "Student '" + name + "' was removed.", "Removed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Could not remove student.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Displays executive summary report modal with monospaced text and copy tool.
     */
    private void showSummaryReportDialog() {
        JDialog dialog = new JDialog(this, "📊 Executive Summary Report", true);
        dialog.setSize(800, 580);
        dialog.setLocationRelativeTo(this);

        String reportText = tracker.generateSummaryReportString();

        JTextArea textArea = new JTextArea(reportText);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        textArea.setEditable(false);
        textArea.setBackground(new Color(248, 250, 252));
        textArea.setBorder(new EmptyBorder(12, 14, 12, 14));

        JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnCopy = createColorButton("📋 Copy to Clipboard", new Color(79, 70, 229), Color.WHITE, new Color(67, 56, 202));
        JButton btnClose = createColorButton("Close", new Color(148, 163, 184), Color.WHITE, new Color(100, 116, 139));

        btnCopy.addActionListener(e -> {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(reportText), null);
            JOptionPane.showMessageDialog(dialog, "Report successfully copied to clipboard!", "Copied", JOptionPane.INFORMATION_MESSAGE);
        });

        btnClose.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCopy);
        btnPanel.add(btnClose);

        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private ArrayList<Double> parseMarksInput(Component parent, String input) {
        String[] tokens = input.split("[,\\s]+");
        ArrayList<Double> marks = new ArrayList<>();

        for (String token : tokens) {
            if (token.isEmpty()) continue;
            try {
                double mark = Double.parseDouble(token);
                if (mark < 0.0 || mark > 100.0) {
                    JOptionPane.showMessageDialog(parent, "Mark value '" + token + "' must be between 0.0 and 100.0.", "Invalid Mark Range", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                marks.add(mark);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parent, "Invalid numeric format: '" + token + "'. Please enter valid numbers.", "Format Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        if (marks.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter at least one valid mark.", "No Marks", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return marks;
    }

    // =========================================================================
    // VISUAL GRADE DISTRIBUTION CHART COMPONENT
    // =========================================================================

    /**
     * A colorful sidebar widget displaying live graphical progress bars for each grade.
     */
    private static class GradeDistributionPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private int[] distribution = new int[5]; // [A, B, C, D, F]
        private int totalStudents = 0;

        private final String[] gradeNames = {"Grade A (90-100%)", "Grade B (80-89%)", "Grade C (70-79%)", "Grade D (60-69%)", "Grade F (< 60%)"};
        private final Color[] barColors = {
                new Color(16, 185, 129), // Emerald A
                new Color(59, 130, 246), // Blue B
                new Color(245, 158, 11), // Amber C
                new Color(249, 115, 22), // Orange D
                new Color(239, 68, 68)   // Red F
        };
        private final Color[] bgColors = {
                new Color(209, 250, 229),
                new Color(219, 234, 254),
                new Color(254, 243, 199),
                new Color(255, 237, 213),
                new Color(254, 226, 226)
        };

        public GradeDistributionPanel() {
            setBackground(Color.WHITE);
            setBorder(new CompoundBorder(
                    new LineBorder(new Color(226, 232, 240), 1, true),
                    new EmptyBorder(16, 16, 16, 16)
            ));
        }

        public void updateDistribution(GradeTracker tracker) {
            this.distribution = tracker.getGradeDistribution();
            this.totalStudents = tracker.getStudentCount();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth() - 32;

            // Title
            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
            g2.setColor(new Color(30, 41, 59));
            g2.drawString("📊 Grade Breakdown", 16, 28);

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.setColor(new Color(100, 116, 139));
            g2.drawString(totalStudents + " total students evaluated", 16, 46);

            int startY = 70;
            int barHeight = 16;
            int spacing = 50;

            for (int i = 0; i < 5; i++) {
                int count = distribution[i];
                double percentage = totalStudents > 0 ? ((double) count / totalStudents) * 100.0 : 0.0;

                // Label and count badge
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.setColor(new Color(51, 65, 85));
                g2.drawString(gradeNames[i], 16, startY + (i * spacing));

                String countStr = String.format("%d (%.0f%%)", count, percentage);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.setColor(barColors[i]);
                int strWidth = g2.getFontMetrics().stringWidth(countStr);
                g2.drawString(countStr, getWidth() - 16 - strWidth, startY + (i * spacing));

                // Background Bar
                int barY = startY + (i * spacing) + 8;
                g2.setColor(bgColors[i]);
                g2.fill(new RoundRectangle2D.Float(16, barY, width, barHeight, 8, 8));

                // Foreground Progress Bar
                if (totalStudents > 0 && count > 0) {
                    int fillWidth = (int) Math.max(12, (percentage / 100.0) * width);
                    g2.setColor(barColors[i]);
                    g2.fill(new RoundRectangle2D.Float(16, barY, fillWidth, barHeight, 8, 8));
                }
            }

            g2.dispose();
        }
    }

    /**
     * Application entry point for GUI mode.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {}

        SwingUtilities.invokeLater(() -> {
            GradeTrackerGUI gui = new GradeTrackerGUI();
            gui.setVisible(true);
        });
    }
}
