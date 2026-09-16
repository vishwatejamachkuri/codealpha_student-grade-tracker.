# 📊 Student Grade Tracker (Java GUI)

A desktop-based Java application featuring a Graphical User Interface (GUI) designed to help educators seamlessly manage student records, track academic scores, and calculate automated grading metrics.

## 🚀 Features

- **Interactive GUI Dashboard:** User-friendly forms and data tables built with Java Swing/AWT.
- **Automated Grade & GPA Engine:** Instantly converts raw assignment percentages into weighted scores, letter grades, and GPA scales.
- **Performance Analytics:** Quickly view class summaries including highest marks, lowest marks, and overall class averages.
- **Data Organization:** Keeps project files organized by separating compiled runtime files from raw source code.

## ⚙️ How to Compile & Run

Make sure you have the **Java Development Kit (JDK 8 or higher)** installed on your machine. Open your terminal or command prompt in the root project folder and execute the following commands:

### 1. Compile the Source Code
This command compiles all Java source files from the `src/` directory and outputs the production-ready `.class` binaries into a clean `bin/` folder:
```bash
javac -d bin src/*.java
```

### 2. Launch the GUI Application
Run the main executable window by pointing Java to the compiled class files in your binary path:
```bash
java -cp bin GradeTrackerGUI
```

## 📂 Project Directory Structure

```text
├── src/                # Raw Java source files (.java)
│   └── GradeTrackerGUI.java
├── bin/                # Compiled Java bytecode files (.class)
├── README.md           # Project documentation
└── .gitignore          # Keeps compiled files out of version control
```
