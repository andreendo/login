package PM.login.PM;

import PM.login.DAO.WrongTriesDAO;
import PM.login.model.UserType;
import PM.login.model.User;
import PM.login.DAO.UserDAO;
import PM.login.model.WrongTries;

/**
 *
 * @author andreendo
 */
public class PerformLoginPM {
    String login;
    String password;
    UserDAO userDao;
    WrongTriesDAO wtd;

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
        if (wtd.count(user) >= 2) {
            throw new Exception("Account is blocked");
        }
        if(! user.getPassword().equals(password)) {
            wtd.save(new WrongTries(user, password));
            if (wtd.count(user) >= 2) {
                throw new Exception("Account is blocked");
            }
            throw new Exception("Wrong password");
        }

        PagePM pagePM = null;
        if(user.getType() == UserType.ADMIN)
            pagePM = new AdminMainPagePM();
        else
            pagePM = new NormalUserMainPagePM();
        
        pagePM.setLoggedUser(user);
        wtd.clear(user);
        return pagePM;
    }

    void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    public void setWtd(WrongTriesDAO wtd) {
        this.wtd = wtd;
    }
}
