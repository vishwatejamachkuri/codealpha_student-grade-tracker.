import java.util.ArrayList;

public class GradeTrackerTest {
    public static void main(String[] args) {
        System.out.println("Starting GradeTracker automated unit tests...");
        GradeTracker tracker = new GradeTracker();

        // 1. Initial empty state
        assert tracker.isEmpty() : "Tracker should be empty initially";
        assert tracker.getStudentCount() == 0 : "Count should be 0";
        assert tracker.getClassAverage() == 0.0 : "Class average of empty tracker should be 0.0";

        // 2. Add student 1
        ArrayList<Double> marks1 = new ArrayList<>();
        marks1.add(90.0);
        marks1.add(100.0);
        marks1.add(80.0);
        Student s1 = new Student("S001", "Student One", marks1);
        boolean added1 = tracker.addStudent(s1);
        assert added1 : "Student 1 should be added successfully";
        assert s1.calculateTotal() == 270.0 : "Total should be 270.0";
        assert s1.calculateAverage() == 90.0 : "Average should be 90.0";
        assert s1.getHighestScore() == 100.0 : "Highest score should be 100.0";
        assert s1.getLowestScore() == 80.0 : "Lowest score should be 80.0";
        assert s1.calculateGrade() == 'A' : "Grade should be A";

        // 3. Duplicate ID test
        boolean addedDup = tracker.addStudent(new Student("S001", "Duplicate Student", marks1));
        assert !addedDup : "Duplicate ID should be rejected";

        // 4. Add student 2
        ArrayList<Double> marks2 = new ArrayList<>();
        marks2.add(50.0);
        marks2.add(60.0);
        marks2.add(70.0);
        Student s2 = new Student("S002", "Student Two", marks2);
        tracker.addStudent(s2);
        assert s2.calculateAverage() == 60.0 : "Average should be 60.0";
        assert s2.calculateGrade() == 'D' : "Grade should be D";

        // 5. Class metrics
        assert tracker.getStudentCount() == 2 : "Count should be 2";
        assert tracker.getClassAverage() == 75.0 : "Class average should be 75.0";
        assert tracker.getClassHighestScore() == 100.0 : "Highest score in class should be 100.0";
        assert tracker.getClassLowestScore() == 50.0 : "Lowest score in class should be 50.0";
        assert tracker.getTopStudent().getId().equals("S001") : "Top student should be S001";
        assert tracker.getLowestStudent().getId().equals("S002") : "Lowest student should be S002";

        // 6. Grade distribution: 1 'A', 0 'B', 0 'C', 1 'D', 0 'F'
        int[] dist = tracker.getGradeDistribution();
        assert dist[0] == 1 : "Expected 1 Grade A";
        assert dist[1] == 0 : "Expected 0 Grade B";
        assert dist[2] == 0 : "Expected 0 Grade C";
        assert dist[3] == 1 : "Expected 1 Grade D";
        assert dist[4] == 0 : "Expected 0 Grade F";

        // 7. Update grades
        ArrayList<Double> updatedMarks = new ArrayList<>();
        updatedMarks.add(85.0);
        updatedMarks.add(85.0);
        boolean updated = tracker.updateStudent("S002", updatedMarks);
        assert updated : "Update should succeed";
        assert s2.calculateAverage() == 85.0 : "Updated average should be 85.0";
        assert s2.calculateGrade() == 'B' : "Updated grade should be B";

        // 8. Summary report text generation
        String report = tracker.generateSummaryReportString();
        assert report.contains("ALL STUDENT RECORDS") : "Report should contain header";
        assert report.contains("CLASS PERFORMANCE STATISTICS") : "Report should contain stats";
        assert report.contains("GRADE DISTRIBUTION BREAKDOWN") : "Report should contain breakdown";

        // 9. Remove student
        boolean removed = tracker.removeStudent("S001");
        assert removed : "Removal of S001 should succeed";
        assert tracker.getStudentCount() == 1 : "Count should be 1 after removal";
        assert tracker.findStudentById("S001") == null : "S001 should not be found";

        System.out.println("ALL UNIT TESTS PASSED SUCCESSFULLY! (9/9 checks verified)");
    }
}
