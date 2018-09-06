/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PM.login.PM;

import PM.login.DAO.UserDAO;
import PM.login.model.User;
import PM.login.model.UserType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

/**
 *
 * @author Aluno
 */
public class BloquearLoginTest {

    PerformLoginPM login;

    @Before
    public void inicializa() {
        login = new PerformLoginPM();
    }

    @Test
    public void testBloqueiaAposTresTentativasTest() {
        UserDAO mock = Mockito.mock(UserDAO.class);
        User user = new User("jefferson", "bastiao", UserType.ADMIN);
        when(mock.getByName("jefferson")).thenReturn(user);

        login.setLogin(user.getUsername());
        login.setPassword("123");

        for (int i = 0; i < 3; i++) {
            try {
                login.pressLogin();
            } catch (Exception e) {
                if(i < 2)
                    Assert.assertEquals("Wrong password", e.getMessage());
                else
                    Assert.assertEquals("Sua conta foi bloqueada por errar a senha 3 vezes", e.getMessage());
            }
        }
    }
}
