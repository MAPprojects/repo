package entities;

public class MailCredentials {
    private static final String APPLICATION_EMAIL = "<email>";
    private static final String APPLICATION_PASSWORD = "<password>";
    private static final MailCredentials INSTANCE = new MailCredentials();

    private MailCredentials() {
    }

    public static String getApplicationEmail() {
        return APPLICATION_EMAIL;
    }

    public static String getApplicationPassword() {
        return APPLICATION_PASSWORD;
    }

    public static MailCredentials getINSTANCE() {
        return INSTANCE;
    }
}
