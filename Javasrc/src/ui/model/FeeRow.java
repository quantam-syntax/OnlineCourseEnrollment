package ui.model;

public class FeeRow {
    private int feeId;
    private int studentId;
    private double amount;
    private String deadline;
    private String paymentStatus;

    public FeeRow(int feeId, int studentId, double amount, String deadline, String paymentStatus) {
        this.feeId = feeId;
        this.studentId = studentId;
        this.amount = amount;
        this.deadline = deadline;
        this.paymentStatus = paymentStatus;
    }

    public int getFeeId() { return feeId; }
    public int getStudentId() { return studentId; }
    public double getAmount() { return amount; }
    public String getDeadline() { return deadline; }
    public String getPaymentStatus() { return paymentStatus; }
}
