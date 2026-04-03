package core;

import java.util.*;

public class Session {

    private String type, day, slot;
    private double fee;
    private int month, week;

    private List<User> participants = new ArrayList<>();
    private List<Integer> scores = new ArrayList<>();
    private List<String> comments = new ArrayList<>();

    public void addComment(String c) {
        comments.add(c);
    }
    
    public int getParticipantCount() {
        return participants.size();
    }

    public Session(String type, String day, String slot, double fee, int month, int week) {
        this.type = type;
        this.day = day;
        this.slot = slot;
        this.fee = fee;
        this.month = month;
        this.week = week;
    }

    public boolean enroll(User u) {
        if (participants.size() < 4 && !participants.contains(u)) {
            participants.add(u);
            return true;
        }
        return false;
    }

    public void drop(User u) {
        participants.remove(u);
    }

    public void addScore(int s) {
        scores.add(s);
    }

    public double avgScore() {
        return scores.isEmpty() ? 0 :
                scores.stream().mapToInt(i -> i).average().orElse(0);
    }

    public String summary() {
        return type + " | " + day + " | " + slot + " | Week " + week
                + " | Seats: " + participants.size() + "/4";
    }

    public String getType() { return type; }
    public String getDay() { return day; }
    public String getSlot() { return slot; }
    public double getFee() { return fee; }
    public int getMonth() { return month; }
    public int getWeek() { return week; }
}