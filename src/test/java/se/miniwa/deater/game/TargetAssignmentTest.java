package se.miniwa.deater.game;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TargetAssignmentTest {
    private Player player1 = new Player("Player1");
    private Player player2 = new Player("Player2");
    private Player player3 = new Player("Player3");
    private Player player4 = new Player("Player4");
    private Player player5 = new Player("Player5");
    private List<Player> players = new ArrayList<>();
    private TargetAssignment targetAssignment;

    @BeforeEach
    public void setup() {
        players.clear();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        players.add(player5);

        targetAssignment = TargetAssignment.builder()
                .addPlayers(players)
                .build();
    }

    @Test
    public void testGetTargetReturnsTargetOfGivenPlayer() {
        Assertions.assertEquals(player2, targetAssignment.getTarget(player1));
    }

    @Test
    public void testGetChainFormsLoopWhereAllPlayersArePresent() {
        Iterable<PlayerTarget> chain = targetAssignment.getChain(player1);
        Iterator<PlayerTarget> iterator = chain.iterator();

        int count = players.size();
        for(int i = 0; i < count - 1; i++) {
            Player player = players.get(i);
            Player target = players.get(i + 1);
            PlayerTarget playerTarget = iterator.next();

            Assertions.assertEquals(player, playerTarget.getPlayer());
            Assertions.assertEquals(target, playerTarget.getTarget());
        }
        Player first = players.get(0);
        Player last = players.get(players.size() - 1);
        PlayerTarget playerTarget = iterator.next();

        Assertions.assertEquals(last, playerTarget.getPlayer());
        Assertions.assertEquals(first, playerTarget.getTarget());
        Assertions.assertFalse(iterator.hasNext());
    }
}
