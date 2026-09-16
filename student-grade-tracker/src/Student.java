import java.util.ArrayList;

/**
 * Represents an individual student with their personal details and subject marks.
 * Demonstrates OOP Encapsulation with private fields, getters, setters,
 * and business methods for calculating grade metrics.
 */
public class Student {
    private String id;
    private String name;
    private ArrayList<Double> marks;

    /**
     * Parameterized constructor to initialize a student.
     *
     * @param id    Unique student ID / Roll number
     * @param name  Full name of the student
     * @param marks List of marks scored in various subjects
     */
    public Student(String id, String name, ArrayList<Double> marks) {
        this.id = id.trim();
        this.name = name.trim();
        this.marks = new ArrayList<>(marks); // Defensive copy
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public ArrayList<Double> getMarks() {
        return new ArrayList<>(marks); // Defensive copy to preserve encapsulation
    }

    public void setMarks(ArrayList<Double> marks) {
        this.marks = new ArrayList<>(marks);
    }

    /**
     * Calculates the sum of all subject marks.
     *
     * @return Total marks obtained
     */
    public double calculateTotal() {
        double total = 0.0;
        for (double mark : marks) {
            total += mark;
        }
        return total;
    }

    /**
     * Calculates the average percentage marks across subjects.
     *
     * @return Average marks (0.0 if no subjects recorded)
     */
    public double calculateAverage() {
        if (marks.isEmpty()) {
            return 0.0;
        }
        return calculateTotal() / marks.size();
    }

    /**
     * Finds the highest single subject score.
     *
     * @return Highest mark
     */
    public double getHighestScore() {
        if (marks.isEmpty()) {
            return 0.0;
        }
        double highest = marks.get(0);
        for (double mark : marks) {
            if (mark > highest) {
                highest = mark;
            }
        }
        return highest;
    }

    /**
     * Finds the lowest single subject score.
     *
     * @return Lowest mark
     */
    public double getLowestScore() {
        if (marks.isEmpty()) {
            return 0.0;
        }
        double lowest = marks.get(0);
        for (double mark : marks) {
            if (mark < lowest) {
                lowest = mark;
            }
        }
        return lowest;
    }

    /**
     * Determines letter grade based on the student's average marks.
     * Grading Scale:
     * - 90 to 100: A
     * - 80 to 89.99: B
     * - 70 to 79.99: C
     * - 60 to 69.99: D
     * - Below 60: F
     *
     * @return Letter grade ('A', 'B', 'C', 'D', or 'F')
     */
    public char calculateGrade() {
        double avg = calculateAverage();
        if (avg >= 90.0) {
            return 'A';
        } else if (avg >= 80.0) {
            return 'B';
        } else if (avg >= 70.0) {
            return 'C';
        } else if (avg >= 60.0) {
            return 'D';
        } else {
            return 'F';
        }
    }

    /**
     * Displays a clean formatted summary of this student's performance.
     */
    public void displayStudentDetails() {
        System.out.println("--------------------------------------------------------");
        System.out.println(" Student Details");
        System.out.println("--------------------------------------------------------");
        System.out.printf("  ID / Roll No    : %s%n", id);
        System.out.printf("  Student Name    : %s%n", name);
        System.out.print("  Subject Marks   : ");
        for (int i = 0; i < marks.size(); i++) {
            System.out.printf("%.1f%s", marks.get(i), (i == marks.size() - 1) ? "" : ", ");
        }
        System.out.println();
        System.out.printf("  Total Marks     : %.2f%n", calculateTotal());
        System.out.printf("  Average Marks   : %.2f%%%n", calculateAverage());
        System.out.printf("  Highest Score   : %.2f%n", getHighestScore());
        System.out.printf("  Lowest Score    : %.2f%n", getLowestScore());
        System.out.printf("  Final Grade     : %c%n", calculateGrade());
        System.out.println("--------------------------------------------------------");
    }
}
