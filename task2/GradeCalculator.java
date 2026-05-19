import java.util.ArrayList;
import java.util.Scanner;

public class GradeCalculator {

    static String getGrade(double average) {
        if (average >= 90) return "A+";
        else if (average >= 80) return "A";
        else if (average >= 70) return "B";
        else if (average >= 60) return "C";
        else if (average >= 50) return "D";
        else if (average >= 35) return "E";
        else return "F";
    }

    static String getRemark(String grade) {
        switch (grade) {
            case "A+": return "Outstanding";
            case "A":  return "Excellent";
            case "B":  return "Very Good";
            case "C":  return "Good";
            case "D":  return "Satisfactory";
            case "E":  return "Pass";
            default:   return "Fail - Needs Improvement";
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("       STUDENT GRADE CALCULATOR         ");
        System.out.println("========================================");

        int numSubjects = 0;
        while (true) {
            System.out.print("Enter the number of subjects: ");
            try {
                numSubjects = Integer.parseInt(scanner.nextLine().trim());
                if (numSubjects <= 0) {
                    System.out.println("  Please enter a positive number.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a whole number.");
            }
        }

        ArrayList<String> subjectNames = new ArrayList<>();
        ArrayList<Integer> subjectMarks = new ArrayList<>();
        int totalMarks = 0;

        System.out.println();
        System.out.println("Enter marks obtained (out of 100) for each subject:");
        System.out.println("----------------------------------------------------");

        for (int i = 1; i <= numSubjects; i++) {
            System.out.print("Subject " + i + " name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = "Subject " + i;

            int marks = 0;
            while (true) {
                System.out.print("Marks obtained in " + name + ": ");
                try {
                    marks = Integer.parseInt(scanner.nextLine().trim());
                    if (marks < 0 || marks > 100) {
                        System.out.println("  Marks must be between 0 and 100. Try again.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("  Invalid input. Please enter a number between 0 and 100.");
                }
            }

            subjectNames.add(name);
            subjectMarks.add(marks);
            totalMarks += marks;
        }

        double averagePercentage = (double) totalMarks / numSubjects;

        String grade  = getGrade(averagePercentage);
        String remark = getRemark(grade);

        System.out.println();
        System.out.println("========================================");
        System.out.println("              RESULTS                   ");
        System.out.println("========================================");

        System.out.printf("%-20s %-10s%n", "Subject", "Marks");
        System.out.println("--------------------------------------------");
        for (int i = 0; i < numSubjects; i++) {
            System.out.printf("%-20s %d / 100%n", subjectNames.get(i), subjectMarks.get(i));
        }
        System.out.println("--------------------------------------------");
        System.out.printf("%-20s %d / %d%n",  "Total Marks",       totalMarks, numSubjects * 100);
        System.out.printf("%-20s %.2f%%%n",    "Average Percentage",averagePercentage);
        System.out.printf("%-20s %s%n",        "Grade",             grade);
        System.out.printf("%-20s %s%n",        "Remark",            remark);
        System.out.println("========================================");

        System.out.println();
        System.out.println("--- Grade Scale ---");
        System.out.println("A+ : 90 - 100  -> Outstanding");
        System.out.println("A  : 80 -  89  -> Excellent");
        System.out.println("B  : 70 -  79  -> Very Good");
        System.out.println("C  : 60 -  69  -> Good");
        System.out.println("D  : 50 -  59  -> Satisfactory");
        System.out.println("E  : 35 -  49  -> Pass");
        System.out.println("F  :  0 -  34  -> Fail");

        scanner.close();
    }
}