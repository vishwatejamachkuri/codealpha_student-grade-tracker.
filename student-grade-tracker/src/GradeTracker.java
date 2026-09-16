import java.util.ArrayList;

/**
 * Manages the collection of Student objects using an ArrayList.
 * Implements business operations for adding, viewing, searching,
 * updating, removing students, and computing class-level statistics.
 */
public class GradeTracker {
    // Encapsulated dynamic list of student records
    private ArrayList<Student> students;

    /**
     * Default constructor initializing an empty student list.
     */
    public GradeTracker() {
        this.students = new ArrayList<>();
    }

    /**
     * Checks if a student ID already exists in the records.
     *
     * @param id Student ID to check
     * @return true if ID exists, false otherwise
     */
    public boolean idExists(String id) {
        return findStudentById(id) != null;
    }

    /**
     * Helper method to search for a student by ID.
     *
     * @param id Student ID
     * @return Student instance if found, null otherwise
     */
    public Student findStudentById(String id) {
        if (id == null) return null;
        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(id.trim())) {
                return student;
            }
        }
        return null;
    }

    /**
     * Adds a new student to the ArrayList after verifying ID uniqueness.
     *
     * @param student Student object to add
     * @return true if successfully added, false if duplicate ID
     */
    public boolean addStudent(Student student) {
        if (student == null || idExists(student.getId())) {
            return false;
        }
        return students.add(student);
    }

    /**
     * Returns the total count of students currently in the tracker.
     *
     * @return Number of students
     */
    public int getStudentCount() {
        return students.size();
    }

    /**
     * Checks if the student list is empty.
     *
     * @return true if no students, false otherwise
     */
    public boolean isEmpty() {
        return students.isEmpty();
    }

    /**
     * Displays all student records in a structured, professional table.
     */
    public void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("\n[!] No student records found. Please add students first.");
            return;
        }

        System.out.println("\n=======================================================================================================");
        System.out.println("                                         ALL STUDENT RECORDS                                          ");
        System.out.println("=======================================================================================================");
        System.out.printf("%-12s %-22s %-12s %-14s %-12s %-12s %-8s%n",
                "ID", "Name", "Total", "Average (%)", "Highest", "Lowest", "Grade");
        System.out.println("-------------------------------------------------------------------------------------------------------");

        for (Student s : students) {
            System.out.printf("%-12s %-22s %-12.2f %-14.2f %-12.2f %-12.2f %-8c%n",
                    s.getId(),
                    s.getName(),
                    s.calculateTotal(),
                    s.calculateAverage(),
                    s.getHighestScore(),
                    s.getLowestScore(),
                    s.calculateGrade());
        }

        System.out.println("-------------------------------------------------------------------------------------------------------");
        System.out.printf("Total Enrolled Students: %d%n", students.size());
    }

    /**
     * Searches for a student by ID and prints their full profile if found.
     *
     * @param id Student ID to search
     * @return Student if found, null otherwise
     */
    public Student searchStudent(String id) {
        Student student = findStudentById(id);
        if (student != null) {
            System.out.println("\n[✓] Student Found:");
            student.displayStudentDetails();
        } else {
            System.out.printf("\n[!] Student with ID '%s' was not found.%n", id);
        }
        return student;
    }

    /**
     * Updates subject marks for an existing student and recalculates statistics.
     *
     * @param id       Student ID to update
     * @param newMarks New list of marks
     * @return true if updated successfully, false if student not found
     */
    public boolean updateStudent(String id, ArrayList<Double> newMarks) {
        Student student = findStudentById(id);
        if (student == null) {
            return false;
        }
        student.setMarks(newMarks);
        return true;
    }

    /**
     * Permanently deletes a student from the ArrayList.
     * Demonstrates dynamic resizing and object removal from ArrayList.
     *
     * @param id Student ID to remove
     * @return true if removed, false if student was not found
     */
    public boolean removeStudent(String id) {
        Student student = findStudentById(id);
        if (student == null) {
            return false;
        }
        // ArrayList.remove(Object o) removes the element and shifts remaining elements left
        return students.remove(student);
    }

    /**
     * Returns a copy of the list of all students.
     *
     * @return List of students
     */
    public ArrayList<Student> getStudents() {
        return new ArrayList<>(students);
    }

    /**
     * Calculates the overall average of all students' percentage scores.
     *
     * @return Class average percentage, or 0.0 if no students
     */
    public double getClassAverage() {
        if (students.isEmpty()) return 0.0;
        double sum = 0.0;
        for (Student s : students) {
            sum += s.calculateAverage();
        }
        return sum / students.size();
    }

    /**
     * Finds the student with the highest average score.
     *
     * @return Top performing Student, or null if no students
     */
    public Student getTopStudent() {
        if (students.isEmpty()) return null;
        Student top = students.get(0);
        for (Student s : students) {
            if (s.calculateAverage() > top.calculateAverage()) {
                top = s;
            }
        }
        return top;
    }

    /**
     * Finds the student with the lowest average score.
     *
     * @return Lowest performing Student, or null if no students
     */
    public Student getLowestStudent() {
        if (students.isEmpty()) return null;
        Student lowest = students.get(0);
        for (Student s : students) {
            if (s.calculateAverage() < lowest.calculateAverage()) {
                lowest = s;
            }
        }
        return lowest;
    }

    /**
     * Finds the highest single subject score across the entire class.
     *
     * @return Highest score, or 0.0 if no students
     */
    public double getClassHighestScore() {
        if (students.isEmpty()) return 0.0;
        double highest = Double.NEGATIVE_INFINITY;
        for (Student s : students) {
            if (s.getHighestScore() > highest) {
                highest = s.getHighestScore();
            }
        }
        return highest;
    }

    /**
     * Finds the lowest single subject score across the entire class.
     *
     * @return Lowest score, or 0.0 if no students
     */
    public double getClassLowestScore() {
        if (students.isEmpty()) return 0.0;
        double lowest = Double.POSITIVE_INFINITY;
        for (Student s : students) {
            if (s.getLowestScore() < lowest) {
                lowest = s.getLowestScore();
            }
        }
        return lowest;
    }

    /**
     * Calculates the counts of students in each grade tier:
     * index 0: A (90 - 100%)
     * index 1: B (80 - 89%)
     * index 2: C (70 - 79%)
     * index 3: D (60 - 69%)
     * index 4: F (< 60%)
     *
     * @return integer array of length 5 [A, B, C, D, F]
     */
    public int[] getGradeDistribution() {
        int[] dist = new int[5];
        for (Student s : students) {
            switch (s.calculateGrade()) {
                case 'A': dist[0]++; break;
                case 'B': dist[1]++; break;
                case 'C': dist[2]++; break;
                case 'D': dist[3]++; break;
                default:  dist[4]++; break;
            }
        }
        return dist;
    }

    /**
     * Computes and displays overall class-level performance metrics.
     */
    public void calculateClassStatistics() {
        if (students.isEmpty()) {
            System.out.println("\n[!] No student records available to calculate class statistics.");
            return;
        }

        Student topStudent = getTopStudent();
        Student lowestStudent = getLowestStudent();
        double classAverage = getClassAverage();
        double overallClassHighest = getClassHighestScore();
        double overallClassLowest = getClassLowestScore();

        System.out.println("\n========================================================");
        System.out.println("                 CLASS PERFORMANCE STATISTICS           ");
        System.out.println("========================================================");
        System.out.printf("  Total Students Evaluated    : %d%n", students.size());
        System.out.printf("  Overall Class Average       : %.2f%%%n", classAverage);
        System.out.println("--------------------------------------------------------");
        System.out.printf("  Top Performing Student      : %s (ID: %s, Avg: %.2f%%, Grade: %c)%n",
                topStudent.getName(), topStudent.getId(), topStudent.calculateAverage(), topStudent.calculateGrade());
        System.out.printf("  Lowest Performing Student   : %s (ID: %s, Avg: %.2f%%, Grade: %c)%n",
                lowestStudent.getName(), lowestStudent.getId(), lowestStudent.calculateAverage(), lowestStudent.calculateGrade());
        System.out.println("--------------------------------------------------------");
        System.out.printf("  Highest Subject Mark in Class: %.2f%n", overallClassHighest);
        System.out.printf("  Lowest Subject Mark in Class : %.2f%n", overallClassLowest);
        System.out.println("========================================================");
    }

    /**
     * Generates a complete executive summary string combining roster and metrics.
     *
     * @return Formatted multi-line summary text
     */
    public String generateSummaryReportString() {
        if (students.isEmpty()) {
            return "[!] No records available to generate summary report.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=======================================================================================================\n");
        sb.append("                                         ALL STUDENT RECORDS                                          \n");
        sb.append("=======================================================================================================\n");
        sb.append(String.format("%-12s %-22s %-12s %-14s %-12s %-12s %-8s%n",
                "ID", "Name", "Total", "Average (%)", "Highest", "Lowest", "Grade"));
        sb.append("-------------------------------------------------------------------------------------------------------\n");

        for (Student s : students) {
            sb.append(String.format("%-12s %-22s %-12.2f %-14.2f %-12.2f %-12.2f %-8c%n",
                    s.getId(),
                    s.getName(),
                    s.calculateTotal(),
                    s.calculateAverage(),
                    s.getHighestScore(),
                    s.getLowestScore(),
                    s.calculateGrade()));
        }

        sb.append("-------------------------------------------------------------------------------------------------------\n");
        sb.append(String.format("Total Enrolled Students: %d%n%n", students.size()));

        Student topStudent = getTopStudent();
        Student lowestStudent = getLowestStudent();

        sb.append("========================================================\n");
        sb.append("                 CLASS PERFORMANCE STATISTICS           \n");
        sb.append("========================================================\n");
        sb.append(String.format("  Total Students Evaluated    : %d%n", students.size()));
        sb.append(String.format("  Overall Class Average       : %.2f%%%n", getClassAverage()));
        sb.append("--------------------------------------------------------\n");
        sb.append(String.format("  Top Performing Student      : %s (ID: %s, Avg: %.2f%%, Grade: %c)%n",
                topStudent.getName(), topStudent.getId(), topStudent.calculateAverage(), topStudent.calculateGrade()));
        sb.append(String.format("  Lowest Performing Student   : %s (ID: %s, Avg: %.2f%%, Grade: %c)%n",
                lowestStudent.getName(), lowestStudent.getId(), lowestStudent.calculateAverage(), lowestStudent.calculateGrade()));
        sb.append("--------------------------------------------------------\n");
        sb.append(String.format("  Highest Subject Mark in Class: %.2f%n", getClassHighestScore()));
        sb.append(String.format("  Lowest Subject Mark in Class : %.2f%n", getClassLowestScore()));
        sb.append("========================================================\n\n");

        int[] dist = getGradeDistribution();
        sb.append("========================================================\n");
        sb.append("                 GRADE DISTRIBUTION BREAKDOWN           \n");
        sb.append("========================================================\n");
        sb.append(String.format("  Grade A (90 - 100%%) : %d student(s)%n", dist[0]));
        sb.append(String.format("  Grade B (80 - 89%%)  : %d student(s)%n", dist[1]));
        sb.append(String.format("  Grade C (70 - 79%%)  : %d student(s)%n", dist[2]));
        sb.append(String.format("  Grade D (60 - 69%%)  : %d student(s)%n", dist[3]));
        sb.append(String.format("  Grade F (< 60%%)     : %d student(s)%n", dist[4]));
        sb.append("========================================================\n");

        return sb.toString();
    }

    /**
     * Displays a complete executive summary combining the roster and class metrics.
     */
    public void displaySummaryReport() {
        if (students.isEmpty()) {
            System.out.println("\n[!] No records available to generate summary report.");
            return;
        }

        System.out.println("\n*******************************************************************************************************");
        System.out.println("                                      COMPREHENSIVE SUMMARY REPORT                                     ");
        System.out.println("*******************************************************************************************************");

        // 1. Roster Table
        viewStudents();

        // 2. Class Statistics
        calculateClassStatistics();

        // 3. Grade Distribution Breakdown
        int[] dist = getGradeDistribution();
        System.out.println("\n========================================================");
        System.out.println("                 GRADE DISTRIBUTION BREAKDOWN           ");
        System.out.println("========================================================");
        System.out.printf("  Grade A (90 - 100%%) : %d student(s)%n", dist[0]);
        System.out.printf("  Grade B (80 - 89%%)  : %d student(s)%n", dist[1]);
        System.out.printf("  Grade C (70 - 79%%)  : %d student(s)%n", dist[2]);
        System.out.printf("  Grade D (60 - 69%%)  : %d student(s)%n", dist[3]);
        System.out.printf("  Grade F (< 60%%)     : %d student(s)%n", dist[4]);
        System.out.println("========================================================\n");
    }
}
