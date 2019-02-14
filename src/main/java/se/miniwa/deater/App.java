package se.miniwa.deater;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import org.simplejavamail.email.Email;
import se.miniwa.deater.cli.AppArgs;
import se.miniwa.deater.game.Players;
import se.miniwa.deater.game.TargetAssignment;
import se.miniwa.deater.game.Player;
import se.miniwa.deater.game.PlayerTarget;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Target;
import java.util.List;

public class App {
    public static void main(String[] args) {
        AppArgs appArgs = new AppArgs();
        JCommander argParser = JCommander.newBuilder()
                .addObject(appArgs)
                .programName("deater")
                .build();

        try {
            argParser.parse(args);
        } catch(ParameterException ex) {
            argParser.usage();
            System.exit(1);
            return;
        }

        Player[] players;
        try {
            players = Players.fromFile(appArgs.playerFile);
        } catch(FileNotFoundException ex) {
            System.out.println("Could not find file: " + appArgs.playerFile.getAbsolutePath());
            System.exit(1);
            return;
        } catch(JsonParseException ex) {
            ex.printStackTrace();
            System.exit(1);
            return;
        }

        // Build target assignment for game.
        Player first = players[0];
        TargetAssignment targets = TargetAssignment.builder()
                .addPlayers(players)
                .randomize()
                .build();

        masterMode(targets, first);
    }

    public static void masterMode(TargetAssignment targets, Player first) {
        for(PlayerTarget playerTarget : targets.getChain(first)) {
            System.out.println(playerTarget.getPlayer().getName() + " --> " +
                    playerTarget.getTarget().getName());
        }
    }

    public static List<Email> buildEmails(TargetAssignment targets, Player first) {

    }
}
