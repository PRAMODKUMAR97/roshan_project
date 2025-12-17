import java.util.ArrayList;

public class DataManager {
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Subject> subjects = new ArrayList<>();
    private ArrayList<Result> results = new ArrayList<>();
    private ArrayList<String> semesters = new ArrayList<>();
    private ArrayList<String> batches = new ArrayList<>();

    private int studentIdCounter = 1000;
    private int subjectIdCounter = 100;

    public DataManager() {
        initializeSampleData();
    }

    private void initializeSampleData() {
        for (int i = 1; i <= 8; i++) {
            semesters.add("Semester " + i);
        }

        batches.add("2020-2024");
        batches.add("2021-2025");
        batches.add("2022-2026");
        batches.add("2023-2027");

        subjects.add(new Subject(101, "Mathematics I", "MTH101", 4, "Semester 1"));
        subjects.add(new Subject(102, "Physics", "PHY101", 4, "Semester 1"));
        subjects.add(new Subject(103, "Programming in C", "CSE101", 3, "Semester 1"));
        subjects.add(new Subject(104, "English", "ENG101", 2, "Semester 1"));
        subjects.add(new Subject(105, "Mathematics II", "MTH102", 4, "Semester 2"));
        subjects.add(new Subject(106, "Data Structures", "CSE201", 4, "Semester 2"));

        students.add(new Student(1001, "John Smith", "john@email.com", "9876543210", "2021-2025", "A"));
        students.add(new Student(1002, "Emma Wilson", "emma@email.com", "9876543211", "2021-2025", "A"));
        students.add(new Student(1003, "Michael Brown", "michael@email.com", "9876543212", "2021-2025", "B"));
        students.add(new Student(1004, "Sarah Davis", "sarah@email.com", "9876543213", "2022-2026", "A"));
        students.add(new Student(1005, "James Johnson", "james@email.com", "9876543214", "2022-2026", "A"));

        studentIdCounter = 1006;
        subjectIdCounter = 107;

        results.add(new Result(1001, 101, "Semester 1", 85, 78));
        results.add(new Result(1001, 102, "Semester 1", 72, 68));
        results.add(new Result(1001, 103, "Semester 1", 90, 85));
        results.add(new Result(1002, 101, "Semester 1", 78, 82));
        results.add(new Result(1002, 102, "Semester 1", 65, 70));
        results.add(new Result(1003, 101, "Semester 1", 45, 38));
        results.add(new Result(1003, 102, "Semester 1", 52, 48));
    }

    public ArrayList<Student> getStudents() { return students; }
    public ArrayList<Subject> getSubjects() { return subjects; }
    public ArrayList<Result> getResults() { return results; }
    public ArrayList<String> getSemesters() { return semesters; }
    public ArrayList<String> getBatches() { return batches; }

    public int getNextStudentId() { return studentIdCounter++; }
    public int getNextSubjectId() { return subjectIdCounter++; }

    public Student findStudentById(int id) {
        return students.stream().filter(s -> s.id == id).findFirst().orElse(null);
    }

    public Subject findSubjectById(int id) {
        return subjects.stream().filter(s -> s.id == id).findFirst().orElse(null);
    }
}
