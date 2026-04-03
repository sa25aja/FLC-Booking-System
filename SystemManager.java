package logic;

import core.*;
import java.util.*;

public class SystemManager {

    private List<Session> sessions = new ArrayList<>();
    private List<Booking> reservations = new ArrayList<>();

    public void addSession(Session s) {
        sessions.add(s);
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public Booking createReservation(User u, Session s) {

        for (Booking r : reservations) {
            if (r.getUser().equals(u)
                    && r.getSession().equals(s)
                    && r.getState() != Booking.State.CANCELLED)
                return null;
        }

        if (s.enroll(u)) {
            Booking r = new Booking(u, s);
            reservations.add(r);
            return r;
        }
        return null;
    }

    public boolean updateReservation(int id, Session newS) {

        for (Booking r : reservations) {
            if (r.getId() == id) {

                if (r.getState() == Booking.State.DONE) return false;

                if (!newS.enroll(r.getUser())) return false;

                r.getSession().drop(r.getUser());
                r.change(newS);
                return true;
            }
        }
        return false;
    }

    public boolean deleteReservation(int id) {

        for (Booking r : reservations) {
            if (r.getId() == id) {

                if (r.getState() == Booking.State.DONE) return false;

                r.getSession().drop(r.getUser());
                r.setState(Booking.State.CANCELLED);
                return true;
            }
        }
        return false;
    }

    public void finishSession(int id, int rating, String comment) {

        for (Booking r : reservations) {

            if (r.getId() == id) {

                // ❗ prevent multiple attendance
                if (r.getState() == Booking.State.DONE) return;

                if (rating < 1 || rating > 5) return;

                r.getSession().addScore(rating);
                r.getSession().addComment(comment);
                r.setState(Booking.State.DONE);

                return; // stop after processing
            }
        }
    }

    public void printMonthly(int m) {

        System.out.println("\n-- Monthly Overview --");

        for (Session s : sessions) {

            if (s.getMonth() == m) {

                int count = 0;

                for (Booking r : reservations) {
                    if (r.getSession().equals(s)
                            && r.getState() == Booking.State.DONE) {
                        count++;
                    }
                }

                System.out.println(s.getType() + " | "
                        + s.getDay() + " | " + s.getSlot()
                        + " | Week " + s.getWeek()
                        + " | Attendees: " + count
                        + " | Avg Score: " + s.avgScore());
            }
        }
    }

    public void printTop(int m) {

        System.out.println("\n-- Top Performing Activity --");

        Map<String, Double> revenue = new HashMap<>();

        for (Session s : sessions) {

            if (s.getMonth() == m) {

                int count = 0;

                for (Booking r : reservations) {
                    if (r.getSession().equals(s)
                            && r.getState() == Booking.State.DONE) {
                        count++;
                    }
                }

                double total = count * s.getFee();

                revenue.put(s.getType(),
                        revenue.getOrDefault(s.getType(), 0.0) + total);
            }
        }

        String best = "";
        double max = 0;

        for (String k : revenue.keySet()) {

            double v = revenue.get(k);
            System.out.println(k + " -> £" + v);

            if (v > max) {
                max = v;
                best = k;
            }
        }

        System.out.println("Top Activity: " + best);
    }
}