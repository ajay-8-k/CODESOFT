import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * The Student class represents a student, holding their name, roll number,
 * marks for 5 subjects, and attendance percentage. It includes various
 * methods to perform grade calculation, rankings, and check eligibilities.
 */
public class Student {
    private String name;
    private String rollNumber;
    private double[] marks; // Size 5
    private double attendancePercentage;

    // Define standard subjects
    public static final String[] SUBJECTS = {
        "Mathematics", 
        "Science", 
        "English", 
        "Social Studies", 
        "Computer Science"
    };

    /**
     * Constructor to initialize Student details.
     */
    public Student(String name, String rollNumber, double[] marks, double attendancePercentage) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.marks = Arrays.copyOf(marks, 5);
        this.attendancePercentage = attendancePercentage;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public double[] getMarks() {
        return marks;
    }

    public void setMarks(double[] marks) {
        this.marks = Arrays.copyOf(marks, 5);
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    /**
     * Calculates the total marks of the student.
     * @return Sum of marks in all 5 subjects.
     */
    public double calculateTotalMarks() {
        double total = 0;
        for (double mark : marks) {
            total += mark;
        }
        return total;
    }

    /**
     * Calculates the average percentage.
     * @return Average score.
     */
    public double calculateAveragePercentage() {
        return calculateTotalMarks() / SUBJECTS.length;
    }

    /**
     * Determines the overall grade based on the average percentage.
     * Grading System:
     * 90-100 = A+
     * 80-89 = A
     * 70-79 = B
     * 60-69 = C
     * 50-59 = D
     * Below 50 = Fail
     */
    public String getGrade() {
        double average = calculateAveragePercentage();
        if (average >= 90.0) return "A+";
        if (average >= 80.0) return "A";
        if (average >= 70.0) return "B";
        if (average >= 60.0) return "C";
        if (average >= 50.0) return "D";
        return "Fail";
    }

    /**
     * Find individual subject grade.
     */
    public String getSubjectGrade(double score) {
        if (score >= 90.0) return "A+";
        if (score >= 80.0) return "A";
        if (score >= 70.0) return "B";
        if (score >= 60.0) return "C";
        if (score >= 50.0) return "D";
        return "Fail";
    }

    /**
     * Returns the highest mark obtained.
     */
    public double getHighestMark() {
        double highest = marks[0];
        for (double mark : marks) {
            if (mark > highest) {
                highest = mark;
            }
        }
        return highest;
    }

    /**
     * Returns the name of the subject with the highest mark.
     */
    public String getHighestSubject() {
        double highest = marks[0];
        int index = 0;
        for (int i = 1; i < marks.length; i++) {
            if (marks[i] > highest) {
                highest = marks[i];
                index = i;
            }
        }
        return SUBJECTS[index];
    }

    /**
     * Returns the lowest mark obtained.
     */
    public double getLowestMark() {
        double lowest = marks[0];
        for (double mark : marks) {
            if (mark < lowest) {
                lowest = mark;
            }
        }
        return lowest;
    }

    /**
     * Returns the name of the subject with the lowest mark.
     */
    public String getLowestSubject() {
        double lowest = marks[0];
        int index = 0;
        for (int i = 1; i < marks.length; i++) {
            if (marks[i] < lowest) {
                lowest = marks[i];
                index = i;
            }
        }
        return SUBJECTS[index];
    }

    /**
     * Checks if the student passed a particular subject (Pass threshold is 50.0).
     */
    public boolean isSubjectPassed(int index) {
        return marks[index] >= 50.0;
    }

    /**
     * Checks scholarship eligibility (average > 90.0).
     */
    public boolean isEligibleForScholarship() {
        return calculateAveragePercentage() > 90.0;
    }

    /**
     * Checks attendance eligibility (attendance >= 75.0%).
     */
    public boolean isAttendanceEligible() {
        return attendancePercentage >= 75.0;
    }

    /**
     * Ranks subjects from highest marks to lowest marks.
     * Uses a simple Bubble Sort algorithm (beginner-friendly) to sort a copy.
     * @return Sorted array of strings describing subject and its mark.
     */
    public String[] getRankedSubjects() {
        double[] tempMarks = Arrays.copyOf(marks, marks.length);
        String[] tempSubjects = Arrays.copyOf(SUBJECTS, SUBJECTS.length);

        for (int i = 0; i < tempMarks.length - 1; i++) {
            for (int j = 0; j < tempMarks.length - 1 - i; j++) {
                if (tempMarks[j] < tempMarks[j + 1]) {
                    // Swap marks
                    double mTemp = tempMarks[j];
                    tempMarks[j] = tempMarks[j + 1];
                    tempMarks[j + 1] = mTemp;

                    // Swap subjects
                    String sTemp = tempSubjects[j];
                    tempSubjects[j] = tempSubjects[j + 1];
                    tempSubjects[j + 1] = sTemp;
                }
            }
        }

        String[] ranked = new String[marks.length];
        for (int i = 0; i < marks.length; i++) {
            ranked[i] = String.format("%d. %-18s : %.1f", (i + 1), tempSubjects[i], tempMarks[i]);
        }
        return ranked;
    }

    /**
     * Calculates marks needed to upgrade to the next grade tier.
     * @return Required additional total marks.
     */
    public double getMarksNeededForNextGrade() {
        double avg = calculateAveragePercentage();
        double currentTotal = calculateTotalMarks();
        double nextThreshold;

        if (avg < 50.0) nextThreshold = 50.0;
        else if (avg < 60.0) nextThreshold = 60.0;
        else if (avg < 70.0) nextThreshold = 70.0;
        else if (avg < 80.0) nextThreshold = 80.0;
        else if (avg < 90.0) nextThreshold = 90.0;
        else return 0.0; // Already at highest grade (A+)

        double requiredTotal = nextThreshold * SUBJECTS.length;
        return requiredTotal - currentTotal;
    }

    /**
     * Returns the name of the next grade level.
     */
    public String getNextGradeName() {
        double avg = calculateAveragePercentage();
        if (avg < 50.0) return "D";
        if (avg < 60.0) return "C";
        if (avg < 70.0) return "B";
        if (avg < 80.0) return "A";
        if (avg < 90.0) return "A+";
        return "N/A (Highest Grade)";
    }

    /**
     * Returns performance feedback based on overall grade.
     */
    public String getPerformanceFeedback() {
        String grade = getGrade();
        switch (grade) {
            case "A+":
                return "Outstanding! You have displayed exemplary understanding and command over all subjects. Keep leading the way!";
            case "A":
                return "Excellent performance! You have a very strong grasp of the material. A little more effort on your lowest subjects can push you to an A+.";
            case "B":
                return "Good job! You are performing above average. Focus on revising key concepts in subjects where you scored lower to reach the next tier.";
            case "C":
                return "Satisfactory. You have passed, but there is significant room for improvement. We recommend structured study hours and clearing doubts early.";
            case "D":
                return "Pass (Needs Attention). You are on the borderline. Consider seeking help, joining peer tutoring, or dedicating extra study time to avoid falling behind.";
            default:
                return "Academic Probation. You have not met the passing criteria. Please schedule a guidance meeting and create a dedicated recovery plan.";
        }
    }

    /**
     * Returns motivational message based on overall grade.
     */
    public String getMotivationalMessage() {
        String grade = getGrade();
        switch (grade) {
            case "A+":
                return "Success isn't just about what you accomplish, but what you inspire others to do. Keep shining!";
            case "A":
                return "Intelligence without ambition is a bird without wings. Keep pushing your limits!";
            case "B":
                return "Strive for progress, not perfection. You are on the right path!";
            case "C":
                return "Success is the sum of small efforts, repeated day in and day out. You can do it!";
            case "D":
                return "Believe you can and you're halfway there. Focus, practice, and watch yourself grow!";
            default:
                return "Our greatest glory is not in never falling, but in rising every time we fall. This is just a stepping stone to your comeback!";
        }
    }

    /**
     * Generates a beautifully formatted report card layout with borders.
     */
    public String generateReportCardText() {
        StringBuilder sb = new StringBuilder();
        sb.append("+-----------------------------------------------------------------------+\n");
        sb.append("|                      STUDENT REPORT CARD                              |\n");
        sb.append("+-----------------------------------------------------------------------+\n");
        sb.append(String.format("  Student Name : %-50s \n", name));
        sb.append(String.format("  Roll Number  : %-50s \n", rollNumber));
        sb.append(String.format("  Attendance   : %-5.1f%% (%-43s)\n", attendancePercentage, 
            isAttendanceEligible() ? "Eligible - Safe" : "Shortage - Below 75%"));
        sb.append("-------------------------------------------------------------------------\n");
        sb.append("  SUBJECTS & SCORES:\n");
        for (int i = 0; i < SUBJECTS.length; i++) {
            String passFail = isSubjectPassed(i) ? "PASS" : "FAIL";
            sb.append(String.format("  - %-18s : %5.1f / 100   [%-4s]   (Grade: %-2s)\n", 
                SUBJECTS[i], marks[i], passFail, getSubjectGrade(marks[i])));
        }
        sb.append("-------------------------------------------------------------------------\n");
        sb.append("  SUMMARY STATISTICS:\n");
        sb.append(String.format("  * Total Marks        : %.1f / 500.0\n", calculateTotalMarks()));
        sb.append(String.format("  * Average Percentage : %.2f%%\n", calculateAveragePercentage()));
        sb.append(String.format("  * Overall Grade      : %s\n", getGrade()));
        sb.append(String.format("  * Highest Scored Subject: %s (%.1f)\n", getHighestSubject(), getHighestMark()));
        sb.append(String.format("  * Lowest Scored Subject : %s (%.1f)\n", getLowestSubject(), getLowestMark()));
        sb.append("-------------------------------------------------------------------------\n");
        sb.append("  SUBJECT RANKINGS (Highest to Lowest):\n");
        for (String ranked : getRankedSubjects()) {
            sb.append("  ").append(ranked).append("\n");
        }
        sb.append("-------------------------------------------------------------------------\n");
        sb.append("  ACADEMIC STANDINGS & ELIGIBILITY:\n");
        sb.append(String.format("  * Scholarship Status : %s\n", 
            isEligibleForScholarship() ? "Eligible! (Average > 90%)" : "Not Eligible (Requires Average > 90%)"));
        
        double marksNeeded = getMarksNeededForNextGrade();
        if (marksNeeded > 0) {
            sb.append(String.format("  * Next Grade Target  : Needs %.1f more marks in total to reach Grade %s\n", 
                marksNeeded, getNextGradeName()));
        } else {
            sb.append("  * Next Grade Target  : Maximum Grade (A+) achieved! Outstanding work!\n");
        }
        sb.append("-------------------------------------------------------------------------\n");
        sb.append("  FEEDBACK & MOTIVATION:\n");
        sb.append("  Feedback  : ").append(getPerformanceFeedback()).append("\n");
        sb.append("  Motivation: ").append(getMotivationalMessage()).append("\n");
        sb.append("+-----------------------------------------------------------------------+\n");
        return sb.toString();
    }

    /**
     * Saves the report card text into a file reports/report_<rollNumber>.txt.
     * @return Output message or path of the file.
     * @throws IOException If folder or file creation fails.
     */
    public String saveReportCardToFile() throws IOException {
        File folder = new File("reports");
        if (!folder.exists()) {
            folder.mkdir();
        }
        String fileName = "reports/report_" + rollNumber + ".txt";
        File file = new File(fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(generateReportCardText());
        }
        return file.getAbsolutePath();
    }
}
