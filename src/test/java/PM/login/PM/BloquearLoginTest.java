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

    EfetuarLoginPM login;

    @Before
    public void inicializa() {
        login = new EfetuarLoginPM();
    }

    @Test
    public void testBloqueiaAposTresTentativasTest() throws Exception {
        UserDAO mock = Mockito.mock(UserDAO.class);
        User user = new User("jefferson", "bastiao", UserType.ADMIN);
        when(mock.getByName("jefferson")).thenReturn(user);

        login.setLogin("jefferson");
        login.setPassword("joao");
        try {
            login.pressLogin();
            login.pressLogin();
            login.pressLogin();
        } catch (Exception e) {
            Assert.assertEquals("Sua conta foi bloqueada por errar a senha 3 vezes", e.getMessage());
        }
    }
}
