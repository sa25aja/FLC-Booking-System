package main;

import core.*;
import logic.*;

import java.util.*;

public class AppRunner {

    private static SystemManager manager = new SystemManager();
    private static List<User> users = new ArrayList<>();
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        initialise();

        while (true) {

            System.out.println("\n=== Leisure Centre Console ===");
            System.out.println("1) Show Schedule by Day");
            System.out.println("2) Show Schedule by Activity");
            System.out.println("3) Make Booking");
            System.out.println("4) Change Booking");
            System.out.println("5) Cancel Booking");
            System.out.println("6) Complete Lesson and Review");
            System.out.println("7) View Monthly Report Summary");
            System.out.println("8) View Top Activity (Champion Report)");
            System.out.println("9) Quit");

            System.out.print("Choose option: ");
            int opt = readInt();

            switch (opt) {
                case 1 -> displayByDay();
                case 2 -> displayByType();
                case 3 -> reserve();
                case 4 -> modify();
                case 5 -> remove();
                case 6 -> complete();
                case 7 -> monthlyStats();
                case 8 -> topActivity();
                case 9 -> {
                    System.out.println("System closed.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(input.nextLine());
            } catch (Exception e) {
                System.out.print("Enter valid number: ");
            }
        }
    }

    private static void initialise() {

        String[] days = {"Saturday", "Sunday"};
        String[] slots = {"Morning", "Afternoon", "Evening"};
        String[] activities = {"Yoga", "Zumba", "Aquacise", "BoxFit"};

        Map<String, Double> cost = Map.of(
                "Yoga", 10.0,
                "Zumba", 12.0,
                "Aquacise", 14.0,
                "BoxFit", 15.0
        );

        int month = 5;

        for (int w = 1; w <= 8; w++) {

            if (w == 5) month = 6;

            for (String d : days) {
                for (int i = 0; i < 3; i++) {

                    String act = activities[(w + i) % activities.length];

                    manager.addSession(new Session(act, d, slots[i], cost.get(act), month, w));
                }
            }
        }

        for (int i = 1; i <= 10; i++) {
            users.add(new User(i, "User" + i));
        }
        
     // AUTO GENERATE 20 REVIEWS
        Random rand = new Random();
        int count = 0;

        List<Session> allSessions = manager.getSessions();

        while (count < 20) {

            User u = users.get(rand.nextInt(users.size()));
            Session s = allSessions.get(rand.nextInt(allSessions.size()));

            Booking r = manager.createReservation(u, s);

            if (r != null) {
                int rating = rand.nextInt(5) + 1;
                String comment = "Generated review " + (count + 1);

                manager.finishSession(r.getId(), rating, comment);
                count++;
            }
        }
    }

    private static void displayByDay() {

        System.out.print("Enter day: ");
        String d = input.nextLine();

        int i = 1;
        for (Session s : manager.getSessions()) {
            if (s.getDay().equalsIgnoreCase(d)) {
                System.out.println(i++ + ") " + s.summary());
            }
        }
    }

    private static void displayByType() {

        System.out.print("Enter activity: ");
        String t = input.nextLine();

        int i = 1;
        for (Session s : manager.getSessions()) {
            if (s.getType().equalsIgnoreCase(t)) {
                System.out.println(i++ + ") " + s.summary());
            }
        }
    }

    private static void reserve() {

        System.out.print("User ID: ");
        User u = findUser(readInt());

        if (u == null) {
            System.out.println("User does not exist.");
            return;
        }

        List<Session> list = manager.getSessions();

        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ") " + list.get(i).summary());
        }

        System.out.print("Pick session: ");
        int choice = readInt();

        if (choice < 1 || choice > list.size()) return;

        Booking r = manager.createReservation(u, list.get(choice - 1));

        if (r != null)
            System.out.println("Reserved. Ref ID: " + r.getId());
        else
            System.out.println("Reservation failed.");
    }

    private static void modify() {

        System.out.print("Reservation ID: ");
        int id = readInt();

        List<Session> list = manager.getSessions();

        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ") " + list.get(i).summary());
        }

        System.out.print("New selection: ");
        int c = readInt();

        boolean ok = manager.updateReservation(id, list.get(c - 1));

        System.out.println(ok ? "Updated." : "Update failed.");
    }

    private static void remove() {

        System.out.print("Reservation ID: ");
        int id = readInt();

        System.out.println(manager.deleteReservation(id)
                ? "Removed."
                : "Removal failed.");
    }

    private static void complete() {

        System.out.print("Reservation ID: ");
        int id = readInt();

        System.out.print("Rating: ");
        int r = readInt();

        System.out.print("Comment: ");
        String c = input.nextLine();

        manager.finishSession(id, r, c);
        System.out.println("Recorded.");
    }

    private static void monthlyStats() {

        System.out.print("Month: ");
        manager.printMonthly(readInt());
    }

    private static void topActivity() {

        System.out.print("Month: ");
        manager.printTop(readInt());
    }

    private static User findUser(int id) {
        return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }
}