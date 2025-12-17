/*
 * SEE (Semester End Examination) Result Management System
 * Complete Java Swing Application
 * 
 * To run: Compile and run this file in any Java IDE (IntelliJ, Eclipse, NetBeans)
 * or use: javac SEEResultManagement.java && java SEEResultManagement
 */

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.DecimalFormat;
import java.io.*;

public class SEEResultManagement extends JFrame {
    
    // Data Storage
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Subject> subjects = new ArrayList<>();
    private ArrayList<Result> results = new ArrayList<>();
    private ArrayList<String> semesters = new ArrayList<>();
    private ArrayList<String> batches = new ArrayList<>();
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel dashboardPanel, studentPanel, subjectPanel, resultPanel, reportPanel;
    
    // Tables
    private JTable studentTable, subjectTable, resultTable;
    private DefaultTableModel studentTableModel, subjectTableModel, resultTableModel;
    
    // Dashboard Labels
    private JLabel totalStudentsLabel, totalSubjectsLabel, totalResultsLabel, passRateLabel;
    
    // Colors
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    
    // Counter for IDs
    private int studentIdCounter = 1000;
    private int subjectIdCounter = 100;
    
    public SEEResultManagement() {
        initializeSampleData();
        initializeUI();
    }
    
    private void initializeSampleData() {
        // Initialize semesters
        for (int i = 1; i <= 8; i++) {
            semesters.add("Semester " + i);
        }
        
        // Initialize batches
        batches.add("2020-2024");
        batches.add("2021-2025");
        batches.add("2022-2026");
        batches.add("2023-2027");
        
        // Sample subjects
        subjects.add(new Subject(101, "Mathematics I", "MTH101", 4, "Semester 1"));
        subjects.add(new Subject(102, "Physics", "PHY101", 4, "Semester 1"));
        subjects.add(new Subject(103, "Programming in C", "CSE101", 3, "Semester 1"));
        subjects.add(new Subject(104, "English", "ENG101", 2, "Semester 1"));
        subjects.add(new Subject(105, "Mathematics II", "MTH102", 4, "Semester 2"));
        subjects.add(new Subject(106, "Data Structures", "CSE201", 4, "Semester 2"));
        
        // Sample students
        students.add(new Student(1001, "John Smith", "john@email.com", "9876543210", "2021-2025", "A"));
        students.add(new Student(1002, "Emma Wilson", "emma@email.com", "9876543211", "2021-2025", "A"));
        students.add(new Student(1003, "Michael Brown", "michael@email.com", "9876543212", "2021-2025", "B"));
        students.add(new Student(1004, "Sarah Davis", "sarah@email.com", "9876543213", "2022-2026", "A"));
        students.add(new Student(1005, "James Johnson", "james@email.com", "9876543214", "2022-2026", "A"));
        
        studentIdCounter = 1006;
        subjectIdCounter = 107;
        
        // Sample results
        results.add(new Result(1001, 101, "Semester 1", 85, 78));
        results.add(new Result(1001, 102, "Semester 1", 72, 68));
        results.add(new Result(1001, 103, "Semester 1", 90, 85));
        results.add(new Result(1002, 101, "Semester 1", 78, 82));
        results.add(new Result(1002, 102, "Semester 1", 65, 70));
        results.add(new Result(1003, 101, "Semester 1", 45, 38));
        results.add(new Result(1003, 102, "Semester 1", 52, 48));
    }
    
    private void initializeUI() {
        setTitle("SEE Result Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);
        
        // Create panels
        dashboardPanel = createDashboardPanel();
        studentPanel = createStudentPanel();
        subjectPanel = createSubjectPanel();
        resultPanel = createResultPanel();
        reportPanel = createReportPanel();
        
        // Add tabs with icons
        tabbedPane.addTab("📊 Dashboard", dashboardPanel);
        tabbedPane.addTab("👥 Students", studentPanel);
        tabbedPane.addTab("📚 Subjects", subjectPanel);
        tabbedPane.addTab("📝 Results", resultPanel);
        tabbedPane.addTab("📈 Reports", reportPanel);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        updateDashboard();
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setPreferredSize(new Dimension(0, 60));
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("SEE Result Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Semester End Examination Portal");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(200, 220, 240));
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        panel.add(titlePanel, BorderLayout.WEST);
        
        return panel;
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(SECONDARY_COLOR);
        panel.setPreferredSize(new Dimension(0, 30));
        
        JLabel footerLabel = new JLabel("© 2024 SEE Result Management System | Exam Coordinator Portal");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        panel.add(footerLabel);
        return panel;
    }
    
    // ==================== DASHBOARD PANEL ====================
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Stats Cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);
        
        totalStudentsLabel = new JLabel("0");
        totalSubjectsLabel = new JLabel("0");
        totalResultsLabel = new JLabel("0");
        passRateLabel = new JLabel("0%");
        
        statsPanel.add(createStatCard("Total Students", totalStudentsLabel, "👥", PRIMARY_COLOR));
        statsPanel.add(createStatCard("Total Subjects", totalSubjectsLabel, "📚", SUCCESS_COLOR));
        statsPanel.add(createStatCard("Results Entered", totalResultsLabel, "📝", WARNING_COLOR));
        statsPanel.add(createStatCard("Pass Rate", passRateLabel, "📈", new Color(155, 89, 182)));
        
        panel.add(statsPanel, BorderLayout.NORTH);
        
        // Recent Activity
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Quick Actions
        JPanel actionsPanel = new JPanel(new BorderLayout());
        actionsPanel.setBackground(Color.WHITE);
        actionsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel actionsTitle = new JLabel("Quick Actions");
        actionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        actionsPanel.add(actionsTitle, BorderLayout.NORTH);
        
        JPanel buttonsPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JButton addStudentBtn = createActionButton("➕ Add New Student", PRIMARY_COLOR);
        JButton enterResultBtn = createActionButton("📝 Enter Results", SUCCESS_COLOR);
        JButton viewReportBtn = createActionButton("📊 Generate Report", WARNING_COLOR);
        JButton exportBtn = createActionButton("💾 Export Data", SECONDARY_COLOR);
        
        addStudentBtn.addActionListener(e -> { tabbedPane.setSelectedIndex(1); showAddStudentDialog(); });
        enterResultBtn.addActionListener(e -> tabbedPane.setSelectedIndex(3));
        viewReportBtn.addActionListener(e -> tabbedPane.setSelectedIndex(4));
        exportBtn.addActionListener(e -> exportResults());
        
        buttonsPanel.add(addStudentBtn);
        buttonsPanel.add(enterResultBtn);
        buttonsPanel.add(viewReportBtn);
        buttonsPanel.add(exportBtn);
        
        actionsPanel.add(buttonsPanel, BorderLayout.CENTER);
        
        // Semester Overview
        JPanel overviewPanel = new JPanel(new BorderLayout());
        overviewPanel.setBackground(Color.WHITE);
        overviewPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel overviewTitle = new JLabel("Semester Overview");
        overviewTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        overviewPanel.add(overviewTitle, BorderLayout.NORTH);
        
        JTextArea overviewText = new JTextArea();
        overviewText.setEditable(false);
        overviewText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        overviewText.setLineWrap(true);
        overviewText.setWrapStyleWord(true);
        overviewText.setText(getOverviewText());
        overviewText.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        overviewPanel.add(new JScrollPane(overviewText), BorderLayout.CENTER);
        
        centerPanel.add(actionsPanel);
        centerPanel.add(overviewPanel);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatCard(String title, JLabel valueLabel, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(Color.GRAY);
        
        textPanel.add(valueLabel);
        textPanel.add(titleLabel);
        
        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 40));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private String getOverviewText() {
        StringBuilder sb = new StringBuilder();
        sb.append("System Statistics:\n\n");
        sb.append("• Active Batches: ").append(batches.size()).append("\n");
        sb.append("• Registered Subjects: ").append(subjects.size()).append("\n");
        sb.append("• Total Students: ").append(students.size()).append("\n");
        sb.append("• Results Recorded: ").append(results.size()).append("\n\n");
        sb.append("Recent Updates:\n");
        sb.append("• System initialized with sample data\n");
        sb.append("• Ready for result entry\n");
        return sb.toString();
    }
    
    // ==================== STUDENT PANEL ====================
    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setOpaque(false);
        
        JButton addBtn = createActionButton("➕ Add Student", SUCCESS_COLOR);
        JButton editBtn = createActionButton("✏️ Edit", PRIMARY_COLOR);
        JButton deleteBtn = createActionButton("🗑️ Delete", DANGER_COLOR);
        JButton refreshBtn = createActionButton("🔄 Refresh", SECONDARY_COLOR);
        
        addBtn.setPreferredSize(new Dimension(140, 35));
        editBtn.setPreferredSize(new Dimension(100, 35));
        deleteBtn.setPreferredSize(new Dimension(100, 35));
        refreshBtn.setPreferredSize(new Dimension(110, 35));
        
        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);
        toolbar.add(refreshBtn);
        
        // Search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JTextField searchField = new JTextField(20);
        searchField.putClientProperty("JTextField.placeholderText", "Search students...");
        JButton searchBtn = createActionButton("🔍 Search", PRIMARY_COLOR);
        searchBtn.setPreferredSize(new Dimension(100, 35));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(toolbar, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Name", "Email", "Phone", "Batch", "Section"};
        studentTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(studentTableModel);
        styleTable(studentTable);
        
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Actions
        addBtn.addActionListener(e -> showAddStudentDialog());
        editBtn.addActionListener(e -> showEditStudentDialog());
        deleteBtn.addActionListener(e -> deleteSelectedStudent());
        refreshBtn.addActionListener(e -> refreshStudentTable());
        searchBtn.addActionListener(e -> searchStudents(searchField.getText()));
        
        refreshStudentTable();
        
        return panel;
    }
    
    private void showAddStudentDialog() {
        JDialog dialog = new JDialog(this, "Add New Student", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JComboBox<String> batchCombo = new JComboBox<>(batches.toArray(new String[0]));
        JComboBox<String> sectionCombo = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Batch:"), gbc);
        gbc.gridx = 1;
        panel.add(batchCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Section:"), gbc);
        gbc.gridx = 1;
        panel.add(sectionCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = createActionButton("Save", SUCCESS_COLOR);
        JButton cancelBtn = createActionButton("Cancel", SECONDARY_COLOR);
        saveBtn.setPreferredSize(new Dimension(100, 35));
        cancelBtn.setPreferredSize(new Dimension(100, 35));
        
        saveBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Student student = new Student(
                studentIdCounter++,
                nameField.getText().trim(),
                emailField.getText().trim(),
                phoneField.getText().trim(),
                (String) batchCombo.getSelectedItem(),
                (String) sectionCombo.getSelectedItem()
            );
            students.add(student);
            refreshStudentTable();
            updateDashboard();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showEditStudentDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a student to edit!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int studentId = (int) studentTableModel.getValueAt(selectedRow, 0);
        Student student = findStudentById(studentId);
        if (student == null) return;
        
        JDialog dialog = new JDialog(this, "Edit Student", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField nameField = new JTextField(student.name, 20);
        JTextField emailField = new JTextField(student.email, 20);
        JTextField phoneField = new JTextField(student.phone, 20);
        JComboBox<String> batchCombo = new JComboBox<>(batches.toArray(new String[0]));
        batchCombo.setSelectedItem(student.batch);
        JComboBox<String> sectionCombo = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        sectionCombo.setSelectedItem(student.section);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Batch:"), gbc);
        gbc.gridx = 1;
        panel.add(batchCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Section:"), gbc);
        gbc.gridx = 1;
        panel.add(sectionCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = createActionButton("Update", SUCCESS_COLOR);
        JButton cancelBtn = createActionButton("Cancel", SECONDARY_COLOR);
        saveBtn.setPreferredSize(new Dimension(100, 35));
        cancelBtn.setPreferredSize(new Dimension(100, 35));
        
        saveBtn.addActionListener(e -> {
            student.name = nameField.getText().trim();
            student.email = emailField.getText().trim();
            student.phone = phoneField.getText().trim();
            student.batch = (String) batchCombo.getSelectedItem();
            student.section = (String) sectionCombo.getSelectedItem();
            refreshStudentTable();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void deleteSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this student?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int studentId = (int) studentTableModel.getValueAt(selectedRow, 0);
            students.removeIf(s -> s.id == studentId);
            results.removeIf(r -> r.studentId == studentId);
            refreshStudentTable();
            updateDashboard();
            JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void refreshStudentTable() {
        studentTableModel.setRowCount(0);
        for (Student s : students) {
            studentTableModel.addRow(new Object[]{s.id, s.name, s.email, s.phone, s.batch, s.section});
        }
    }
    
    private void searchStudents(String query) {
        if (query.trim().isEmpty()) {
            refreshStudentTable();
            return;
        }
        studentTableModel.setRowCount(0);
        String lowerQuery = query.toLowerCase();
        for (Student s : students) {
            if (s.name.toLowerCase().contains(lowerQuery) || 
                s.email.toLowerCase().contains(lowerQuery) ||
                String.valueOf(s.id).contains(lowerQuery)) {
                studentTableModel.addRow(new Object[]{s.id, s.name, s.email, s.phone, s.batch, s.section});
            }
        }
    }
    
    // ==================== SUBJECT PANEL ====================
    private JPanel createSubjectPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setOpaque(false);
        
        JButton addBtn = createActionButton("➕ Add Subject", SUCCESS_COLOR);
        JButton editBtn = createActionButton("✏️ Edit", PRIMARY_COLOR);
        JButton deleteBtn = createActionButton("🗑️ Delete", DANGER_COLOR);
        
        addBtn.setPreferredSize(new Dimension(140, 35));
        editBtn.setPreferredSize(new Dimension(100, 35));
        deleteBtn.setPreferredSize(new Dimension(100, 35));
        
        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);
        
        // Filter by semester
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Filter by Semester:"));
        JComboBox<String> semesterFilter = new JComboBox<>();
        semesterFilter.addItem("All Semesters");
        for (String sem : semesters) {
            semesterFilter.addItem(sem);
        }
        filterPanel.add(semesterFilter);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(toolbar, BorderLayout.WEST);
        topPanel.add(filterPanel, BorderLayout.EAST);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Code", "Subject Name", "Subject Code", "Credits", "Semester"};
        subjectTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        subjectTable = new JTable(subjectTableModel);
        styleTable(subjectTable);
        
        JScrollPane scrollPane = new JScrollPane(subjectTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Actions
        addBtn.addActionListener(e -> showAddSubjectDialog());
        editBtn.addActionListener(e -> showEditSubjectDialog());
        deleteBtn.addActionListener(e -> deleteSelectedSubject());
        semesterFilter.addActionListener(e -> filterSubjectsBySemester((String) semesterFilter.getSelectedItem()));
        
        refreshSubjectTable();
        
        return panel;
    }
    
    private void showAddSubjectDialog() {
        JDialog dialog = new JDialog(this, "Add New Subject", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField nameField = new JTextField(20);
        JTextField codeField = new JTextField(20);
        JSpinner creditsSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 6, 1));
        JComboBox<String> semesterCombo = new JComboBox<>(semesters.toArray(new String[0]));
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Subject Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Subject Code:"), gbc);
        gbc.gridx = 1;
        panel.add(codeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Credits:"), gbc);
        gbc.gridx = 1;
        panel.add(creditsSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Semester:"), gbc);
        gbc.gridx = 1;
        panel.add(semesterCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = createActionButton("Save", SUCCESS_COLOR);
        JButton cancelBtn = createActionButton("Cancel", SECONDARY_COLOR);
        saveBtn.setPreferredSize(new Dimension(100, 35));
        cancelBtn.setPreferredSize(new Dimension(100, 35));
        
        saveBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty() || codeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Subject subject = new Subject(
                subjectIdCounter++,
                nameField.getText().trim(),
                codeField.getText().trim(),
                (int) creditsSpinner.getValue(),
                (String) semesterCombo.getSelectedItem()
            );
            subjects.add(subject);
            refreshSubjectTable();
            updateDashboard();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Subject added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showEditSubjectDialog() {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a subject to edit!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int subjectId = (int) subjectTableModel.getValueAt(selectedRow, 0);
        Subject subject = findSubjectById(subjectId);
        if (subject == null) return;
        
        JDialog dialog = new JDialog(this, "Edit Subject", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField nameField = new JTextField(subject.name, 20);
        JTextField codeField = new JTextField(subject.code, 20);
        JSpinner creditsSpinner = new JSpinner(new SpinnerNumberModel(subject.credits, 1, 6, 1));
        JComboBox<String> semesterCombo = new JComboBox<>(semesters.toArray(new String[0]));
        semesterCombo.setSelectedItem(subject.semester);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Subject Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Subject Code:"), gbc);
        gbc.gridx = 1;
        panel.add(codeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Credits:"), gbc);
        gbc.gridx = 1;
        panel.add(creditsSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Semester:"), gbc);
        gbc.gridx = 1;
        panel.add(semesterCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = createActionButton("Update", SUCCESS_COLOR);
        JButton cancelBtn = createActionButton("Cancel", SECONDARY_COLOR);
        saveBtn.setPreferredSize(new Dimension(100, 35));
        cancelBtn.setPreferredSize(new Dimension(100, 35));
        
        saveBtn.addActionListener(e -> {
            subject.name = nameField.getText().trim();
            subject.code = codeField.getText().trim();
            subject.credits = (int) creditsSpinner.getValue();
            subject.semester = (String) semesterCombo.getSelectedItem();
            refreshSubjectTable();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Subject updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void deleteSelectedSubject() {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a subject to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this subject? All related results will also be deleted.", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int subjectId = (int) subjectTableModel.getValueAt(selectedRow, 0);
            subjects.removeIf(s -> s.id == subjectId);
            results.removeIf(r -> r.subjectId == subjectId);
            refreshSubjectTable();
            updateDashboard();
            JOptionPane.showMessageDialog(this, "Subject deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void refreshSubjectTable() {
        subjectTableModel.setRowCount(0);
        for (Subject s : subjects) {
            subjectTableModel.addRow(new Object[]{s.id, s.name, s.code, s.credits, s.semester});
        }
    }
    
    private void filterSubjectsBySemester(String semester) {
        subjectTableModel.setRowCount(0);
        for (Subject s : subjects) {
            if (semester.equals("All Semesters") || s.semester.equals(semester)) {
                subjectTableModel.addRow(new Object[]{s.id, s.name, s.code, s.credits, s.semester});
            }
        }
    }
    
    // ==================== RESULT PANEL ====================
    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Top section with filters and entry
        JPanel topSection = new JPanel(new BorderLayout(10, 10));
        topSection.setOpaque(false);
        
        // Entry Form
        JPanel entryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        entryPanel.setBackground(Color.WHITE);
        entryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Enter Result"),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JComboBox<String> studentCombo = new JComboBox<>();
        JComboBox<String> subjectCombo = new JComboBox<>();
        JComboBox<String> semesterCombo = new JComboBox<>(semesters.toArray(new String[0]));
        JSpinner internalSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 40, 1));
        JSpinner externalSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 60, 1));
        
        // Populate combos
        for (Student s : students) {
            studentCombo.addItem(s.id + " - " + s.name);
        }
        for (Subject s : subjects) {
            subjectCombo.addItem(s.id + " - " + s.name);
        }
        
        entryPanel.add(new JLabel("Student:"));
        entryPanel.add(studentCombo);
        entryPanel.add(new JLabel("Subject:"));
        entryPanel.add(subjectCombo);
        entryPanel.add(new JLabel("Semester:"));
        entryPanel.add(semesterCombo);
        entryPanel.add(new JLabel("Internal (40):"));
        entryPanel.add(internalSpinner);
        entryPanel.add(new JLabel("External (60):"));
        entryPanel.add(externalSpinner);
        
        JButton saveResultBtn = createActionButton("💾 Save Result", SUCCESS_COLOR);
        saveResultBtn.setPreferredSize(new Dimension(130, 30));
        entryPanel.add(saveResultBtn);
        
        topSection.add(entryPanel, BorderLayout.CENTER);
        
        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Filter:"));
        
        JComboBox<String> filterSemester = new JComboBox<>();
        filterSemester.addItem("All Semesters");
        for (String sem : semesters) {
            filterSemester.addItem(sem);
        }
        
        JComboBox<String> filterBatch = new JComboBox<>();
        filterBatch.addItem("All Batches");
        for (String batch : batches) {
            filterBatch.addItem(batch);
        }
        
        filterPanel.add(new JLabel("Semester:"));
        filterPanel.add(filterSemester);
        filterPanel.add(new JLabel("Batch:"));
        filterPanel.add(filterBatch);
        
        JButton applyFilterBtn = createActionButton("Apply Filter", PRIMARY_COLOR);
        applyFilterBtn.setPreferredSize(new Dimension(120, 30));
        filterPanel.add(applyFilterBtn);
        
        topSection.add(filterPanel, BorderLayout.SOUTH);
        
        panel.add(topSection, BorderLayout.NORTH);
        
        // Results Table
        String[] columns = {"Student ID", "Student Name", "Subject", "Semester", "Internal", "External", "Total", "Grade", "Status"};
        resultTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable = new JTable(resultTableModel);
        styleTable(resultTable);
        
        // Custom renderer for status
        resultTable.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if ("PASS".equals(value)) {
                    c.setForeground(SUCCESS_COLOR);
                } else {
                    c.setForeground(DANGER_COLOR);
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom toolbar
        JPanel bottomToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        bottomToolbar.setOpaque(false);
        
        JButton editResultBtn = createActionButton("✏️ Edit Result", PRIMARY_COLOR);
        JButton deleteResultBtn = createActionButton("🗑️ Delete Result", DANGER_COLOR);
        JButton bulkEntryBtn = createActionButton("📋 Bulk Entry", WARNING_COLOR);
        
        editResultBtn.setPreferredSize(new Dimension(130, 35));
        deleteResultBtn.setPreferredSize(new Dimension(140, 35));
        bulkEntryBtn.setPreferredSize(new Dimension(120, 35));
        
        bottomToolbar.add(editResultBtn);
        bottomToolbar.add(deleteResultBtn);
        bottomToolbar.add(bulkEntryBtn);
        
        panel.add(bottomToolbar, BorderLayout.SOUTH);
        
        // Actions
        saveResultBtn.addActionListener(e -> {
            if (studentCombo.getSelectedItem() == null || subjectCombo.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Please select student and subject!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String studentStr = (String) studentCombo.getSelectedItem();
            String subjectStr = (String) subjectCombo.getSelectedItem();
            int studentId = Integer.parseInt(studentStr.split(" - ")[0]);
            int subjectId = Integer.parseInt(subjectStr.split(" - ")[0]);
            String semester = (String) semesterCombo.getSelectedItem();
            int internal = (int) internalSpinner.getValue();
            int external = (int) externalSpinner.getValue();
            
            // Check if result already exists
            boolean exists = results.stream().anyMatch(r -> 
                r.studentId == studentId && r.subjectId == subjectId && r.semester.equals(semester));
            
            if (exists) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Result already exists. Update it?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    results.removeIf(r -> r.studentId == studentId && r.subjectId == subjectId && r.semester.equals(semester));
                } else {
                    return;
                }
            }
            
            Result result = new Result(studentId, subjectId, semester, internal, external);
            results.add(result);
            refreshResultTable(null, null);
            updateDashboard();
            JOptionPane.showMessageDialog(this, "Result saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        
        applyFilterBtn.addActionListener(e -> {
            String sem = filterSemester.getSelectedItem().equals("All Semesters") ? null : (String) filterSemester.getSelectedItem();
            String batch = filterBatch.getSelectedItem().equals("All Batches") ? null : (String) filterBatch.getSelectedItem();
            refreshResultTable(sem, batch);
        });
        
        deleteResultBtn.addActionListener(e -> {
            int selectedRow = resultTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a result to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this result?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int studentId = (int) resultTableModel.getValueAt(selectedRow, 0);
                String subjectName = (String) resultTableModel.getValueAt(selectedRow, 2);
                String semester = (String) resultTableModel.getValueAt(selectedRow, 3);
                
                Subject subject = subjects.stream().filter(s -> s.name.equals(subjectName)).findFirst().orElse(null);
                if (subject != null) {
                    results.removeIf(r -> r.studentId == studentId && r.subjectId == subject.id && r.semester.equals(semester));
                    refreshResultTable(null, null);
                    updateDashboard();
                    JOptionPane.showMessageDialog(this, "Result deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        bulkEntryBtn.addActionListener(e -> showBulkEntryDialog());
        
        refreshResultTable(null, null);
        
        return panel;
    }
    
    private void showBulkEntryDialog() {
        JDialog dialog = new JDialog(this, "Bulk Result Entry", true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Selection panel
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        selectionPanel.add(new JLabel("Semester:"));
        JComboBox<String> semCombo = new JComboBox<>(semesters.toArray(new String[0]));
        selectionPanel.add(semCombo);
        selectionPanel.add(new JLabel("Subject:"));
        JComboBox<String> subCombo = new JComboBox<>();
        for (Subject s : subjects) {
            subCombo.addItem(s.id + " - " + s.name);
        }
        selectionPanel.add(subCombo);
        
        JButton loadBtn = createActionButton("Load Students", PRIMARY_COLOR);
        loadBtn.setPreferredSize(new Dimension(130, 30));
        selectionPanel.add(loadBtn);
        
        panel.add(selectionPanel, BorderLayout.NORTH);
        
        // Entry table
        String[] cols = {"Student ID", "Student Name", "Internal (40)", "External (60)"};
        DefaultTableModel bulkModel = new DefaultTableModel(cols, 0);
        JTable bulkTable = new JTable(bulkModel);
        styleTable(bulkTable);
        
        // Make internal and external editable
        bulkTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JTextField()));
        bulkTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JTextField()));
        
        panel.add(new JScrollPane(bulkTable), BorderLayout.CENTER);
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveAllBtn = createActionButton("Save All Results", SUCCESS_COLOR);
        JButton cancelBtn = createActionButton("Cancel", SECONDARY_COLOR);
        saveAllBtn.setPreferredSize(new Dimension(150, 35));
        cancelBtn.setPreferredSize(new Dimension(100, 35));
        btnPanel.add(saveAllBtn);
        btnPanel.add(cancelBtn);
        
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        loadBtn.addActionListener(e -> {
            bulkModel.setRowCount(0);
            for (Student s : students) {
                bulkModel.addRow(new Object[]{s.id, s.name, 0, 0});
            }
        });
        
        saveAllBtn.addActionListener(e -> {
            if (bulkModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(dialog, "No students loaded!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String semester = (String) semCombo.getSelectedItem();
            String subjectStr = (String) subCombo.getSelectedItem();
            int subjectId = Integer.parseInt(subjectStr.split(" - ")[0]);
            
            int saved = 0;
            for (int i = 0; i < bulkModel.getRowCount(); i++) {
                int studentId = (int) bulkModel.getValueAt(i, 0);
                int internal = Integer.parseInt(bulkModel.getValueAt(i, 2).toString());
                int external = Integer.parseInt(bulkModel.getValueAt(i, 3).toString());
                
                if (internal > 0 || external > 0) {
                    results.removeIf(r -> r.studentId == studentId && r.subjectId == subjectId && r.semester.equals(semester));
                    results.add(new Result(studentId, subjectId, semester, internal, external));
                    saved++;
                }
            }
            
            refreshResultTable(null, null);
            updateDashboard();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, saved + " results saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void refreshResultTable(String filterSemester, String filterBatch) {
        resultTableModel.setRowCount(0);
        for (Result r : results) {
            Student student = findStudentById(r.studentId);
            Subject subject = findSubjectById(r.subjectId);
            
            if (student == null || subject == null) continue;
            
            if (filterSemester != null && !r.semester.equals(filterSemester)) continue;
            if (filterBatch != null && !student.batch.equals(filterBatch)) continue;
            
            int total = r.internalMarks + r.externalMarks;
            String grade = calculateGrade(total);
            String status = (r.internalMarks >= 16 && r.externalMarks >= 24) ? "PASS" : "FAIL";
            
            resultTableModel.addRow(new Object[]{
                r.studentId, student.name, subject.name, r.semester,
                r.internalMarks, r.externalMarks, total, grade, status
            });
        }
    }
    
    // ==================== REPORT PANEL ====================
    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Report selection
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        selectionPanel.setBackground(Color.WHITE);
        selectionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Generate Report"),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JComboBox<String> reportType = new JComboBox<>(new String[]{
            "Individual Student Result",
            "Class Result - Semester Wise",
            "Subject Analysis",
            "Pass/Fail Statistics",
            "Top Performers"
        });
        
        JComboBox<String> semesterCombo = new JComboBox<>(semesters.toArray(new String[0]));
        JComboBox<String> studentCombo = new JComboBox<>();
        for (Student s : students) {
            studentCombo.addItem(s.id + " - " + s.name);
        }
        
        JComboBox<String> batchCombo = new JComboBox<>();
        batchCombo.addItem("All Batches");
        for (String batch : batches) {
            batchCombo.addItem(batch);
        }
        
        selectionPanel.add(new JLabel("Report Type:"));
        selectionPanel.add(reportType);
        selectionPanel.add(new JLabel("Semester:"));
        selectionPanel.add(semesterCombo);
        selectionPanel.add(new JLabel("Student:"));
        selectionPanel.add(studentCombo);
        selectionPanel.add(new JLabel("Batch:"));
        selectionPanel.add(batchCombo);
        
        JButton generateBtn = createActionButton("📊 Generate Report", PRIMARY_COLOR);
        JButton printBtn = createActionButton("🖨️ Print", SECONDARY_COLOR);
        JButton exportBtn = createActionButton("💾 Export", SUCCESS_COLOR);
        
        generateBtn.setPreferredSize(new Dimension(150, 35));
        printBtn.setPreferredSize(new Dimension(100, 35));
        exportBtn.setPreferredSize(new Dimension(100, 35));
        
        selectionPanel.add(generateBtn);
        selectionPanel.add(printBtn);
        selectionPanel.add(exportBtn);
        
        panel.add(selectionPanel, BorderLayout.NORTH);
        
        // Report display area
        JTextArea reportArea = new JTextArea();
        reportArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        reportArea.setEditable(false);
        reportArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Report Output"),
            BorderFactory.createLineBorder(new Color(220, 220, 220))
        ));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Actions
        generateBtn.addActionListener(e -> {
            String type = (String) reportType.getSelectedItem();
            String semester = (String) semesterCombo.getSelectedItem();
            String batch = (String) batchCombo.getSelectedItem();
            String studentStr = (String) studentCombo.getSelectedItem();
            
            StringBuilder report = new StringBuilder();
            report.append("═".repeat(70)).append("\n");
            report.append("              SEE RESULT MANAGEMENT SYSTEM\n");
            report.append("═".repeat(70)).append("\n\n");
            
            switch (type) {
                case "Individual Student Result":
                    if (studentStr != null) {
                        int studentId = Integer.parseInt(studentStr.split(" - ")[0]);
                        report.append(generateIndividualReport(studentId, semester));
                    }
                    break;
                case "Class Result - Semester Wise":
                    report.append(generateClassReport(semester, batch.equals("All Batches") ? null : batch));
                    break;
                case "Subject Analysis":
                    report.append(generateSubjectAnalysis(semester));
                    break;
                case "Pass/Fail Statistics":
                    report.append(generatePassFailStats(semester, batch.equals("All Batches") ? null : batch));
                    break;
                case "Top Performers":
                    report.append(generateTopPerformers(semester, 10));
                    break;
            }
            
            report.append("\n").append("═".repeat(70)).append("\n");
            report.append("Report Generated: ").append(new java.util.Date()).append("\n");
            
            reportArea.setText(report.toString());
        });
        
        printBtn.addActionListener(e -> {
            try {
                reportArea.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Print error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        exportBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("report.txt"));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                    writer.print(reportArea.getText());
                    JOptionPane.showMessageDialog(this, "Report exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Export error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        return panel;
    }
    
    private String generateIndividualReport(int studentId, String semester) {
        Student student = findStudentById(studentId);
        if (student == null) return "Student not found!";
        
        StringBuilder sb = new StringBuilder();
        sb.append("INDIVIDUAL MARKSHEET\n");
        sb.append("─".repeat(50)).append("\n\n");
        sb.append(String.format("Student ID   : %d\n", student.id));
        sb.append(String.format("Name         : %s\n", student.name));
        sb.append(String.format("Batch        : %s\n", student.batch));
        sb.append(String.format("Section      : %s\n", student.section));
        sb.append(String.format("Semester     : %s\n\n", semester));
        
        sb.append("─".repeat(70)).append("\n");
        sb.append(String.format("%-25s %10s %10s %10s %10s\n", "Subject", "Internal", "External", "Total", "Grade"));
        sb.append("─".repeat(70)).append("\n");
        
        int totalMarks = 0;
        int totalSubjects = 0;
        int passedSubjects = 0;
        
        for (Result r : results) {
            if (r.studentId == studentId && r.semester.equals(semester)) {
                Subject subject = findSubjectById(r.subjectId);
                if (subject != null) {
                    int total = r.internalMarks + r.externalMarks;
                    String grade = calculateGrade(total);
                    boolean passed = r.internalMarks >= 16 && r.externalMarks >= 24;
                    
                    sb.append(String.format("%-25s %10d %10d %10d %10s\n", 
                        subject.name, r.internalMarks, r.externalMarks, total, grade));
                    
                    totalMarks += total;
                    totalSubjects++;
                    if (passed) passedSubjects++;
                }
            }
        }
        
        sb.append("─".repeat(70)).append("\n");
        if (totalSubjects > 0) {
            double percentage = (totalMarks * 100.0) / (totalSubjects * 100);
            sb.append(String.format("\nTotal Marks: %d / %d\n", totalMarks, totalSubjects * 100));
            sb.append(String.format("Percentage: %.2f%%\n", percentage));
            sb.append(String.format("Result: %s\n", passedSubjects == totalSubjects ? "PASS" : "FAIL"));
        } else {
            sb.append("\nNo results found for this semester.\n");
        }
        
        return sb.toString();
    }
    
    private String generateClassReport(String semester, String batch) {
        StringBuilder sb = new StringBuilder();
        sb.append("CLASS RESULT - ").append(semester).append("\n");
        if (batch != null) sb.append("Batch: ").append(batch).append("\n");
        sb.append("─".repeat(80)).append("\n\n");
        
        sb.append(String.format("%-6s %-20s %-10s %10s %10s %10s %10s\n", 
            "ID", "Name", "Section", "Total", "Percentage", "Grade", "Status"));
        sb.append("─".repeat(80)).append("\n");
        
        for (Student student : students) {
            if (batch != null && !student.batch.equals(batch)) continue;
            
            int totalMarks = 0;
            int totalSubjects = 0;
            int passedSubjects = 0;
            
            for (Result r : results) {
                if (r.studentId == student.id && r.semester.equals(semester)) {
                    totalMarks += r.internalMarks + r.externalMarks;
                    totalSubjects++;
                    if (r.internalMarks >= 16 && r.externalMarks >= 24) passedSubjects++;
                }
            }
            
            if (totalSubjects > 0) {
                double percentage = (totalMarks * 100.0) / (totalSubjects * 100);
                String grade = calculateGrade((int) percentage);
                String status = passedSubjects == totalSubjects ? "PASS" : "FAIL";
                
                sb.append(String.format("%-6d %-20s %-10s %10d %9.2f%% %10s %10s\n",
                    student.id, student.name, student.section, totalMarks, percentage, grade, status));
            }
        }
        
        return sb.toString();
    }
    
    private String generateSubjectAnalysis(String semester) {
        StringBuilder sb = new StringBuilder();
        sb.append("SUBJECT WISE ANALYSIS - ").append(semester).append("\n");
        sb.append("─".repeat(70)).append("\n\n");
        
        for (Subject subject : subjects) {
            if (!subject.semester.equals(semester)) continue;
            
            int totalStudents = 0;
            int passCount = 0;
            int totalMarks = 0;
            int highest = 0;
            int lowest = 100;
            
            for (Result r : results) {
                if (r.subjectId == subject.id && r.semester.equals(semester)) {
                    int total = r.internalMarks + r.externalMarks;
                    totalStudents++;
                    totalMarks += total;
                    if (r.internalMarks >= 16 && r.externalMarks >= 24) passCount++;
                    highest = Math.max(highest, total);
                    lowest = Math.min(lowest, total);
                }
            }
            
            if (totalStudents > 0) {
                sb.append(String.format("Subject: %s (%s)\n", subject.name, subject.code));
                sb.append(String.format("  Total Students: %d\n", totalStudents));
                sb.append(String.format("  Pass Count: %d (%.1f%%)\n", passCount, (passCount * 100.0) / totalStudents));
                sb.append(String.format("  Average: %.2f\n", totalMarks * 1.0 / totalStudents));
                sb.append(String.format("  Highest: %d | Lowest: %d\n\n", highest, lowest));
            }
        }
        
        return sb.toString();
    }
    
    private String generatePassFailStats(String semester, String batch) {
        StringBuilder sb = new StringBuilder();
        sb.append("PASS/FAIL STATISTICS - ").append(semester).append("\n");
        if (batch != null) sb.append("Batch: ").append(batch).append("\n");
        sb.append("─".repeat(50)).append("\n\n");
        
        int totalStudents = 0;
        int passCount = 0;
        int failCount = 0;
        
        for (Student student : students) {
            if (batch != null && !student.batch.equals(batch)) continue;
            
            boolean hasResults = false;
            boolean allPassed = true;
            
            for (Result r : results) {
                if (r.studentId == student.id && r.semester.equals(semester)) {
                    hasResults = true;
                    if (r.internalMarks < 16 || r.externalMarks < 24) {
                        allPassed = false;
                    }
                }
            }
            
            if (hasResults) {
                totalStudents++;
                if (allPassed) passCount++;
                else failCount++;
            }
        }
        
        sb.append(String.format("Total Students Appeared: %d\n", totalStudents));
        sb.append(String.format("Passed: %d (%.1f%%)\n", passCount, totalStudents > 0 ? (passCount * 100.0) / totalStudents : 0));
        sb.append(String.format("Failed: %d (%.1f%%)\n", failCount, totalStudents > 0 ? (failCount * 100.0) / totalStudents : 0));
        
        sb.append("\n");
        sb.append("Grade Distribution:\n");
        sb.append("─".repeat(30)).append("\n");
        
        Map<String, Integer> gradeCount = new HashMap<>();
        for (Result r : results) {
            if (r.semester.equals(semester)) {
                Student student = findStudentById(r.studentId);
                if (batch != null && student != null && !student.batch.equals(batch)) continue;
                
                int total = r.internalMarks + r.externalMarks;
                String grade = calculateGrade(total);
                gradeCount.put(grade, gradeCount.getOrDefault(grade, 0) + 1);
            }
        }
        
        for (String grade : new String[]{"A+", "A", "B+", "B", "C+", "C", "D", "F"}) {
            int count = gradeCount.getOrDefault(grade, 0);
            sb.append(String.format("Grade %s: %d\n", grade, count));
        }
        
        return sb.toString();
    }
    
    private String generateTopPerformers(String semester, int limit) {
        StringBuilder sb = new StringBuilder();
        sb.append("TOP PERFORMERS - ").append(semester).append("\n");
        sb.append("─".repeat(60)).append("\n\n");
        
        // Calculate total marks per student
        Map<Integer, Integer> studentMarks = new HashMap<>();
        Map<Integer, Integer> studentSubjects = new HashMap<>();
        
        for (Result r : results) {
            if (r.semester.equals(semester)) {
                int total = r.internalMarks + r.externalMarks;
                studentMarks.put(r.studentId, studentMarks.getOrDefault(r.studentId, 0) + total);
                studentSubjects.put(r.studentId, studentSubjects.getOrDefault(r.studentId, 0) + 1);
            }
        }
        
        // Sort by percentage
        java.util.List<Map.Entry<Integer, Integer>> sorted = new ArrayList<>(studentMarks.entrySet());
        sorted.sort((a, b) -> {
            double percA = (a.getValue() * 100.0) / (studentSubjects.get(a.getKey()) * 100);
            double percB = (b.getValue() * 100.0) / (studentSubjects.get(b.getKey()) * 100);
            return Double.compare(percB, percA);
        });
        
        sb.append(String.format("%-5s %-6s %-25s %12s %10s\n", "Rank", "ID", "Name", "Percentage", "Grade"));
        sb.append("─".repeat(60)).append("\n");
        
        int rank = 1;
        for (Map.Entry<Integer, Integer> entry : sorted) {
            if (rank > limit) break;
            
            Student student = findStudentById(entry.getKey());
            if (student != null) {
                double percentage = (entry.getValue() * 100.0) / (studentSubjects.get(entry.getKey()) * 100);
                String grade = calculateGrade((int) percentage);
                
                sb.append(String.format("%-5d %-6d %-25s %11.2f%% %10s\n",
                    rank, student.id, student.name, percentage, grade));
                rank++;
            }
        }
        
        return sb.toString();
    }
    
    // ==================== UTILITY METHODS ====================
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(230, 230, 230));
        table.setShowGrid(true);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(SECONDARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 35));
    }
    
    private String calculateGrade(int marks) {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B+";
        if (marks >= 60) return "B";
        if (marks >= 50) return "C+";
        if (marks >= 45) return "C";
        if (marks >= 40) return "D";
        return "F";
    }
    
    private Student findStudentById(int id) {
        return students.stream().filter(s -> s.id == id).findFirst().orElse(null);
    }
    
    private Subject findSubjectById(int id) {
        return subjects.stream().filter(s -> s.id == id).findFirst().orElse(null);
    }
    
    private void updateDashboard() {
        totalStudentsLabel.setText(String.valueOf(students.size()));
        totalSubjectsLabel.setText(String.valueOf(subjects.size()));
        totalResultsLabel.setText(String.valueOf(results.size()));
        
        // Calculate pass rate
        int passed = 0;
        int total = 0;
        for (Result r : results) {
            total++;
            if (r.internalMarks >= 16 && r.externalMarks >= 24) passed++;
        }
        double passRate = total > 0 ? (passed * 100.0) / total : 0;
        passRateLabel.setText(String.format("%.1f%%", passRate));
    }
    
    private void exportResults() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File("results_export.csv"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                writer.println("Student ID,Student Name,Subject,Semester,Internal,External,Total,Grade,Status");
                for (Result r : results) {
                    Student student = findStudentById(r.studentId);
                    Subject subject = findSubjectById(r.subjectId);
                    if (student != null && subject != null) {
                        int total = r.internalMarks + r.externalMarks;
                        String grade = calculateGrade(total);
                        String status = (r.internalMarks >= 16 && r.externalMarks >= 24) ? "PASS" : "FAIL";
                        writer.printf("%d,%s,%s,%s,%d,%d,%d,%s,%s\n",
                            r.studentId, student.name, subject.name, r.semester,
                            r.internalMarks, r.externalMarks, total, grade, status);
                    }
                }
                JOptionPane.showMessageDialog(this, "Data exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Export error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // ==================== DATA CLASSES ====================
    static class Student {
        int id;
        String name, email, phone, batch, section;
        
        Student(int id, String name, String email, String phone, String batch, String section) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.batch = batch;
            this.section = section;
        }
    }
    
    static class Subject {
        int id, credits;
        String name, code, semester;
        
        Subject(int id, String name, String code, int credits, String semester) {
            this.id = id;
            this.name = name;
            this.code = code;
            this.credits = credits;
            this.semester = semester;
        }
    }
    
    static class Result {
        int studentId, subjectId, internalMarks, externalMarks;
        String semester;
        
        Result(int studentId, int subjectId, String semester, int internalMarks, int externalMarks) {
            this.studentId = studentId;
            this.subjectId = subjectId;
            this.semester = semester;
            this.internalMarks = internalMarks;
            this.externalMarks = externalMarks;
        }
    }
    
    // ==================== MAIN ====================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new SEEResultManagement().setVisible(true);
        });
    }
}
