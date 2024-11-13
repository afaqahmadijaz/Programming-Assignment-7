//********************************************************************
//
//  Author:       Afaq Ahmad
//
//  Program #:    Seven
//
//  File Name:    Program7.java
//
//  Course:       ITSE 2321 Object-Oriented Programming
//
//  Due Date:     November 12th, 2024
//
//  Instructor:   Prof. Fred Kumi
//
//  Chapter:      Chapters 2-6 & 15
//
//  Description:  This program calculates salary raises for faculty members
//                based on their current salary and prints a report with the
//                old salary, raise percentage, pay raise, and new salary.
//
//********************************************************************

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Program7
{
    // Instance variables for file handling
    private Scanner input;
    private PrintWriter output;

    //***************************************************************
    //
    //  Method:       main
    //
    //  Description:  The main method of the program
    //
    //  Parameters:   A String Array
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public static void main(String[] args)
    {
        Program7 app = new Program7();
        app.developerInfo();
        app.openFiles();
        app.processRecords();
        app.closeFiles();
    }

    //***************************************************************
    //
    //  Method:       openFiles
    //
    //  Description:  Opens input and output files
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void openFiles()
    {
        try
        {
            File inputFile = new File("Program7.txt");
            if (!inputFile.exists()) {
                System.err.println("Input file not found at: " + inputFile.getAbsolutePath());
                System.exit(1);
            }

            input = new Scanner(inputFile);
            output = new PrintWriter("Program7-output.txt");
        }
        catch (IOException ioException)
        {
            System.err.println("Error opening files: " + ioException.getMessage());
            System.exit(1);
        }
    }

    //***************************************************************
    //
    //  Method:       processRecords
    //
    //  Description:  Reads input data and processes each record
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void processRecords()
    {
        SalaryStats stats = new SalaryStats();
        printHeader();

        try
        {
            int facultyNumber = 101; // Start with an arbitrary faculty number

            // Loop through all the data in the file, handling each salary
            while (input.hasNextDouble())
            {
                double salary = input.nextDouble();
                processSingleRecord(facultyNumber++, salary, stats);
            }
            printSummary(stats);
        }
        catch (NoSuchElementException elementException)
        {
            System.err.println("File improperly formed. Terminating.");
            System.exit(1);
        }
        catch (IllegalStateException stateException)
        {
            System.err.println("Error reading from file. Terminating.");
            System.exit(1);
        }
    }

    //***************************************************************
    //
    //  Method:       processSingleRecord
    //
    //  Description:  Processes a single faculty record
    //
    //  Parameters:   facultyNumber (int), salary (double), stats (SalaryStats)
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void processSingleRecord(int facultyNumber, double salary, SalaryStats stats)
    {
        double raisePercent = calculateRaisePercentage(salary);
        double raiseAmount = salary * raisePercent / 100.0;
        double newSalary = salary + raiseAmount;

        stats.updateStats(salary, raiseAmount, newSalary);

        output.printf("%-10d %-15.2f %-15.1f %-15.2f %-15.2f%n",
                facultyNumber, salary, raisePercent, raiseAmount, newSalary);
    }

    //***************************************************************
    //
    //  Method:       calculateRaisePercentage
    //
    //  Description:  Determines raise percentage based on salary
    //
    //  Parameters:   salary (double)
    //
    //  Returns:      double
    //
    //**************************************************************
    public double calculateRaisePercentage(double salary)
    {
        double threshold1 = 50000.00;
        double threshold2 = 70000.00;

        // Using mathematical approach instead of logical operators
        int index = (int)((Math.signum(salary - threshold1) + 1) / 2 +
                (Math.signum(salary - threshold2) + 1) / 2);

        double[] rates = {5.5, 7.0, 4.0};
        return rates[index];
    }

    //***************************************************************
    //
    //  Method:       printHeader
    //
    //  Description:  Prints the report header
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void printHeader()
    {
        output.printf("%-10s %-15s %-15s %-15s %-15s%n",
                "Faculty #", "Old Salary", "Raise %", "Pay Raise", "New Salary");
    }

    //***************************************************************
    //
    //  Method:       printSummary
    //
    //  Description:  Prints summary statistics
    //
    //  Parameters:   stats (SalaryStats)
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void printSummary(SalaryStats stats)
    {
        output.printf("%nTotal Raise Amount: %.2f%n", stats.getTotalRaise());
        output.printf("Average Raise Amount: %.2f%n", stats.getAverageRaise());
        output.printf("Total Salary Before Raise: %.2f%n", stats.getTotalOldSalary());
        output.printf("Total Salary After Raise: %.2f%n", stats.getTotalNewSalary());
        output.printf("Average Salary Before Raise: %.2f%n", stats.getAverageOldSalary());
        output.printf("Average Salary After Raise: %.2f%n", stats.getAverageNewSalary());
    }

    //***************************************************************
    //
    //  Method:       closeFiles
    //
    //  Description:  Closes input and output files
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void closeFiles()
    {
        if (input != null)
            input.close();

        if (output != null)
            output.close();
    }

    //***************************************************************
    //
    //  Method:       developerInfo
    //
    //  Description:  Prints developer information
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void developerInfo()
    {
        System.out.println("Name:     Afaq Ahmad");
        System.out.println("Course:   ITSE 2321 Object-Oriented Programming");
        System.out.println("Program:  Seven");
        System.out.println("Due Date: November 12th, 2024\n");
    }

    // Inner class to maintain statistics
    private class SalaryStats
    {
        private double totalOldSalary = 0.0;
        private double totalNewSalary = 0.0;
        private double totalRaise = 0.0;
        private int count = 0;

        public void updateStats(double oldSalary, double raise, double newSalary)
        {
            totalOldSalary += oldSalary;
            totalNewSalary += newSalary;
            totalRaise += raise;
            count++;
        }

        public double getTotalOldSalary() { return totalOldSalary; }
        public double getTotalNewSalary() { return totalNewSalary; }
        public double getTotalRaise() { return totalRaise; }
        public double getAverageOldSalary() { return count == 0 ? 0.0 : totalOldSalary / count; }
        public double getAverageNewSalary() { return count == 0 ? 0.0 : totalNewSalary / count; }
        public double getAverageRaise() { return count == 0 ? 0.0 : totalRaise / count; }
    }
}