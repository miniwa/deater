package se.miniwa.deater.mail;

import org.hazlewood.connor.bottema.emailaddress.EmailAddressCriteria;
import org.hazlewood.connor.bottema.emailaddress.EmailAddressValidator;

public final class EmailAddress {
    private EmailAddress() {}

    public static boolean isValid(String address) {
        return EmailAddressValidator.isValid(address);
    }
}
