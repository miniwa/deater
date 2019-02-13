package se.miniwa.deater;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import se.miniwa.deater.game.TargetAssignment;
import se.miniwa.deater.game.Player;
import se.miniwa.deater.game.PlayerTarget;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Player[] players;
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader("players.json");
            players = gson.fromJson(reader, Player[].class);
        } catch(IOException ex) {
            ex.printStackTrace();
            System.exit(1);
            return;
        }

        TargetAssignment targets = TargetAssignment.builder()
                .addPlayers(players)
                .randomize()
                .build();

        for(PlayerTarget playerTarget : targets.getChain(players[0])) {
            System.out.println(playerTarget.getPlayer().getName() + " --> " +
            playerTarget.getTarget().getName());
        }
    }
}
