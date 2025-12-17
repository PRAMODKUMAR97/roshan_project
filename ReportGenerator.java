import java.util.*;

public class ReportGenerator {
    private DataManager dataManager;

    public ReportGenerator(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public String generateIndividualReport(int studentId, String semester) {
        Student student = dataManager.findStudentById(studentId);
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

        for (Result r : dataManager.getResults()) {
            if (r.studentId == studentId && r.semester.equals(semester)) {
                Subject subject = dataManager.findSubjectById(r.subjectId);
                if (subject != null) {
                    int total = r.internalMarks + r.externalMarks;
                    String grade = UIHelper.calculateGrade(total);
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

    public String generateClassReport(String semester, String batch) {
        StringBuilder sb = new StringBuilder();
        sb.append("CLASS RESULT - ").append(semester).append("\n");
        if (batch != null) sb.append("Batch: ").append(batch).append("\n");
        sb.append("─".repeat(80)).append("\n\n");

        sb.append(String.format("%-6s %-20s %-10s %10s %10s %10s %10s\n",
            "ID", "Name", "Section", "Total", "Percentage", "Grade", "Status"));
        sb.append("─".repeat(80)).append("\n");

        for (Student student : dataManager.getStudents()) {
            if (batch != null && !student.batch.equals(batch)) continue;

            int totalMarks = 0;
            int totalSubjects = 0;
            int passedSubjects = 0;

            for (Result r : dataManager.getResults()) {
                if (r.studentId == student.id && r.semester.equals(semester)) {
                    totalMarks += r.internalMarks + r.externalMarks;
                    totalSubjects++;
                    if (r.internalMarks >= 16 && r.externalMarks >= 24) passedSubjects++;
                }
            }

            if (totalSubjects > 0) {
                double percentage = (totalMarks * 100.0) / (totalSubjects * 100);
                String grade = UIHelper.calculateGrade((int) percentage);
                String status = passedSubjects == totalSubjects ? "PASS" : "FAIL";

                sb.append(String.format("%-6d %-20s %-10s %10d %9.2f%% %10s %10s\n",
                    student.id, student.name, student.section, totalMarks, percentage, grade, status));
            }
        }

        return sb.toString();
    }

    public String generateSubjectAnalysis(String semester) {
        StringBuilder sb = new StringBuilder();
        sb.append("SUBJECT WISE ANALYSIS - ").append(semester).append("\n");
        sb.append("─".repeat(70)).append("\n\n");

        for (Subject subject : dataManager.getSubjects()) {
            if (!subject.semester.equals(semester)) continue;

            int totalStudents = 0;
            int passCount = 0;
            int totalMarks = 0;
            int highest = 0;
            int lowest = 100;

            for (Result r : dataManager.getResults()) {
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

    public String generatePassFailStats(String semester, String batch) {
        StringBuilder sb = new StringBuilder();
        sb.append("PASS/FAIL STATISTICS - ").append(semester).append("\n");
        if (batch != null) sb.append("Batch: ").append(batch).append("\n");
        sb.append("─".repeat(50)).append("\n\n");

        int totalStudents = 0;
        int passCount = 0;
        int failCount = 0;

        for (Student student : dataManager.getStudents()) {
            if (batch != null && !student.batch.equals(batch)) continue;

            boolean hasResults = false;
            boolean allPassed = true;

            for (Result r : dataManager.getResults()) {
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
        for (Result r : dataManager.getResults()) {
            if (r.semester.equals(semester)) {
                Student student = dataManager.findStudentById(r.studentId);
                if (batch != null && student != null && !student.batch.equals(batch)) continue;

                int total = r.internalMarks + r.externalMarks;
                String grade = UIHelper.calculateGrade(total);
                gradeCount.put(grade, gradeCount.getOrDefault(grade, 0) + 1);
            }
        }

        for (String grade : new String[]{"A+", "A", "B+", "B", "C+", "C", "D", "F"}) {
            int count = gradeCount.getOrDefault(grade, 0);
            sb.append(String.format("Grade %s: %d\n", grade, count));
        }

        return sb.toString();
    }

    public String generateTopPerformers(String semester, int limit) {
        StringBuilder sb = new StringBuilder();
        sb.append("TOP PERFORMERS - ").append(semester).append("\n");
        sb.append("─".repeat(60)).append("\n\n");

        Map<Integer, Integer> studentMarks = new HashMap<>();
        Map<Integer, Integer> studentSubjects = new HashMap<>();

        for (Result r : dataManager.getResults()) {
            if (r.semester.equals(semester)) {
                int total = r.internalMarks + r.externalMarks;
                studentMarks.put(r.studentId, studentMarks.getOrDefault(r.studentId, 0) + total);
                studentSubjects.put(r.studentId, studentSubjects.getOrDefault(r.studentId, 0) + 1);
            }
        }

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

            Student student = dataManager.findStudentById(entry.getKey());
            if (student != null) {
                double percentage = (entry.getValue() * 100.0) / (studentSubjects.get(entry.getKey()) * 100);
                String grade = UIHelper.calculateGrade((int) percentage);

                sb.append(String.format("%-5d %-6d %-25s %11.2f%% %10s\n",
                    rank, student.id, student.name, percentage, grade));
                rank++;
            }
        }

        return sb.toString();
    }
}
