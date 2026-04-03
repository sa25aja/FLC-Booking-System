package core;

public class User {

    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        return o instanceof User && ((User) o).id == id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}