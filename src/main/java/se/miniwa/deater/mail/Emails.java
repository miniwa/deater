package se.miniwa.deater.mail;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import se.miniwa.deater.game.PlayerTarget;

import java.util.ArrayList;
import java.util.List;

public final class Emails {
    private Emails() {}

    public static List<Email> buildTargetEmails(Iterable<PlayerTarget> targets) throws InvalidEmailAddressException {
        List<Email> emails = new ArrayList<>();
        for(PlayerTarget playerTarget : targets) {
            String name = playerTarget.getPlayer().getName();
            String address = playerTarget.getPlayer().getEmail();
            if(address == null) {
                String msg = String.format("Player '%s' does not have an email address.", name);
                throw new InvalidEmailAddressException(msg);
            }

            Email email = EmailBuilder.startingBlank()
                    .to(address)
                    .withSubject("Death Eater Game")
                    .buildEmail();
            emails.add(email);
        }
        return emails;
    }
}
