package se.miniwa.deater.mail;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import se.miniwa.deater.game.PlayerTarget;

import java.util.ArrayList;
import java.util.List;

public final class Emails {
    private Emails() {}

    public static List<Email> buildTargetNotificationEmails(String subject, String fromName, String fromAddress,
                                                            Iterable<PlayerTarget> targets)
            throws InvalidEmailAddressException {
        if(!EmailAddress.isValid(fromAddress)) {
            throw new InvalidEmailAddressException(String.format("From address '%s' is not valid.", fromAddress));
        }

        List<Email> emails = new ArrayList<>();
        for(PlayerTarget playerTarget : targets) {
            String name = playerTarget.getPlayer().getName();
            String address = playerTarget.getPlayer().getEmail();
            if(address == null) {
                String msg = String.format("Player '%s' does not have an email address.", name);
                throw new InvalidEmailAddressException(msg);
            }

            if(!EmailAddress.isValid(address)) {
                String msg = String.format("Email address '%s' for player '%s' is invalid.", address, name);
                throw new InvalidEmailAddressException(msg);
            }

            Email email = EmailBuilder.startingBlank()
                    .to(name, address)
                    .from(fromName, fromAddress)
                    .withSubject(subject)
                    .withPlainText(
                            String.format("%s.. Ditt mål är %s!", name, playerTarget.getTarget().getName()))
                    .buildEmail();
            emails.add(email);
        }
        return emails;
    }
}
