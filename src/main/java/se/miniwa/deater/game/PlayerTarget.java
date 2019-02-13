package se.miniwa.deater.game;

public class PlayerTarget {
    private Player player;
    private Player target;

    public PlayerTarget(Player player, Player target) {
        this.player = player;
        this.target = target;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getTarget() {
        return target;
    }
}
