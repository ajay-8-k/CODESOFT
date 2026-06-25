package com.sms;

import com.sms.model.Student;
import com.sms.service.StudentManagementSystem;
import java.io.File;
import java.util.List;

/**
 * Automated verification test suite for the Student Management System.
 */
public class TestSMS {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("      Running Automated SMS Integration Tests      ");
        System.out.println("==================================================");

        int totalTests = 0;
        int passedTests = 0;

        // Test 1: Add Students and Verify
        totalTests++;
        System.out.print("Test 1: Adding students... ");
        StudentManagementSystem sms = new StudentManagementSystem();
        Student s1 = new Student(1, "Alice Smith", 101, "Computer Science", 92.5, 95.0);
        Student s2 = new Student(2, "Bob Jones", 102, "Electrical Eng", 88.0, 60.0); // Low attendance
        Student s3 = new Student(3, "Charlie Brown", 103, "Mechanical Eng", 96.0, 85.0); // Topper

        boolean added1 = sms.addStudent(s1);
        boolean added2 = sms.addStudent(s2);
        boolean added3 = sms.addStudent(s3);

        if (added1 && added2 && added3 && sms.getTotalStudentCount() == 3) {
            System.out.println("PASSED");
            passedTests++;
        } else {
            System.out.println("FAILED");
        }

        // Test 2: Add Duplicate ID & Roll Number Validation
        totalTests++;
        System.out.print("Test 2: Duplicate validation... ");
        // Duplicate ID
        Student sDuplicateId = new Student(1, "Duplicate ID", 104, "Chemistry", 75.0, 80.0);
        // Duplicate Roll Number
        Student sDuplicateRoll = new Student(4, "Duplicate Roll", 102, "Physics", 70.0, 90.0);

        boolean duplicateIdCheck = sms.addStudent(sDuplicateId);
        boolean duplicateRollCheck = sms.addStudent(sDuplicateRoll);

        if (!duplicateIdCheck && !duplicateRollCheck && sms.getTotalStudentCount() == 3) {
            System.out.println("PASSED");
            passedTests++;
        } else {
            System.out.println("FAILED");
        }

        // Test 3: Average Marks and Find Topper
        totalTests++;
        System.out.print("Test 3: Analytics (Average & Topper)... ");
        double avg = sms.calculateAverageMarks();
        Student topper = sms.findTopper();
        
        // Expected Average: (92.5 + 88.0 + 96.0) / 3 = 92.166...
        boolean avgMatch = Math.abs(avg - 92.17) < 0.01;
        boolean topperMatch = topper != null && topper.getStudentId() == 3; // Charlie is 96.0

        if (avgMatch && topperMatch) {
            System.out.println("PASSED");
            passedTests++;
        } else {
            System.out.printf("FAILED (Avg: %.2f, Topper ID: %s)\n", avg, (topper != null ? topper.getStudentId() : "none"));
        }

        // Test 4: Partial Name Search
        totalTests++;
        System.out.print("Test 4: Partial name search... ");
        List<Student> results = sms.searchStudentsByName("li"); // Should match "Alice Smith" and "Charlie Brown"
        
        if (results.size() == 2) {
            System.out.println("PASSED");
            passedTests++;
        } else {
            System.out.println("FAILED (Found: " + results.size() + ")");
        }

        // Test 5: Low Attendance List
        totalTests++;
        System.out.print("Test 5: Low attendance check... ");
        List<Student> lowAttendance = sms.getLowAttendanceStudents(75.0); // Should only match Bob Jones (60.0%)
        
        if (lowAttendance.size() == 1 && lowAttendance.get(0).getStudentId() == 2) {
            System.out.println("PASSED");
            passedTests++;
        } else {
            System.out.println("FAILED");
        }

        // Test 6: Save and Load from file
        totalTests++;
        System.out.print("Test 6: File persistence... ");
        String testFile = "students_test.txt";
        
        // Clean up previous run files if exists
        File f = new File(testFile);
        if (f.exists()) {
            f.delete();
        }

        boolean saved = sms.saveToFile(testFile);
        
        StudentManagementSystem smsLoaded = new StudentManagementSystem();
        boolean loaded = smsLoaded.loadFromFile(testFile);
        
        boolean persistenceMatch = smsLoaded.getTotalStudentCount() == 3 
                && smsLoaded.searchStudent(1).getName().equals("Alice Smith")
                && smsLoaded.searchStudent(2).getAttendance() == 60.0
                && smsLoaded.searchStudent(3).getMarks() == 96.0;

        if (saved && loaded && persistenceMatch) {
            System.out.println("PASSED");
            passedTests++;
        } else {
            System.out.println("FAILED");
        }

        // Clean up test file
        if (f.exists()) {
            f.delete();
        }

        // Test 7: Report Generation
        totalTests++;
        System.out.print("Test 7: Report generation... ");
        String reportFile = "student_report_test.txt";
        File rf = new File(reportFile);
        if (rf.exists()) {
            rf.delete();
        }

        boolean reportGenerated = sms.generateReport(reportFile);
        boolean reportExists = rf.exists() && rf.length() > 0;

        if (reportGenerated && reportExists) {
            System.out.println("PASSED");
            passedTests++;
        } else {
            System.out.println("FAILED");
        }

        if (rf.exists()) {
            rf.delete();
        }

        // Summary
        System.out.println("==================================================");
        System.out.printf("Test Execution Summary: %d / %d Tests Passed.\n", passedTests, totalTests);
        System.out.println("==================================================");

        if (passedTests == totalTests) {
            System.exit(0);
        } else {
            System.exit(1);
        }
    }
}
