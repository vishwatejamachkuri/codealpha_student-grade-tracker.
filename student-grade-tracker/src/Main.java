import java.util.ArrayList;
import java.util.Scanner;

/**
 * Console-based user interface for the Student Grade Tracker.
 * Provides menu navigation, user interaction, and robust input validation.
 */
public class Main {

    // ANSI Color Codes for vibrant CLI experience
    public static final String RESET  = "\u001B[0m";
    public static final String BOLD   = "\u001B[1m";
    public static final String RED    = "\u001B[31m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE   = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN   = "\u001B[36m";
    public static final String WHITE  = "\u001B[37m";

    public static void main(String[] args) {
        // Direct launch to GUI if requested via command line flag
        if (args.length > 0 && (args[0].equalsIgnoreCase("--gui") || args[0].equalsIgnoreCase("-g") || args[0].equalsIgnoreCase("gui"))) {
            GradeTrackerGUI.main(args);
            return;
        }

        Scanner scanner = new Scanner(System.in);
        GradeTracker tracker = new GradeTracker();

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = readInt(scanner, BOLD + CYAN + "Enter your choice (1-8): " + RESET, 1, 8);

            switch (choice) {
                case 1:
                    handleAddStudent(scanner, tracker);
                    break;
                case 2:
                    tracker.viewStudents();
                    break;
                case 3:
                    handleSearchStudent(scanner, tracker);
                    break;
                case 4:
                    handleUpdateStudent(scanner, tracker);
                    break;
                case 5:
                    handleRemoveStudent(scanner, tracker);
                    break;
                case 6:
                    tracker.calculateClassStatistics();
                    break;
                case 7:
                    tracker.displaySummaryReport();
                    break;
                case 8:
                    System.out.println(CYAN + "\n========================================================");
                    System.out.println(GREEN + BOLD + "   Thank you for using Student Grade Tracker. Goodbye!  " + RESET);
                    System.out.println(CYAN + "========================================================" + RESET);
                    running = false;
                    break;
                default:
                    System.out.println(RED + "[!] Invalid option. Please select 1 through 8." + RESET);
            }

            if (running) {
                System.out.print(YELLOW + "\nPress Enter to return to the main menu..." + RESET);
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    /**
     * Displays the primary menu interface with vibrant colors.
     */
    private static void displayMenu() {
        System.out.println(CYAN + "\n========================================================" + RESET);
        System.out.println(PURPLE + BOLD + "           🎓 STUDENT GRADE TRACKER (CLI)              " + RESET);
        System.out.println(CYAN + "========================================================" + RESET);
        System.out.println(GREEN + " [1]" + WHITE + " Add Student" + RESET);
        System.out.println(BLUE + " [2]" + WHITE + " View All Students" + RESET);
        System.out.println(YELLOW + " [3]" + WHITE + " Search Student" + RESET);
        System.out.println(PURPLE + " [4]" + WHITE + " Update Grades" + RESET);
        System.out.println(RED + " [5]" + WHITE + " Remove Student" + RESET);
        System.out.println(CYAN + " [6]" + WHITE + " Class Statistics" + RESET);
        System.out.println(GREEN + " [7]" + WHITE + " Summary Report" + RESET);
        System.out.println(RED + " [8]" + WHITE + " Exit" + RESET);
        System.out.println(CYAN + "--------------------------------------------------------" + RESET);
    }

    /**
     * Handles adding a new student with validation for uniqueness, non-empty fields, and mark bounds.
     */
    private static void handleAddStudent(Scanner scanner, GradeTracker tracker) {
        System.out.println(CYAN + "\n--- [ ➕ Add New Student ] ---" + RESET);

        // 1. Enter and validate Student ID
        String id;
        while (true) {
            id = readNonEmptyString(scanner, BOLD + "Enter Student ID / Roll Number: " + RESET);
            if (tracker.idExists(id)) {
                System.out.printf(RED + "[!] Error: Student ID '%s' already exists! Student ID must be unique.%n" + RESET, id);
            } else {
                break;
            }
        }

        // 2. Enter Student Name
        String name = readNonEmptyString(scanner, BOLD + "Enter Student Name: " + RESET);

        // 3. Enter number of subjects and individual marks
        int numSubjects = readInt(scanner, BOLD + "Enter number of subjects: " + RESET, 1, 20);
        ArrayList<Double> marks = new ArrayList<>();

        for (int i = 1; i <= numSubjects; i++) {
            double mark = readDouble(scanner, String.format(BLUE + "  Enter marks for Subject %d (0 - 100): " + RESET, i), 0.0, 100.0);
            marks.add(mark);
        }

        // 4. Create and store student
        Student student = new Student(id, name, marks);
        if (tracker.addStudent(student)) {
            System.out.printf(GREEN + BOLD + "%n[✓] Student '%s' (ID: %s) added successfully!%n" + RESET, name, id);
        } else {
            System.out.println(RED + "\n[!] Failed to add student." + RESET);
        }
    }

    /**
     * Handles searching for a student and viewing their full details.
     */
    private static void handleSearchStudent(Scanner scanner, GradeTracker tracker) {
        if (tracker.isEmpty()) {
            System.out.println(RED + "\n[!] No student records found. Please add students first." + RESET);
            return;
        }

        System.out.println(CYAN + "\n--- [ 🔍 Search Student ] ---" + RESET);
        String id = readNonEmptyString(scanner, BOLD + "Enter Student ID to search: " + RESET);
        tracker.searchStudent(id);
    }

    /**
     * Handles updating an existing student's subject marks.
     */
    private static void handleUpdateStudent(Scanner scanner, GradeTracker tracker) {
        if (tracker.isEmpty()) {
            System.out.println(RED + "\n[!] No student records found. Please add students first." + RESET);
            return;
        }

        System.out.println(CYAN + "\n--- [ ✏️ Update Student Grades ] ---" + RESET);
        String id = readNonEmptyString(scanner, BOLD + "Enter Student ID to update: " + RESET);
        Student student = tracker.findStudentById(id);

        if (student == null) {
            System.out.printf(RED + "\n[!] Student with ID '%s' was not found.%n" + RESET, id);
            return;
        }

        System.out.printf(GREEN + "\nStudent Found: %s (ID: %s)%n" + RESET, student.getName(), student.getId());
        System.out.print(YELLOW + "Current Marks: " + RESET);
        ArrayList<Double> currentMarks = student.getMarks();
        for (int i = 0; i < currentMarks.size(); i++) {
            System.out.printf("%.1f%s", currentMarks.get(i), (i == currentMarks.size() - 1) ? "" : ", ");
        }
        System.out.println();

        int numSubjects = readInt(scanner, BOLD + "Enter new number of subjects: " + RESET, 1, 20);
        ArrayList<Double> newMarks = new ArrayList<>();
        for (int i = 1; i <= numSubjects; i++) {
            double mark = readDouble(scanner, String.format(BLUE + "  Enter new marks for Subject %d (0 - 100): " + RESET, i), 0.0, 100.0);
            newMarks.add(mark);
        }

        tracker.updateStudent(id, newMarks);
        System.out.printf(GREEN + BOLD + "%n[✓] Grades for student '%s' updated successfully!%n" + RESET, student.getName());

        System.out.println("\nUpdated Details:");
        student.displayStudentDetails();
    }

    /**
     * Handles deleting a student with interactive confirmation dialog.
     */
    private static void handleRemoveStudent(Scanner scanner, GradeTracker tracker) {
        if (tracker.isEmpty()) {
            System.out.println(RED + "\n[!] No student records found to remove." + RESET);
            return;
        }

        System.out.println(CYAN + "\n--- [ 🗑️ Remove Student ] ---" + RESET);
        String id = readNonEmptyString(scanner, BOLD + "Enter Student ID to remove: " + RESET);
        Student student = tracker.findStudentById(id);

        if (student == null) {
            System.out.printf(RED + "\nStudent with ID %s was not found.%n" + RESET, id);
            return;
        }

        // Display found student details before confirmation
        System.out.println(YELLOW + "\nStudent Found:" + RESET);
        System.out.printf("Name: %s%n", student.getName());
        System.out.printf("ID: %s%n", student.getId());

        // Ask for confirmation
        String confirm = readConfirmation(scanner, RED + BOLD + "\nAre you sure you want to remove this student? (Y/N): " + RESET);
        if (confirm.equalsIgnoreCase("Y")) {
            String studentName = student.getName();
            boolean removed = tracker.removeStudent(id);
            if (removed) {
                System.out.printf(GREEN + BOLD + "%nStudent %s has been removed successfully.%n" + RESET, studentName);
            } else {
                System.out.println(RED + "\n[!] Could not remove student." + RESET);
            }
        } else {
            System.out.println(YELLOW + "\n[i] Operation cancelled. Student record was not deleted." + RESET);
        }
    }

    // ==========================================
    // ROBUST INPUT VALIDATION UTILITIES
    // ==========================================

    /**
     * Reads a non-empty string, continuously prompting until valid input is given.
     */
    private static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(RED + "[!] Input cannot be empty. Please enter a valid value." + RESET);
        }
    }

    /**
     * Reads an integer within [min, max], handling non-numeric input gracefully.
     */
    private static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(line);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf(RED + "[!] Please enter an integer between %d and %d.%n" + RESET, min, max);
            } catch (NumberFormatException e) {
                System.out.println(RED + "[!] Invalid input. Please enter a valid whole number." + RESET);
            }
        }
    }

    /**
     * Reads a double within [min, max], handling non-numeric input gracefully.
     */
    private static double readDouble(Scanner scanner, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double val = Double.parseDouble(line);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf(RED + "[!] Value must be between %.1f and %.1f.%n" + RESET, min, max);
            } catch (NumberFormatException e) {
                System.out.println(RED + "[!] Invalid numeric format. Please enter a valid number (e.g. 85.5)." + RESET);
            }
        }
    }

    /**
     * Reads a Y/N or Yes/No confirmation from the user.
     */
    private static String readConfirmation(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("Y") || line.equalsIgnoreCase("YES")) {
                return "Y";
            } else if (line.equalsIgnoreCase("N") || line.equalsIgnoreCase("NO")) {
                return "N";
            }
            System.out.println(RED + "[!] Please enter 'Y' for Yes or 'N' for No." + RESET);
        }
    }
}
