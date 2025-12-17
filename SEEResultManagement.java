/*
 * SEE (Semester End Examination) Result Management System
 * Complete Java Swing Application
 *
 * To run: Compile and run this file in any Java IDE (IntelliJ, Eclipse, NetBeans)
 * or use: javac *.java && java SEEResultManagement
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

    private DataManager dataManager;
    private ReportGenerator reportGenerator;

    private JTabbedPane tabbedPane;
    private JPanel dashboardPanel, studentPanel, subjectPanel, resultPanel, reportPanel;

    private JTable studentTable, subjectTable, resultTable;
    private DefaultTableModel studentTableModel, subjectTableModel, resultTableModel;

    private JLabel totalStudentsLabel, totalSubjectsLabel, totalResultsLabel, passRateLabel;

    public SEEResultManagement() {
        dataManager = new DataManager();
        reportGenerator = new ReportGenerator(dataManager);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("SEE Result Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(AppConstants.BACKGROUND_COLOR);

        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);

        dashboardPanel = createDashboardPanel();
        studentPanel = createStudentPanel();
        subjectPanel = createSubjectPanel();
        resultPanel = createResultPanel();
        reportPanel = createReportPanel();

        tabbedPane.addTab("📊 Dashboard", dashboardPanel);
        tabbedPane.addTab("👥 Students", studentPanel);
        tabbedPane.addTab("📚 Subjects", subjectPanel);
        tabbedPane.addTab("📝 Results", resultPanel);
        tabbedPane.addTab("📈 Reports", reportPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);

        updateDashboard();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppConstants.PRIMARY_COLOR);
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
        panel.setBackground(AppConstants.SECONDARY_COLOR);
        panel.setPreferredSize(new Dimension(0, 30));

        JLabel footerLabel = new JLabel("© 2024 SEE Result Management System | Exam Coordinator Portal");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        panel.add(footerLabel);
        return panel;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppConstants.BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);

        totalStudentsLabel = new JLabel("0");
        totalSubjectsLabel = new JLabel("0");
        totalResultsLabel = new JLabel("0");
        passRateLabel = new JLabel("0%");

        statsPanel.add(createStatCard("Total Students", totalStudentsLabel, "👥", AppConstants.PRIMARY_COLOR));
        statsPanel.add(createStatCard("Total Subjects", totalSubjectsLabel, "📚", AppConstants.SUCCESS_COLOR));
        statsPanel.add(createStatCard("Results Entered", totalResultsLabel, "📝", AppConstants.WARNING_COLOR));
        statsPanel.add(createStatCard("Pass Rate", passRateLabel, "📈", new Color(155, 89, 182)));

        panel.add(statsPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

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

        JButton addStudentBtn = UIHelper.createActionButton("➕ Add New Student", AppConstants.PRIMARY_COLOR);
        JButton enterResultBtn = UIHelper.createActionButton("📝 Enter Results", AppConstants.SUCCESS_COLOR);
        JButton viewReportBtn = UIHelper.createActionButton("📊 Generate Report", AppConstants.WARNING_COLOR);
        JButton exportBtn = UIHelper.createActionButton("💾 Export Data", AppConstants.SECONDARY_COLOR);

        addStudentBtn.addActionListener(e -> { tabbedPane.setSelectedIndex(1); showAddStudentDialog(); });
        enterResultBtn.addActionListener(e -> tabbedPane.setSelectedIndex(3));
        viewReportBtn.addActionListener(e -> tabbedPane.setSelectedIndex(4));
        exportBtn.addActionListener(e -> exportResults());

        buttonsPanel.add(addStudentBtn);
        buttonsPanel.add(enterResultBtn);
        buttonsPanel.add(viewReportBtn);
        buttonsPanel.add(exportBtn);

        actionsPanel.add(buttonsPanel, BorderLayout.CENTER);

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

    private String getOverviewText() {
        StringBuilder sb = new StringBuilder();
        sb.append("System Statistics:\n\n");
        sb.append("• Active Batches: ").append(dataManager.getBatches().size()).append("\n");
        sb.append("• Registered Subjects: ").append(dataManager.getSubjects().size()).append("\n");
        sb.append("• Total Students: ").append(dataManager.getStudents().size()).append("\n");
        sb.append("• Results Recorded: ").append(dataManager.getResults().size()).append("\n\n");
        sb.append("Recent Updates:\n");
        sb.append("• System initialized with sample data\n");
        sb.append("• Ready for result entry\n");
        return sb.toString();
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppConstants.BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setOpaque(false);

        JButton addBtn = UIHelper.createActionButton("➕ Add Student", AppConstants.SUCCESS_COLOR);
        JButton editBtn = UIHelper.createActionButton("✏️ Edit", AppConstants.PRIMARY_COLOR);
        JButton deleteBtn = UIHelper.createActionButton("🗑️ Delete", AppConstants.DANGER_COLOR);
        JButton refreshBtn = UIHelper.createActionButton("🔄 Refresh", AppConstants.SECONDARY_COLOR);

        addBtn.setPreferredSize(new Dimension(140, 35));
        editBtn.setPreferredSize(new Dimension(100, 35));
        deleteBtn.setPreferredSize(new Dimension(100, 35));
        refreshBtn.setPreferredSize(new Dimension(110, 35));

        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);
        toolbar.add(refreshBtn);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JTextField searchField = new JTextField(20);
        searchField.putClientProperty("JTextField.placeholderText", "Search students...");
        JButton searchBtn = UIHelper.createActionButton("🔍 Search", AppConstants.PRIMARY_COLOR);
        searchBtn.setPreferredSize(new Dimension(100, 35));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(toolbar, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Email", "Phone", "Batch", "Section"};
        studentTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(studentTableModel);
        UIHelper.styleTable(studentTable);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(scrollPane, BorderLayout.CENTER);

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
        JComboBox<String> batchCombo = new JComboBox<>(dataManager.getBatches().toArray(new String[0]));
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
        JButton saveBtn = UIHelper.createActionButton("Save", AppConstants.SUCCESS_COLOR);
        JButton cancelBtn = UIHelper.createActionButton("Cancel", AppConstants.SECONDARY_COLOR);
        saveBtn.setPreferredSize(new Dimension(100, 35));
        cancelBtn.setPreferredSize(new Dimension(100, 35));

        saveBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Student student = new Student(
                dataManager.getNextStudentId(),
                nameField.getText().trim(),
                emailField.getText().trim(),
                phoneField.getText().trim(),
                (String) batchCombo.getSelectedItem(),
                (String) sectionCombo.getSelectedItem()
            );
            dataManager.getStudents().add(student);
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
        Student student = dataManager.findStudentById(studentId);
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
        JComboBox<String> batchCombo = new JComboBox<>(dataManager.getBatches().toArray(new String[0]));
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
        JButton saveBtn = UIHelper.createActionButton("Update", AppConstants.SUCCESS_COLOR);
        JButton cancelBtn = UIHelper.createActionButton("Cancel", AppConstants.SECONDARY_COLOR);
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
            dataManager.getStudents().removeIf(s -> s.id == studentId);
            dataManager.getResults().removeIf(r -> r.studentId == studentId);
            refreshStudentTable();
            updateDashboard();
            JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshStudentTable() {
        studentTableModel.setRowCount(0);
        for (Student s : dataManager.getStudents()) {
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
        for (Student s : dataManager.getStudents()) {
            if (s.name.toLowerCase().contains(lowerQuery) ||
                s.email.toLowerCase().contains(lowerQuery) ||
                String.valueOf(s.id).contains(lowerQuery)) {
                studentTableModel.addRow(new Object[]{s.id, s.name, s.email, s.phone, s.batch, s.section});
            }
        }
    }

    private JPanel createSubjectPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppConstants.BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setOpaque(false);

        JButton addBtn = UIHelper.createActionButton("➕ Add Subject", AppConstants.SUCCESS_COLOR);
        JButton editBtn = UIHelper.createActionButton("✏️ Edit", AppConstants.PRIMARY_COLOR);
        JButton deleteBtn = UIHelper.createActionButton("🗑️ Delete", AppConstants.DANGER_COLOR);

        addBtn.setPreferredSize(new Dimension(140, 35));
        editBtn.setPreferredSize(new Dimension(100, 35));
        deleteBtn.setPreferredSize(new Dimension(100, 35));

        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Filter by Semester:"));
        JComboBox<String> semesterFilter = new JComboBox<>();
        semesterFilter.addItem("All Semesters");
        for (String sem : dataManager.getSemesters()) {
            semesterFilter.addItem(sem);
        }
        filterPanel.add(semesterFilter);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(toolbar, BorderLayout.WEST);
        topPanel.add(filterPanel, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Code", "Subject Name", "Subject Code", "Credits", "Semester"};
        subjectTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        subjectTable = new JTable(subjectTableModel);
        UIHelper.styleTable(subjectTable);

        JScrollPane scrollPane = new JScrollPane(subjectTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(scrollPane, BorderLayout.CENTER);

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
        JComboBox<String> semesterCombo = new JComboBox<>(dataManager.getSemesters().toArray(new String[0]));

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
        JButton saveBtn = UIHelper.createActionButton("Save", AppConstants.SUCCESS_COLOR);
        JButton cancelBtn = UIHelper.createActionButton("Cancel", AppConstants.SECONDARY_COLOR);
        saveBtn.setPreferredSize(new Dimension(100, 35));
        cancelBtn.setPreferredSize(new Dimension(100, 35));

        saveBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty() || codeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Subject subject = new Subject(
                dataManager.getNextSubjectId(),
                nameField.getText().trim(),
                codeField.getText().trim(),
                (int) creditsSpinner.getValue(),
                (String) semesterCombo.getSelectedItem()
            );
            dataManager.getSubjects().add(subject);
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
        Subject subject = dataManager.findSubjectById(subjectId);
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
        JComboBox<String> semesterCombo = new JComboBox<>(dataManager.getSemesters().toArray(new String[0]));
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
        JButton saveBtn = UIHelper.createActionButton("Update", AppConstants.SUCCESS_COLOR);
        JButton cancelBtn = UIHelper.createActionButton("Cancel", AppConstants.SECONDARY_COLOR);
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
            dataManager.getSubjects().removeIf(s -> s.id == subjectId);
            dataManager.getResults().removeIf(r -> r.subjectId == subjectId);
            refreshSubjectTable();
            updateDashboard();
            JOptionPane.showMessageDialog(this, "Subject deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshSubjectTable() {
        subjectTableModel.setRowCount(0);
        for (Subject s : dataManager.getSubjects()) {
            subjectTableModel.addRow(new Object[]{s.id, s.name, s.code, s.credits, s.semester});
        }
    }

    private void filterSubjectsBySemester(String semester) {
        subjectTableModel.setRowCount(0);
        for (Subject s : dataManager.getSubjects()) {
            if (semester.equals("All Semesters") || s.semester.equals(semester)) {
                subjectTableModel.addRow(new Object[]{s.id, s.name, s.code, s.credits, s.semester});
            }
        }
    }

    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppConstants.BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topSection = new JPanel(new BorderLayout(10, 10));
        topSection.setOpaque(false);

        JPanel entryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        entryPanel.setBackground(Color.WHITE);
        entryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Enter Result"),
            new EmptyBorder(10, 10, 10, 10)
        ));

        JComboBox<String> studentCombo = new JComboBox<>();
        JComboBox<String> subjectCombo = new JComboBox<>();
        JComboBox<String> semesterCombo = new JComboBox<>(dataManager.getSemesters().toArray(new String[0]));
        JSpinner internalSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 40, 1));
        JSpinner externalSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 60, 1));

        for (Student s : dataManager.getStudents()) {
            studentCombo.addItem(s.id + " - " + s.name);
        }
        for (Subject s : dataManager.getSubjects()) {
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

        JButton saveResultBtn = UIHelper.createActionButton("💾 Save Result", AppConstants.SUCCESS_COLOR);
        saveResultBtn.setPreferredSize(new Dimension(130, 30));
        entryPanel.add(saveResultBtn);

        topSection.add(entryPanel, BorderLayout.CENTER);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Filter:"));

        JComboBox<String> filterSemester = new JComboBox<>();
        filterSemester.addItem("All Semesters");
        for (String sem : dataManager.getSemesters()) {
            filterSemester.addItem(sem);
        }

        JComboBox<String> filterBatch = new JComboBox<>();
        filterBatch.addItem("All Batches");
        for (String batch : dataManager.getBatches()) {
            filterBatch.addItem(batch);
        }

        filterPanel.add(new JLabel("Semester:"));
        filterPanel.add(filterSemester);
        filterPanel.add(new JLabel("Batch:"));
        filterPanel.add(filterBatch);

        JButton applyFilterBtn = UIHelper.createActionButton("Apply Filter", AppConstants.PRIMARY_COLOR);
        applyFilterBtn.setPreferredSize(new Dimension(120, 30));
        filterPanel.add(applyFilterBtn);

        topSection.add(filterPanel, BorderLayout.SOUTH);

        panel.add(topSection, BorderLayout.NORTH);

        String[] columns = {"Student ID", "Student Name", "Subject", "Semester", "Internal", "External", "Total", "Grade", "Status"};
        resultTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable = new JTable(resultTableModel);
        UIHelper.styleTable(resultTable);

        resultTable.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if ("PASS".equals(value)) {
                    c.setForeground(AppConstants.SUCCESS_COLOR);
                } else {
                    c.setForeground(AppConstants.DANGER_COLOR);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        bottomToolbar.setOpaque(false);

        JButton editResultBtn = UIHelper.createActionButton("✏️ Edit Result", AppConstants.PRIMARY_COLOR);
        JButton deleteResultBtn = UIHelper.createActionButton("🗑️ Delete Result", AppConstants.DANGER_COLOR);
        JButton bulkEntryBtn = UIHelper.createActionButton("📋 Bulk Entry", AppConstants.WARNING_COLOR);

        editResultBtn.setPreferredSize(new Dimension(130, 35));
        deleteResultBtn.setPreferredSize(new Dimension(140, 35));
        bulkEntryBtn.setPreferredSize(new Dimension(120, 35));

        bottomToolbar.add(editResultBtn);
        bottomToolbar.add(deleteResultBtn);
        bottomToolbar.add(bulkEntryBtn);

        panel.add(bottomToolbar, BorderLayout.SOUTH);

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

            boolean exists = dataManager.getResults().stream().anyMatch(r ->
                r.studentId == studentId && r.subjectId == subjectId && r.semester.equals(semester));

            if (exists) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Result already exists. Update it?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dataManager.getResults().removeIf(r -> r.studentId == studentId && r.subjectId == subjectId && r.semester.equals(semester));
                } else {
                    return;
                }
            }

            Result result = new Result(studentId, subjectId, semester, internal, external);
            dataManager.getResults().add(result);
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

                Subject subject = dataManager.getSubjects().stream().filter(s -> s.name.equals(subjectName)).findFirst().orElse(null);
                if (subject != null) {
                    dataManager.getResults().removeIf(r -> r.studentId == studentId && r.subjectId == subject.id && r.semester.equals(semester));
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

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        selectionPanel.add(new JLabel("Semester:"));
        JComboBox<String> semCombo = new JComboBox<>(dataManager.getSemesters().toArray(new String[0]));
        selectionPanel.add(semCombo);
        selectionPanel.add(new JLabel("Subject:"));
        JComboBox<String> subCombo = new JComboBox<>();
        for (Subject s : dataManager.getSubjects()) {
            subCombo.addItem(s.id + " - " + s.name);
        }
        selectionPanel.add(subCombo);

        JButton loadBtn = UIHelper.createActionButton("Load Students", AppConstants.PRIMARY_COLOR);
        loadBtn.setPreferredSize(new Dimension(130, 30));
        selectionPanel.add(loadBtn);

        panel.add(selectionPanel, BorderLayout.NORTH);

        String[] cols = {"Student ID", "Student Name", "Internal (40)", "External (60)"};
        DefaultTableModel bulkModel = new DefaultTableModel(cols, 0);
        JTable bulkTable = new JTable(bulkModel);
        UIHelper.styleTable(bulkTable);

        bulkTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JTextField()));
        bulkTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JTextField()));

        panel.add(new JScrollPane(bulkTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveAllBtn = UIHelper.createActionButton("Save All Results", AppConstants.SUCCESS_COLOR);
        JButton cancelBtn = UIHelper.createActionButton("Cancel", AppConstants.SECONDARY_COLOR);
        saveAllBtn.setPreferredSize(new Dimension(150, 35));
        cancelBtn.setPreferredSize(new Dimension(100, 35));
        btnPanel.add(saveAllBtn);
        btnPanel.add(cancelBtn);

        panel.add(btnPanel, BorderLayout.SOUTH);

        loadBtn.addActionListener(e -> {
            bulkModel.setRowCount(0);
            for (Student s : dataManager.getStudents()) {
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
                    dataManager.getResults().removeIf(r -> r.studentId == studentId && r.subjectId == subjectId && r.semester.equals(semester));
                    dataManager.getResults().add(new Result(studentId, subjectId, semester, internal, external));
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
        for (Result r : dataManager.getResults()) {
            Student student = dataManager.findStudentById(r.studentId);
            Subject subject = dataManager.findSubjectById(r.subjectId);

            if (student == null || subject == null) continue;

            if (filterSemester != null && !r.semester.equals(filterSemester)) continue;
            if (filterBatch != null && !student.batch.equals(filterBatch)) continue;

            int total = r.internalMarks + r.externalMarks;
            String grade = UIHelper.calculateGrade(total);
            String status = (r.internalMarks >= 16 && r.externalMarks >= 24) ? "PASS" : "FAIL";

            resultTableModel.addRow(new Object[]{
                r.studentId, student.name, subject.name, r.semester,
                r.internalMarks, r.externalMarks, total, grade, status
            });
        }
    }

    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppConstants.BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

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

        JComboBox<String> semesterCombo = new JComboBox<>(dataManager.getSemesters().toArray(new String[0]));
        JComboBox<String> studentCombo = new JComboBox<>();
        for (Student s : dataManager.getStudents()) {
            studentCombo.addItem(s.id + " - " + s.name);
        }

        JComboBox<String> batchCombo = new JComboBox<>();
        batchCombo.addItem("All Batches");
        for (String batch : dataManager.getBatches()) {
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

        JButton generateBtn = UIHelper.createActionButton("📊 Generate Report", AppConstants.PRIMARY_COLOR);
        JButton printBtn = UIHelper.createActionButton("🖨️ Print", AppConstants.SECONDARY_COLOR);
        JButton exportBtn = UIHelper.createActionButton("💾 Export", AppConstants.SUCCESS_COLOR);

        generateBtn.setPreferredSize(new Dimension(150, 35));
        printBtn.setPreferredSize(new Dimension(100, 35));
        exportBtn.setPreferredSize(new Dimension(100, 35));

        selectionPanel.add(generateBtn);
        selectionPanel.add(printBtn);
        selectionPanel.add(exportBtn);

        panel.add(selectionPanel, BorderLayout.NORTH);

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
                        report.append(reportGenerator.generateIndividualReport(studentId, semester));
                    }
                    break;
                case "Class Result - Semester Wise":
                    report.append(reportGenerator.generateClassReport(semester, batch.equals("All Batches") ? null : batch));
                    break;
                case "Subject Analysis":
                    report.append(reportGenerator.generateSubjectAnalysis(semester));
                    break;
                case "Pass/Fail Statistics":
                    report.append(reportGenerator.generatePassFailStats(semester, batch.equals("All Batches") ? null : batch));
                    break;
                case "Top Performers":
                    report.append(reportGenerator.generateTopPerformers(semester, 10));
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

    private void updateDashboard() {
        totalStudentsLabel.setText(String.valueOf(dataManager.getStudents().size()));
        totalSubjectsLabel.setText(String.valueOf(dataManager.getSubjects().size()));
        totalResultsLabel.setText(String.valueOf(dataManager.getResults().size()));

        int passed = 0;
        int total = 0;
        for (Result r : dataManager.getResults()) {
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
                for (Result r : dataManager.getResults()) {
                    Student student = dataManager.findStudentById(r.studentId);
                    Subject subject = dataManager.findSubjectById(r.subjectId);
                    if (student != null && subject != null) {
                        int total = r.internalMarks + r.externalMarks;
                        String grade = UIHelper.calculateGrade(total);
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
