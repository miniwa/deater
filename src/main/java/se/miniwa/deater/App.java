package se.miniwa.deater;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.google.gson.JsonParseException;
import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.miniwa.deater.cli.AppArgs;
import se.miniwa.deater.cli.EmailCommand;
import se.miniwa.deater.cli.MasterCommand;
import se.miniwa.deater.game.Players;
import se.miniwa.deater.game.TargetAssignment;
import se.miniwa.deater.game.Player;
import se.miniwa.deater.game.PlayerTarget;
import se.miniwa.deater.mail.EmailException;
import se.miniwa.deater.mail.EmailStrategy;
import se.miniwa.deater.mail.Emails;
import se.miniwa.deater.mail.InvalidEmailAddressException;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.List;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        AppArgs appArgs = new AppArgs();
        MasterCommand masterCmd = new MasterCommand();
        EmailCommand emailCmd = new EmailCommand();
        JCommander argParser = JCommander.newBuilder()
                .addObject(appArgs)
                .addCommand(MasterCommand.MASTER_NAME, masterCmd)
                .addCommand(EmailCommand.EMAIL_NAME, emailCmd)
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
            players = Players.fromFile(appArgs.getPlayerFile());
        } catch(FileNotFoundException ex) {
            logger.error("Could not find file: ", appArgs.getPlayerFile().getAbsolutePath());
            System.exit(1);
            return;
        } catch(JsonParseException ex) {
            logger.error("Can't parse player file", ex);
            System.exit(1);
            return;
        }
        logger.info(String.format("Read %d players from player file", players.length));

        // Build target assignment for game.
        Player first = players[0];
        TargetAssignment assignment = TargetAssignment.builder()
                .addPlayers(players)
                .randomize()
                .build();
        Iterable<PlayerTarget> targets = assignment.getChain(first);

        String command = argParser.getParsedCommand();
        if(command.equals(MasterCommand.MASTER_NAME)) {
            masterMode(targets);
        } else if(command.equals(EmailCommand.EMAIL_NAME)) {
            emailMode(emailCmd, targets);
        }
    }

    public static void masterMode(Iterable<PlayerTarget> targets) {
        for(PlayerTarget playerTarget : targets) {
            logger.info(playerTarget.getPlayer().getName() + " --> " +
                    playerTarget.getTarget().getName());
        }
    }

    public static void emailMode(EmailCommand emailCommand, Iterable<PlayerTarget> targets) {
        String subject = emailCommand.getSubject();
        String fromName = emailCommand.getFromName();
        String fromAddress = emailCommand.getFromAddress();
        String password = emailCommand.getPassword();

        Mailer mailer = MailerBuilder.withSMTPServerHost("smtp.gmail.com")
                .withSMTPServerPort(587)
                .withSMTPServerUsername(fromAddress)
                .withSMTPServerPassword(password)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();
        EmailStrategy emailStrategy = new EmailStrategy(mailer, 5, Duration.ofMillis(1000));

        try {
            logger.info("Building invitiation emails");
            List<Email> emails = Emails.buildTargetNotificationEmails(subject, fromName, fromAddress, targets);
            int emailCount = emails.size();
            for(int i = 0; i < emailCount; i++) {
                logger.info(String.format("Sending %d of %d emails", i + 1, emailCount));
                emailStrategy.send(emails.get(i));
            }
            logger.info("Done!");
        } catch(InvalidEmailAddressException ex) {
            logger.error(ex.getMessage());
        } catch(EmailException ex) {
            logger.error("Could not send email", ex);
        }
    }
}
