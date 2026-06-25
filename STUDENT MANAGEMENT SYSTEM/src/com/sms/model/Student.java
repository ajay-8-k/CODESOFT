package com.sms.model;

/**
 * Model class representing a Student.
 */
public class Student {
    private int studentId;
    private String name;
    private int rollNumber;
    private String department;
    private double marks;
    private double attendance;

    // Default Constructor
    public Student() {
    }

    // Parameterized Constructor
    public Student(int studentId, String name, int rollNumber, String department, double marks, double attendance) {
        this.studentId = studentId;
        this.name = name;
        this.rollNumber = rollNumber;
        this.department = department;
        this.marks = marks;
        this.attendance = attendance;
    }

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public double getAttendance() {
        return attendance;
    }

    public void setAttendance(double attendance) {
        this.attendance = attendance;
    }

    /**
     * Converts the student object to a CSV formatted string for saving to a file.
     * Replaces any existing commas or double quotes in text fields to prevent syntax issues.
     */
    public String toCsvString() {
        String sanitizedName = name.replace(",", " ").replace("\"", "'").trim();
        String sanitizedDept = department.replace(",", " ").replace("\"", "'").trim();
        return studentId + "," + sanitizedName + "," + rollNumber + "," + sanitizedDept + "," + marks + "," + attendance;
    }

    /**
     * Creates a Student object from a CSV line.
     * @param csvLine The comma-separated values string.
     * @return A valid Student object, or null if parsing fails.
     */
    public static Student fromCsvString(String csvLine) {
        try {
            String[] tokens = csvLine.split(",", -1);
            if (tokens.length < 6) {
                return null;
            }
            int id = Integer.parseInt(tokens[0].trim());
            String name = tokens[1].trim();
            int roll = Integer.parseInt(tokens[2].trim());
            String dept = tokens[3].trim();
            double marks = Double.parseDouble(tokens[4].trim());
            double att = Double.parseDouble(tokens[5].trim());

            return new Student(id, name, roll, dept, marks, att);
        } catch (NumberFormatException e) {
            // Log or ignore corrupted rows silently or handle in service load
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format(
            "| ID: %-6d | Name: %-18s | Roll: %-5d | Dept: %-12s | Marks: %-6.2f | Attendance: %-5.1f%% |",
            studentId, name, rollNumber, department, marks, attendance
        );
    }
}
