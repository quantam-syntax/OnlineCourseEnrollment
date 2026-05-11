package service;

import model.Fees;
import repository.FileManager;
import java.util.ArrayList;

public class FeesService {
    private ArrayList<Fees> feesList;
    private ArrayList<Integer> studentIds;
    private FileManager fileManager;
    private int feesCounter = 1;

    public FeesService() {
        this.feesList = new ArrayList<>();
        this.studentIds = new ArrayList<>();
        this.fileManager = new FileManager();
        loadFromFile();
    }

    public void loadFromFile() {
        ArrayList<String[]> data = fileManager.loadFees();
        for (String[] parts : data) {
            if (parts.length != 5) {
                continue;
            }
            try {
                int id = Integer.parseInt(parts[0].trim());
                int studentId = Integer.parseInt(parts[1].trim());
                double amount = Double.parseDouble(parts[2].trim());
                feesList.add(new Fees(id, amount, parts[3].trim(), parts[4].trim()));
                studentIds.add(studentId);
                feesCounter = Math.max(feesCounter, id + 1);
            } catch (NumberFormatException ex) {
                System.out.println("Skipping malformed fees row: " + String.join(",", parts));
            }
        }
    }

    public Fees assignFees(int studentId, double amount, String deadline) {
        Fees fees = new Fees(feesCounter, amount, deadline, "Unpaid");
        feesCounter++;
        feesList.add(fees);
        studentIds.add(studentId);
        fileManager.saveFees(studentId, fees);
        System.out.println("Fees assigned ID: " + fees.getFeeId() + " for Student ID: " + studentId);
        return fees;
    }

    public boolean payFees(int feeId) {
        for (Fees f : feesList) {
            if (f.getFeeId() == feeId) {
                f.payFees();
                fileManager.updateFeesStatus(feeId);
                return true;
            }
        }
        System.out.println("Fee record not found");
        return false;
    }

    public void viewFeesByStudent(int studentId) {
        boolean found = false;
        for (int i = 0; i < feesList.size(); i++) {
            if (studentIds.get(i) == studentId) {
                Fees f = feesList.get(i);
                System.out.println("Fee ID: " + f.getFeeId() + " | Amount: " + f.getAmount() + " | Deadline: " + f.getDeadline() + " | Status: " + f.getPaymentStatus());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No fee records found for Student ID: " + studentId);
        }
    }

    public ArrayList<Fees> getFeesList() {
        return feesList;
    }

    public ArrayList<Integer> getStudentIds() {
        return studentIds;
    }
}