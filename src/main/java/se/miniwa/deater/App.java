package se.miniwa.deater;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.google.gson.JsonParseException;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import se.miniwa.deater.cli.AppArgs;
import se.miniwa.deater.game.Players;
import se.miniwa.deater.game.TargetAssignment;
import se.miniwa.deater.game.Player;
import se.miniwa.deater.game.PlayerTarget;
import se.miniwa.deater.mail.EmailStrategy;
import se.miniwa.deater.mail.Emails;

import java.io.FileNotFoundException;
import java.time.Duration;

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
        TargetAssignment assignment = TargetAssignment.builder()
                .addPlayers(players)
                .randomize()
                .build();
        Iterable<PlayerTarget> targets = assignment.getChain(first);

        masterMode(assignment, first);
    }

    public static void masterMode(TargetAssignment targets, Player first) {
        for(PlayerTarget playerTarget : targets.getChain(first)) {
            System.out.println(playerTarget.getPlayer().getName() + " --> " +
                    playerTarget.getTarget().getName());
        }
    }

    public static void emailMode(Iterable<PlayerTarget> targets) {
        Emails.buildTargetEmails(targets);
        Mailer mailer = MailerBuilder.withSMTPServer("smtp.google.com", 585, "blarch3030@gmail.com" "hej")
                .withSMTPServerHost("smtp.google.se")
                .withSMTPServerPort(583)
                .withSMTPServerUsername("blarch3030@gmail.com")
                .withSMTPServerPassword("password")
                .buildMailer();

        EmailStrategy strategy = new EmailStrategy(mailer, 10, Duration.ofSeconds(5));
        Emails
    }
}
