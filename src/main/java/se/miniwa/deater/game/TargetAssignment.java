package se.miniwa.deater.game;

import java.util.*;

public class TargetAssignment {
    private Map<Player, Player> targetMap;

    private TargetAssignment(Map<Player, Player> targetMap) {
        this.targetMap = targetMap;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Player getTarget(Player player) {
        return targetMap.get(player);
    }

    public Iterable<PlayerTarget> getChain(Player first) {
        List<PlayerTarget> targets = new ArrayList<PlayerTarget>();
        Player current = first;
        for(int i = 0; i < targetMap.size(); i++) {
            Player target = targetMap.get(current);
            targets.add(new PlayerTarget(current, target));
            current = target;
        }
        return targets;
    }

    public static class Builder {
        private List<Player> players = new ArrayList<Player>();
        private Builder() { }

        public Builder addPlayer(Player player) {
            players.add(player);
            return this;
        }

        public Builder addPlayers(Iterable<Player> players) {
            for(Player player : players) {
                this.players.add(player);
            }
            return this;
        }

        public Builder addPlayers(Player[] players) {
            Collections.addAll(this.players, players);
            return this;
        }

        public Builder randomize() {
            Collections.shuffle(players);
            return this;
        }

        public TargetAssignment build() {
            Map<Player, Player> targetMap = new HashMap<Player, Player>();
            for(int i = 0; i < players.size() - 1; i++) {
                Player player = players.get(i);
                Player target = players.get(i + 1);
                targetMap.put(player, target);
            }
            targetMap.put(players.get(players.size() - 1), players.get(0));
            return new TargetAssignment(targetMap);
        }
    }
}
