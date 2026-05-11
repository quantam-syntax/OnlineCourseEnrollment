package service;

import model.Schedule;
import repository.FileManager;
import java.util.ArrayList;

public class ScheduleService {
    private ArrayList<Schedule> schedules;
    private FileManager fileManager;
    private int scheduleCounter = 1;

    public ScheduleService() {
        this.schedules = new ArrayList<>();
        this.fileManager = new FileManager();
        loadFromFile();
    }

    public void loadFromFile() {
        ArrayList<String[]> data = fileManager.loadSchedules();
        for (String[] parts : data) {
            if (parts.length != 3) {
                continue;
            }
            try {
                int id = Integer.parseInt(parts[0].trim());
                schedules.add(new Schedule(id, parts[2].trim(), parts[1].trim()));
                scheduleCounter = Math.max(scheduleCounter, id + 1);
            } catch (NumberFormatException ex) {
                System.out.println("Skipping malformed schedule row: " + String.join(",", parts));
            }
        }
    }

    public Schedule addSchedule(String day, String time) {
        Schedule schedule = new Schedule(scheduleCounter, time, day);
        scheduleCounter++;
        schedules.add(schedule);
        fileManager.saveSchedule(schedule);
        System.out.println("Schedule added ID: " + schedule.getScheduleId());
        return schedule;
    }

    public Schedule getScheduleById(int scheduleId) {
        for (Schedule s : schedules) {
            if (s.getScheduleId() == scheduleId) {
                return s;
            }
        }
        return null;
    }

    public void viewAllSchedules() {
        if (schedules.isEmpty()) {
            System.out.println("No schedules available");
            return;
        }
        for (Schedule s : schedules) {
            System.out.println("Schedule ID: " + s.getScheduleId() + " | " + s.viewSchedule());
        }
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }
}