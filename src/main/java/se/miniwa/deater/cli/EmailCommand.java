package se.miniwa.deater.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Launches a game in email mode")
public class EmailCommand {
    public static final String EMAIL_NAME = "email";

    @Parameter(names = {"--subject", "-s"}, required = true,
            description = "The subject of the invitation email")
    private String subject;

    @Parameter(names = {"--from-name", "-n"}, required = true,
            description = "The name that will appear as sender in the invitation emails")
    private String fromName;

    @Parameter(names = {"--from-address", "-a"}, required = true,
            description = "The address to send invitation emails from")
    private String fromAddress;

    @Parameter(names = {"--password", "-p"}, required = true,
            description = "The password for the account that will send the invitation emails")
    private String password;

    public String getSubject() {
        return subject;
    }

    public String getFromName() {
        return fromName;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getPassword() {
        return password;
    }
}
