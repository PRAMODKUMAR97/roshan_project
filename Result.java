public class Result {
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
