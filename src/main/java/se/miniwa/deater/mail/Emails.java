package se.miniwa.deater.mail;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import se.miniwa.deater.game.PlayerTarget;

import java.util.ArrayList;
import java.util.List;

public final class Emails {
    private Emails() {}

    public List<Email> buildTargetEmails(Iterable<PlayerTarget> targetChain) {
        List<Email> emails = new ArrayList<>();
        for(PlayerTarget playerTarget : targetChain) {
            Email email = EmailBuilder.startingBlank()
                    .to(playerTarget.getPlayer().getEmail())
                    .withSubject("Death Eater Game")
                    .buildEmail();
            emails.add(email);
        }
        return emails;
    }
}
