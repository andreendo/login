package PM.login.model;

/**
 *
 * @author andreendo
 */
public class User {
    private String username, password;
    private UserType type;
    private int wrongAttempts;
    
    public User(String username, String password, UserType type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }
    
    public User(String username, String password, UserType type, int wrongAttempts) {
        this.username = username;
        this.password = password;
        this.type = type;
        this.wrongAttempts = wrongAttempts;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public UserType getType() {
        return type;
    }   

    public int getWrongAttempts() {
        return wrongAttempts;
    }

    public void setWrongAttempts(int wrongAttempts) {
        this.wrongAttempts = wrongAttempts;
    }
    
    
}
