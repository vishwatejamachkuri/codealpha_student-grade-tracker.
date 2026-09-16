# Student Grade Tracker

A robust, object-oriented application written in pure Java (standard library only, zero external dependencies). Designed for educators to track, manage, analyze, and report student grades with real-time class statistics and validation. Supports both **Console (CLI)** and **Interactive Graphical User Interface (GUI)** modes.

---

## Project Structure

```text
student-grade-tracker/
├── src/
│   ├── Student.java         # Model representing an individual student and score calculations
│   ├── GradeTracker.java    # Core controller managing ArrayList<Student> & statistical analytics
│   ├── GradeTrackerGUI.java # Modern Swing desktop GUI with live table, cards & modals
│   └── Main.java            # Interactive console CLI interface with input validation
├── test_input.txt           # Automated CLI test script commands
└── README.md                # Project documentation
```

---

## Key Features

- **Dynamic Data Storage**: Uses `ArrayList<Student>` for dynamic scaling, insertion, and deletion, and `ArrayList<Double>` for flexible per-student subject marks.
- **Data Encapsulation**: Strict private fields with defensive copying in getters and setters to preserve object integrity.
- **Dual User Interface Support**:
  - **Console CLI (`Main.java`)**: Clean terminal menu, non-empty text validation, range-checked numbers, and confirmation prompts.
  - **Desktop GUI (`GradeTrackerGUI.java`)**: Live JTable, instant ID/name search filter, real-time analytics metric cards, modal dialogs, and a summary report viewer with clipboard copy.
- **Full CRUD Support**:
  - **Add Student**: Validates non-empty names/IDs, checks ID uniqueness, and bounds subject marks between `0.0` and `100.0`.
  - **View All Students**: Displays a formatted table with ID, Name, Total Marks, Average Percentage, Highest Score, Lowest Score, and Final Grade.
  - **Search Student**: Finds any student by ID and prints a full individual report card or filters rows in GUI.
  - **Update Grades**: Modifies marks for any student and immediately recalculates all metrics.
  - **Remove Student**: Safely deletes a student from the ArrayList with confirmation dialog (`Y`/`N`).
- **Comprehensive Class Analytics**:
  - Overall cohort class average.
  - Top and lowest performing student identification.
  - Highest and lowest single subject score in the entire cohort.
  - Grade distribution breakdown (`A`, `B`, `C`, `D`, `F`).
- **Bulletproof Input Handling**:
  - Catches `NumberFormatException` to prevent CLI/GUI crashes from non-numeric input.
  - Solves the classic Java `Scanner` newline skip issue.

---

## Compilation and Running Instructions (Windows Powershell / VS Code Terminal)

### 1. Compile All Files
Open terminal in `C:\Users\Dell\.gemini\antigravity\scratch\student-grade-tracker`:

```powershell
# Compile all source files into the bin directory
javac -d bin src/*.java
```

### 2. Run Desktop GUI Application
```powershell
# Option A: Launch GUI directly
java -cp bin GradeTrackerGUI

# Option B: Launch GUI via Main flag
java -cp bin Main --gui
```

### 3. Run Console CLI Application
```powershell
java -cp bin Main
```

---

## Automated Smoke Test
You can run an automated test that executes all 8 console menu options:
```powershell
Get-Content test_input.txt | java -cp bin Main
```

