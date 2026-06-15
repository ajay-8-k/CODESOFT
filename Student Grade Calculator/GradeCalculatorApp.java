import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * The main application class providing a menu-driven interface to manage
 * students, view individual report cards, compare students, and export report cards.
 */
public class GradeCalculatorApp {
    private static final ArrayList<Student> students = new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("+-----------------------------------------------------------------------+");
        System.out.println("|                WELCOME TO THE STUDENT GRADE CALCULATOR                |");
        System.out.println("+-----------------------------------------------------------------------+");
        
        // Add dummy data for testing if needed or let the user add. Let's seed two dummy students so the user can immediately play with comparison/reports if they want, but also keep it clean.
        seedInitialData();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readIntInput("Enter your choice (1-4): ", 1, 4);
            System.out.println();

            switch (choice) {
                case 1:
                    addNewStudent();
                    break;
                case 2:
                    viewReportCard();
                    break;
                case 3:
                    compareStudents();
                    break;
                case 4:
                    System.out.println("Thank you for using Student Grade Calculator! Exiting... Goodbye!");
                    running = false;
                    break;
            }
            if (running) {
                System.out.println("\nPress Enter to return to main menu...");
                sc.nextLine();
            }
        }
        sc.close();
    }

    /**
     * Seeds initial records for easy testing and immediate comparison availability.
     */
    private static void seedInitialData() {
        students.add(new Student("Sophia Vance", "S101", new double[]{92.5, 88.0, 95.0, 84.0, 96.5}, 89.5));
        students.add(new Student("Michael Scott", "S102", new double[]{55.0, 62.0, 48.0, 70.0, 65.0}, 72.0));
        students.add(new Student("Pam Beesly", "S103", new double[]{81.0, 85.5, 78.0, 90.0, 88.0}, 94.0));
    }

    /**
     * Prints the primary user menu options.
     */
    private static void printMainMenu() {
        System.out.println("\n=========================================================================");
        System.out.println("                              MAIN MENU                                  ");
        System.out.println("=========================================================================");
        System.out.println(" [1] Add New Student");
        System.out.println(" [2] View Report Card & Save to File");
        System.out.println(" [3] Compare Students & Leaderboard");
        System.out.println(" [4] Exit Program");
        System.out.println("=========================================================================");
    }

    /**
     * Gathers input and adds a new student to the system.
     */
    private static void addNewStudent() {
        System.out.println("---------------------------------------------------------");
        System.out.println("                     ADD NEW STUDENT                     ");
        System.out.println("---------------------------------------------------------");

        String name = readNonEmptyString("Enter Student Name: ");
        String rollNumber = readUniqueRollNumber("Enter Roll Number (e.g. S104): ");

        double[] marks = new double[5];
        System.out.println("Enter marks for the following 5 subjects (0 to 100):");
        for (int i = 0; i < Student.SUBJECTS.length; i++) {
            marks[i] = readDoubleInput("  - " + Student.SUBJECTS[i] + ": ", 0.0, 100.0);
        }

        double attendance = readDoubleInput("Enter Attendance Percentage (0 to 100): ", 0.0, 100.0);

        Student student = new Student(name, rollNumber, marks, attendance);
        students.add(student);

        System.out.println("\n+-------------------------------------------------------+");
        System.out.println("|            STUDENT RECORD ADDED SUCCESSFULLY          |");
        System.out.println("+-------------------------------------------------------+");
        System.out.printf(" Name       : %s\n", student.getName());
        System.out.printf(" Roll No    : %s\n", student.getRollNumber());
        System.out.printf(" Average %%  : %.2f%%\n", student.calculateAveragePercentage());
        System.out.printf(" Overall Gr : %s\n", student.getGrade());
        System.out.println("---------------------------------------------------------");
    }

    /**
     * Lists current students, lets the user select one, displays details,
     * and prompts to save reports.
     */
    private static void viewReportCard() {
        System.out.println("---------------------------------------------------------");
        System.out.println("                   VIEW REPORT CARDS                     ");
        System.out.println("---------------------------------------------------------");

        if (students.isEmpty()) {
            System.out.println("[WARNING] No student records available. Please add a student first.");
            return;
        }

        System.out.println("Current Students in System:");
        System.out.printf("  %-10s | %-30s\n", "Roll No", "Student Name");
        System.out.println("  -------------------------------------------");
        for (Student s : students) {
            System.out.printf("  %-10s | %-30s\n", s.getRollNumber(), s.getName());
        }
        System.out.println("  -------------------------------------------");

        String roll = readNonEmptyString("Enter Roll Number of the student: ");
        Student foundStudent = null;
        for (Student s : students) {
            if (s.getRollNumber().equalsIgnoreCase(roll)) {
                foundStudent = s;
                break;
            }
        }

        if (foundStudent == null) {
            System.out.println("[ERROR] Student with Roll Number '" + roll + "' not found.");
            return;
        }

        System.out.println("\n" + foundStudent.generateReportCardText());

        String saveChoice = readNonEmptyString("Save this report card to a text file? (Y/N): ");
        if (saveChoice.equalsIgnoreCase("y") || saveChoice.equalsIgnoreCase("yes")) {
            try {
                String path = foundStudent.saveReportCardToFile();
                System.out.println("\n[SUCCESS] Report card saved successfully to file:");
                System.out.println("          " + path);
            } catch (IOException e) {
                System.out.println("[ERROR] Failed to save report card file: " + e.getMessage());
            }
        }
    }

    /**
     * Compares all students based on performance, sorts them, and displays
     * the leaderboard.
     */
    private static void compareStudents() {
        System.out.println("---------------------------------------------------------");
        System.out.println("                 LEADERBOARD & COMPARISON                ");
        System.out.println("---------------------------------------------------------");

        if (students.size() < 2) {
            System.out.printf("[INFO] There are currently %d student(s) registered.\n", students.size());
            System.out.println("       Add at least 2 students to perform comparison/ranking.");
            return;
        }

        // Copy list to sort
        List<Student> leaderboard = new ArrayList<>(students);
        // Sort descending by average percentage
        leaderboard.sort(new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Double.compare(s2.calculateAveragePercentage(), s1.calculateAveragePercentage());
            }
        });

        System.out.printf("  %-4s | %-10s | %-20s | %-12s | %-9s | %-5s | %-11s | %-10s\n", 
            "Rank", "Roll No", "Name", "Total Marks", "Average %", "Grade", "Scholarship", "Attendance");
        System.out.println("  " + "=".repeat(100));

        for (int i = 0; i < leaderboard.size(); i++) {
            Student s = leaderboard.get(i);
            String isScholar = s.isEligibleForScholarship() ? "Yes" : "No";
            String rankStr = (i == 0) ? "1(TOP)" : String.valueOf(i + 1);

            System.out.printf("  %-4s | %-10s | %-20s | %-12.1f | %-8.2f%% | %-5s | %-11s | %-9.1f%%\n", 
                rankStr, s.getRollNumber(), s.getName(), s.calculateTotalMarks(), 
                s.calculateAveragePercentage(), s.getGrade(), isScholar, s.getAttendancePercentage());
        }
        System.out.println("  " + "=".repeat(100));

        Student topper = leaderboard.get(0);
        System.out.println("\n*** TOPPER OF THE BATCH ***");
        System.out.println("+-------------------------------------------------------+");
        System.out.printf("  Name               : %s\n", topper.getName());
        System.out.printf("  Roll Number        : %s\n", topper.getRollNumber());
        System.out.printf("  Average Percentage : %.2f%%\n", topper.calculateAveragePercentage());
        System.out.printf("  Total Marks        : %.1f / 500.0\n", topper.calculateTotalMarks());
        System.out.printf("  Overall Grade      : %s\n", topper.getGrade());
        System.out.println("+-------------------------------------------------------+");
    }

    // --- Helper Methods for Input Validation ---

    /**
     * Reads a valid integer within a given range.
     */
    private static int readIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                int val = Integer.parseInt(input);
                if (val >= min && val <= max) {
                    return val;
                } else {
                    System.out.printf("  [ERROR] Value must be between %d and %d. Please try again.\n", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.println("  [ERROR] Invalid input. Please enter a valid whole number.");
            }
        }
    }

    /**
     * Reads a valid double within a given range.
     */
    private static double readDoubleInput(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                double val = Double.parseDouble(input);
                if (val >= min && val <= max) {
                    return val;
                } else {
                    System.out.printf("  [ERROR] Value must be between %.1f and %.1f. Please try again.\n", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.println("  [ERROR] Invalid input. Please enter a valid number (e.g. 85.5).");
            }
        }
    }

    /**
     * Reads a non-empty string.
     */
    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("  [ERROR] Input cannot be blank. Please try again.");
        }
    }

    /**
     * Reads a unique roll number.
     */
    private static String readUniqueRollNumber(String prompt) {
        while (true) {
            String roll = readNonEmptyString(prompt);
            boolean exists = false;
            for (Student s : students) {
                if (s.getRollNumber().equalsIgnoreCase(roll)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                return roll;
            }
            System.out.printf("  [ERROR] Roll Number '%s' already exists. Please choose a unique Roll Number.\n", roll);
        }
    }
}
