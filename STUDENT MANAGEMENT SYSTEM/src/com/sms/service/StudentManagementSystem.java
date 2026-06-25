package com.sms.service;

import com.sms.model.Student;
import java.io.*;
import java.util.ArrayList;

import java.util.List;

/**
 * Service class that manages the list of students, persistence, and business operations.
 */
public class StudentManagementSystem {
    private final List<Student> students;

    public StudentManagementSystem() {
        this.students = new ArrayList<>();
    }

    /**
     * Adds a student to the system.
     * Validates that the ID and Roll Number are unique.
     */
    public boolean addStudent(Student student) {
        if (student == null) return false;
        
        // Check uniqueness of studentId and rollNumber
        for (Student s : students) {
            if (s.getStudentId() == student.getStudentId()) {
                System.out.println("Error: Student ID " + student.getStudentId() + " already exists.");
                return false;
            }
            if (s.getRollNumber() == student.getRollNumber()) {
                System.out.println("Error: Roll Number " + student.getRollNumber() + " already exists.");
                return false;
            }
        }
        students.add(student);
        return true;
    }

    /**
     * Removes a student by studentId.
     */
    public boolean removeStudent(int studentId) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId() == studentId) {
                students.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Searches for a student by studentId.
     */
    public Student searchStudent(int studentId) {
        for (Student s : students) {
            if (s.getStudentId() == studentId) {
                return s;
            }
        }
        return null;
    }

    /**
     * Updates an existing student's record.
     */
    public boolean updateStudent(int studentId, String name, int rollNumber, String department, double marks, double attendance) {
        Student student = searchStudent(studentId);
        if (student == null) {
            return false;
        }

        // Check roll number uniqueness if it is being changed
        if (student.getRollNumber() != rollNumber) {
            for (Student s : students) {
                if (s.getRollNumber() == rollNumber) {
                    System.out.println("Error: Roll Number " + rollNumber + " is already taken by another student.");
                    return false;
                }
            }
        }

        student.setName(name);
        student.setRollNumber(rollNumber);
        student.setDepartment(department);
        student.setMarks(marks);
        student.setAttendance(attendance);
        return true;
    }

    /**
     * Prints all students to the console.
     */
    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }
        printStudentTableHeader();
        for (Student s : students) {
            System.out.println(s);
        }
        printStudentTableFooter();
    }

    /**
     * Sorts the student list by marks in descending order (highest to lowest).
     */
    public void sortStudentsByMarks() {
        students.sort((s1, s2) -> Double.compare(s2.getMarks(), s1.getMarks()));
    }

    /**
     * Finds the topper (student with highest marks).
     */
    public Student findTopper() {
        if (students.isEmpty()) {
            return null;
        }
        Student topper = students.get(0);
        for (Student s : students) {
            if (s.getMarks() > topper.getMarks()) {
                topper = s;
            }
        }
        return topper;
    }

    /**
     * Retrieves students with attendance below a specified threshold.
     */
    public List<Student> getLowAttendanceStudents(double threshold) {
        List<Student> lowAttendance = new ArrayList<>();
        for (Student s : students) {
            if (s.getAttendance() < threshold) {
                lowAttendance.add(s);
            }
        }
        return lowAttendance;
    }

    /**
     * Calculates the class average marks.
     */
    public double calculateAverageMarks() {
        if (students.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (Student s : students) {
            sum += s.getMarks();
        }
        return sum / students.size();
    }

    /**
     * Returns the total count of students.
     */
    public int getTotalStudentCount() {
        return students.size();
    }

    /**
     * Searches students by a partial, case-insensitive match on name.
     */
    public List<Student> searchStudentsByName(String query) {
        List<Student> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return results;
        }
        String lowerQuery = query.toLowerCase().trim();
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(lowerQuery)) {
                results.add(s);
            }
        }
        return results;
    }

    /**
     * Generates a beautifully formatted summary report file.
     */
    public boolean generateReport(String filepath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filepath))) {
            writer.println("==========================================================================");
            writer.println("                      STUDENT MANAGEMENT SYSTEM REPORT                     ");
            writer.println("==========================================================================");
            writer.println("Total Registered Students : " + getTotalStudentCount());
            writer.println(String.format("Class Average Marks       : %.2f", calculateAverageMarks()));
            
            Student topper = findTopper();
            if (topper != null) {
                writer.println(String.format("Class Topper              : %s (ID: %d, Marks: %.2f)", 
                    topper.getName(), topper.getStudentId(), topper.getMarks()));
            } else {
                writer.println("Class Topper              : N/A");
            }
            
            List<Student> lowAtt = getLowAttendanceStudents(75.0);
            writer.println("Students with < 75% Attend: " + lowAtt.size());
            writer.println("==========================================================================");
            writer.println("                               STUDENT LIST                               ");
            writer.println("==========================================================================");
            writer.println(String.format("| %-6s | %-18s | %-5s | %-12s | %-6s | %-10s |", 
                "ID", "Name", "Roll", "Department", "Marks", "Attendance"));
            writer.println("--------------------------------------------------------------------------");
            
            for (Student s : students) {
                writer.println(String.format("| %-6d | %-18s | %-5d | %-12s | %-6.2f | %-9.1f%% |",
                    s.getStudentId(), s.getName(), s.getRollNumber(), s.getDepartment(), s.getMarks(), s.getAttendance()));
            }
            writer.println("==========================================================================");
            return true;
        } catch (IOException e) {
            System.out.println("Error generating report: " + e.getMessage());
            return false;
        }
    }

    /**
     * Saves the current students state to file in CSV format.
     */
    public boolean saveToFile(String filepath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            for (Student s : students) {
                bw.write(s.toCsvString());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving student data to file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads student data from a CSV formatted file.
     */
    public boolean loadFromFile(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            return false;
        }
        students.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Student student = Student.fromCsvString(line);
                if (student != null) {
                    students.add(student);
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error loading student data from file: " + e.getMessage());
            return false;
        }
    }

    // Helper formatting methods for console output
    private void printStudentTableHeader() {
        System.out.println("+" + "-".repeat(78) + "+");
        System.out.println(String.format(
            "| %-6s | %-18s | %-5s | %-12s | %-6s | %-10s |",
            "ID", "Name", "Roll", "Department", "Marks", "Attendance"
        ));
        System.out.println("+" + "-".repeat(78) + "+");
    }

    private void printStudentTableFooter() {
        System.out.println("+" + "-".repeat(78) + "+");
    }
}
