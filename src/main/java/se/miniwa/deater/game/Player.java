package se.miniwa.deater.game;

public class Player {
    private String name;
    private String email;

    public Player(String name) {
        this.name = name;
    }

    public Player(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
