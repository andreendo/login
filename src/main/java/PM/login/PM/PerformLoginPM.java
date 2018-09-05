package PM.login.PM;

import PM.login.model.UserType;
import PM.login.model.User;
import PM.login.DAO.UserDAO;

/**
 *
 * @author andreendo
 */
public class PerformLoginPM {
    String login;
    String password;
    UserDAO userDao;
    private int wrongAttemps = 0;

    public PerformLoginPM() {
        login = "";
        password = "";
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }    
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }    

    public void clear() {
        login = "";
        password = "";
        System.out.println("PM.login.EfetuarLoginPM.clear()");
    }
    
    public PagePM pressLogin() throws Exception {
        login = login.trim();
        password = password.trim();
        if(login.isEmpty() || password.isEmpty())
            throw new Exception("Empty fields");
        
        User user = userDao.getByName(login);
        if(user == null)
            throw new Exception("Inexistent username");
        
        if(! user.getPassword().equals(password)) {
            user.setWrongAttempts(user.getWrongAttempts()+1);
            
            userDao.save(user);
            
            verificaSenhaBloqueada(user);
            System.out.println(wrongAttemps);
            
            throw new Exception("Wrong password");
        }
        
        verificaSenhaBloqueada(user);
        
        PagePM pagePM = null;
        if(user.getType() == UserType.ADMIN)
            pagePM = new AdminMainPagePM();
        else
            pagePM = new NormalUserMainPagePM();
        
        pagePM.setLoggedUser(user);
        
        user.setWrongAttempts(wrongAttemps);
        
        userDao.save(user);
        
        return pagePM;
    }

    void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }
    
    private void verificaSenhaBloqueada(User user) throws Exception {
        if (user.getWrongAttempts() >= 3) {
            throw new Exception("Blocked password, please contact support.");
        }
    }
}
