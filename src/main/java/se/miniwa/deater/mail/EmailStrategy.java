package se.miniwa.deater.mail;

import org.hazlewood.connor.bottema.emailaddress.EmailAddressValidator;
import org.simplejavamail.MailException;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.Recipient;
import org.simplejavamail.mailer.Mailer;

import java.time.Duration;

public class EmailStrategy {
    private Mailer mailer;
    private int retryCount;
    private Duration retryDelay;

    public EmailStrategy(Mailer mailer, int retryCount, Duration retryDelay) {
        this.mailer = mailer;
        this.retryCount = retryCount;
        this.retryDelay = retryDelay;
    }

    public void send(Email email) throws EmailException {
        boolean done = false;
        MailException lastEx = null;

        for(Recipient recipient : email.getRecipients()) {
            if(!EmailAddressValidator.isValid(recipient.getAddress(), mailer.getEmailAddressCriteria())) {
                throw new InvalidEmailAddressException(
                        String.format("Recipient address '%s' is invalid.", recipient.getAddress()));
            }
        }

        for(int i = 0; i < retryCount; i++) {
            try {
                mailer.sendMail(email);
                done = true;
            } catch(MailException ex) {
                lastEx = ex;
                try {
                    Thread.sleep(retryDelay.toMillis());
                } catch(InterruptedException interruptedEx) {
                    // Sleep was canceled. Just continue to retry.
                }
            }
        }

        if(!done) {
            assert lastEx != null;
            throw new EmailException("Email could not be sent.", lastEx);
        }
    }
}
