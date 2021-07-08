package PM.login.model;

public class WrongTries {
    User user;
    String passTried;

    public WrongTries(User user, String passTried) {
        this.user = user;
        this.passTried = passTried;
    }
}
