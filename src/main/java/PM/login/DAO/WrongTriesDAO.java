package PM.login.DAO;

import PM.login.model.User;
import PM.login.model.WrongTries;

public interface WrongTriesDAO {

    public WrongTries getById(int id);

    public boolean exist(int id);

    public void save(WrongTries wt);

    public int count(User u);

    public void clear(User u);
}
