package model;

public class Schedule {
    private int scheduleId;
    private String time;
    private String day;

    public Schedule(int scheduleId , String time , String day){
        this.scheduleId = scheduleId;
        this.time = time;
        this.day = day;
    }
    public int getScheduleId(){
        return scheduleId;
    }
    public String getTime(){
        return time;
    }
    public String getDay(){
        return day;
    }
    public boolean assignSchedule(){
        System.out.println("Schedule Assigned: "+ scheduleId + " Day: "+ day+ " Time: "+ time);
        return true;
    }
    public String viewSchedule(){
        return "Day: "+ day + "Time : " + time;
    }
}
