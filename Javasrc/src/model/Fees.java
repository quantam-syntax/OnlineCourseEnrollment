package model;

public class Fees {
    private int feeId;
    private double amount;
    private String deadLine;
    private String paymentStatus;
    public Fees(int feeId , double amount , String deadLine , String paymentStatus)
    {
        this.feeId=feeId;
        this.amount = amount ; 
        this.deadLine = deadLine;
        this.paymentStatus = paymentStatus;
    }
    public int getFeeId(){
        return feeId;
    }
    public double getAmount(){
        return amount;
    }
    public String getDeadline(){
        return deadLine;
    }
    public String getPaymentStatus(){
        return paymentStatus;
    }
    public double calculateFees(){
        System.out.println("Total fees is : "+ amount);
        return amount;
    }
    public boolean payFees(){
        this.paymentStatus = "Paid";
        System.out.println("Fees paid successfully");
        return true;
    }
}
