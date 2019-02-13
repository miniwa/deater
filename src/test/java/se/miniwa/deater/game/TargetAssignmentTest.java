package se.miniwa.deater.game;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TargetAssignmentTest {
    @Test
    public void testGetTargetReturnsProperTarget() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        Map<Player, Player> targetMap = new HashMap<Player, Player>();
        targetMap.put(player1, player2);

        TargetAssignment targetAssignment = new TargetAssignment(targetMap);
        Assertions.assertEquals(player2, targetAssignment.getTarget(player1));
    }
}
