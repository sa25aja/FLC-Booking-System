package core;

public class Booking {

    public enum State { NEW, UPDATED, CANCELLED, DONE }

    private static int count = 1;

    private int id;
    private User user;
    private Session session;
    private State state;

    public Booking(User u, Session s) {
        this.id = count++;
        this.user = u;
        this.session = s;
        this.state = State.NEW;
    }

    public int getId() { return id; }
    public User getUser() { return user; }
    public Session getSession() { return session; }
    public State getState() { return state; }

    public void change(Session s) {
        this.session = s;
        this.state = State.UPDATED;
    }

    public void setState(State s) {
        this.state = s;
    }
}