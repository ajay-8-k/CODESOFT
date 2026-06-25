package com.sms;

import com.sms.model.Student;
import com.sms.service.StudentManagementSystem;
import com.sms.util.ValidationUtils;
import java.util.List;
import java.util.Scanner;

/**
 * Main application entry point for the Student Management System.
 * Manages the CLI console loop.
 */
public class Main {
    private static final String DATA_FILE = "students.txt";
    private static final String REPORT_FILE = "student_report.txt";
    private static final StudentManagementSystem sms = new StudentManagementSystem();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("      Initializing Student Management System      ");
        System.out.println("==================================================");
        
        // Load data on startup
        if (sms.loadFromFile(DATA_FILE)) {
            System.out.println("Data loaded successfully from " + DATA_FILE);
            System.out.println("Total students loaded: " + sms.getTotalStudentCount());
        } else {
            System.out.println("No existing student record found. Starting fresh.");
        }

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = ValidationUtils.readInt(scanner, "Enter your choice (1-10): ", "Invalid entry. Please enter a valid number.", 1);
            System.out.println();
            
            switch (choice) {
                case 1:
                    handleAddStudent();
                    break;
                case 2:
                    handleRemoveStudent();
                    break;
                case 3:
                    handleSearchStudent();
                    break;
                case 4:
                    handleUpdateStudent();
                    break;
                case 5:
                    System.out.println("--- ALL REGISTERED STUDENTS ---");
                    sms.displayAllStudents();
                    break;
                case 6:
                    sms.sortStudentsByMarks();
                    System.out.println("Students successfully sorted by marks in descending order.");
                    sms.displayAllStudents();
                    saveData(); // Save changes
                    break;
                case 7:
                    handleFindTopper();
                    break;
                case 8:
                    handleLowAttendance();
                    break;
                case 9:
                    handleAdvancedAnalytics();
                    break;
                case 10:
                    System.out.println("Saving data to " + DATA_FILE + "...");
                    saveData();
                    System.out.println("Thank you for using Student Management System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid selection. Please choose a number between 1 and 10.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("==================================================");
        System.out.println("        STUDENT MANAGEMENT SYSTEM - MENU          ");
        System.out.println("==================================================");
        System.out.println("1. Add Student");
        System.out.println("2. Remove Student");
        System.out.println("3. Search Student (by ID)");
        System.out.println("4. Update Student Details");
        System.out.println("5. View All Students");
        System.out.println("6. Sort Students by Marks");
        System.out.println("7. Find Class Topper");
        System.out.println("8. View Low Attendance Students (<75%)");
        System.out.println("9. Advanced Analytics & Reports");
        System.out.println("10. Save and Exit");
        System.out.println("==================================================");
    }

    private static void handleAddStudent() {
        System.out.println("--- ADD NEW STUDENT ---");
        int id = ValidationUtils.readInt(scanner, "Enter Student ID: ", "Invalid ID. Must be a positive integer.", 1);
        
        // Fast-fail if student already exists to save user typing other fields
        if (sms.searchStudent(id) != null) {
            System.out.println("Error: A student with ID " + id + " already exists.");
            return;
        }

        String name = ValidationUtils.readNonEmptyString(scanner, "Enter Name: ", "Name cannot be empty.");
        int roll = ValidationUtils.readInt(scanner, "Enter Roll Number: ", "Invalid Roll Number. Must be a positive integer.", 1);
        String dept = ValidationUtils.readNonEmptyString(scanner, "Enter Department: ", "Department cannot be empty.");
        double marks = ValidationUtils.readDoubleInRange(scanner, "Enter Marks (0-100): ", 0.0, 100.0, "Invalid Marks. Enter a value from 0 to 100.");
        double att = ValidationUtils.readDoubleInRange(scanner, "Enter Attendance (0-100)%%: ", 0.0, 100.0, "Invalid Attendance. Enter a value from 0 to 100.");

        Student student = new Student(id, name, roll, dept, marks, att);
        if (sms.addStudent(student)) {
            System.out.println("Student added successfully!");
            saveData();
        } else {
            System.out.println("Failed to add student due to duplicate attributes.");
        }
    }

    private static void handleRemoveStudent() {
        System.out.println("--- REMOVE STUDENT ---");
        int id = ValidationUtils.readInt(scanner, "Enter Student ID to remove: ", "Invalid ID. Must be a positive integer.", 1);
        if (sms.removeStudent(id)) {
            System.out.println("Student with ID " + id + " was removed successfully.");
            saveData();
        } else {
            System.out.println("Error: Student with ID " + id + " not found.");
        }
    }

    private static void handleSearchStudent() {
        System.out.println("--- SEARCH STUDENT BY ID ---");
        int id = ValidationUtils.readInt(scanner, "Enter Student ID: ", "Invalid ID. Must be a positive integer.", 1);
        Student s = sms.searchStudent(id);
        if (s != null) {
            System.out.println("Student Found:");
            System.out.println("+" + "-".repeat(78) + "+");
            System.out.println(s);
            System.out.println("+" + "-".repeat(78) + "+");
        } else {
            System.out.println("Student with ID " + id + " not found.");
        }
    }

    private static void handleUpdateStudent() {
        System.out.println("--- UPDATE STUDENT ---");
        int id = ValidationUtils.readInt(scanner, "Enter Student ID to update: ", "Invalid ID. Must be a positive integer.", 1);
        Student s = sms.searchStudent(id);
        if (s == null) {
            System.out.println("Error: Student with ID " + id + " does not exist.");
            return;
        }

        System.out.println("Current Record: " + s);
        System.out.println("Enter new details:");

        String name = ValidationUtils.readNonEmptyString(scanner, "Enter Name [" + s.getName() + "]: ", "Name cannot be empty.");
        int roll = ValidationUtils.readInt(scanner, "Enter Roll Number [" + s.getRollNumber() + "]: ", "Invalid Roll Number.", 1);
        String dept = ValidationUtils.readNonEmptyString(scanner, "Enter Department [" + s.getDepartment() + "]: ", "Department cannot be empty.");
        double marks = ValidationUtils.readDoubleInRange(scanner, "Enter Marks [" + s.getMarks() + "]: ", 0.0, 100.0, "Invalid Marks. Enter a value from 0 to 100.");
        double att = ValidationUtils.readDoubleInRange(scanner, "Enter Attendance [" + s.getAttendance() + "%%]: ", 0.0, 100.0, "Invalid Attendance. Enter a value from 0 to 100.");

        if (sms.updateStudent(id, name, roll, dept, marks, att)) {
            System.out.println("Student details updated successfully.");
            saveData();
        } else {
            System.out.println("Failed to update student details.");
        }
    }

    private static void handleFindTopper() {
        System.out.println("--- CLASS TOPPER ---");
        Student topper = sms.findTopper();
        if (topper != null) {
            System.out.println("Topper Student details:");
            System.out.println("Name:        " + topper.getName());
            System.out.println("ID:          " + topper.getStudentId());
            System.out.println("Roll Number: " + topper.getRollNumber());
            System.out.println("Department:  " + topper.getDepartment());
            System.out.println("Marks:       " + topper.getMarks());
            System.out.println("Attendance:  " + topper.getAttendance() + "%");
        } else {
            System.out.println("No students found in the class.");
        }
    }

    private static void handleLowAttendance() {
        System.out.println("--- STUDENTS WITH LOW ATTENDANCE (< 75%) ---");
        List<Student> lowAttendance = sms.getLowAttendanceStudents(75.0);
        if (lowAttendance.isEmpty()) {
            System.out.println("No students have attendance below 75%. Great job!");
        } else {
            System.out.println("+" + "-".repeat(78) + "+");
            for (Student s : lowAttendance) {
                System.out.println(s);
            }
            System.out.println("+" + "-".repeat(78) + "+");
        }
    }

    private static void handleAdvancedAnalytics() {
        boolean back = false;
        while (!back) {
            System.out.println("--------------------------------------------------");
            System.out.println("          ADVANCED ANALYTICS & REPORTS            ");
            System.out.println("--------------------------------------------------");
            System.out.println("1. Show Class Average Marks");
            System.out.println("2. Show Total Registered Students");
            System.out.println("3. Search Student by Name (Partial Match)");
            System.out.println("4. Generate Full Text Report (" + REPORT_FILE + ")");
            System.out.println("5. Go Back to Main Menu");
            System.out.println("--------------------------------------------------");
            
            int subChoice = ValidationUtils.readInt(scanner, "Enter selection (1-5): ", "Invalid entry. Enter a valid number.", 1);
            System.out.println();
            
            switch (subChoice) {
                case 1:
                    double avg = sms.calculateAverageMarks();
                    System.out.printf("Class Average Marks: %.2f / 100.0\n", avg);
                    break;
                case 2:
                    System.out.println("Total Registered Students: " + sms.getTotalStudentCount());
                    break;
                case 3:
                    System.out.print("Enter partial name to search: ");
                    String query = scanner.nextLine();
                    List<Student> results = sms.searchStudentsByName(query);
                    if (results.isEmpty()) {
                        System.out.println("No students matching '" + query + "' found.");
                    } else {
                        System.out.println("Search Results (" + results.size() + "):");
                        System.out.println("+" + "-".repeat(78) + "+");
                        for (Student s : results) {
                            System.out.println(s);
                        }
                        System.out.println("+" + "-".repeat(78) + "+");
                    }
                    break;
                case 4:
                    if (sms.generateReport(REPORT_FILE)) {
                        System.out.println("Report successfully written to: " + REPORT_FILE);
                    } else {
                        System.out.println("Failed to generate student report.");
                    }
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid entry. Choose a number between 1 and 5.");
            }
            System.out.println();
        }
    }

    private static void saveData() {
        if (sms.saveToFile(DATA_FILE)) {
            // Silently saved or simple message to avoid cluttering terminal
            System.out.println(" [Autosave] Data saved successfully to " + DATA_FILE);
        } else {
            System.out.println(" WARNING: Failed to autosave data to " + DATA_FILE);
        }
    }
}
