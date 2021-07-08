package PM.login.PM;

import PM.login.DAO.WrongTriesDAO;
import PM.login.model.UserType;
import PM.login.model.User;
import PM.login.DAO.UserDAO;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author andreendo
 */
public class EfetuarLoginPMTest {
    
    public EfetuarLoginPMTest() {
    }

    private PerformLoginPM efetuarLoginPM;
    private UserDAO userDaoMock;
    private WrongTriesDAO wrongTriesDAO;

    @Before
    public void before(){
        wrongTriesDAO = mock(WrongTriesDAO.class);
        userDaoMock = mock(UserDAO.class);
        efetuarLoginPM = new PerformLoginPM();
        efetuarLoginPM.setWtd(wrongTriesDAO);
        efetuarLoginPM.setUserDao(userDaoMock);
    }

    @Test
    public void testClear() {
        efetuarLoginPM.setLogin("andre");
        efetuarLoginPM.setPassword("123");
        
        efetuarLoginPM.clear();
        
        assertEquals("", efetuarLoginPM.getLogin());
        assertEquals("", efetuarLoginPM.getPassword());
    }
    
    @Test
    public void testEmptyFields() {
        efetuarLoginPM.setLogin("");
        efetuarLoginPM.setPassword("");
        
        try {
            efetuarLoginPM.pressLogin();
            fail();
        } catch(Exception e) {
            assertEquals("Empty fields", e.getMessage());
        }
    }
    
    @Test
    public void testInexistentUsername() {
        when(userDaoMock.getByName("andre"))
                .thenReturn(null);

        efetuarLoginPM.setLogin("andre");
        efetuarLoginPM.setPassword("123");
        
        try {
            efetuarLoginPM.pressLogin();
            fail();
        } catch(Exception e) {
            assertEquals("Inexistent username", e.getMessage());
        }
    }

    @Test
    public void testWrongPassword() {
        when(userDaoMock.getByName("andre"))
                .thenReturn( new User("andre", "1234", UserType.NORMALUSER) );

        efetuarLoginPM.setLogin("andre");
        efetuarLoginPM.setPassword("123");


        try {
            efetuarLoginPM.pressLogin();
            fail();
        } catch(Exception e) {
            assertEquals("Wrong password", e.getMessage());
        }
    }

    @Test
    public void testWrongPassword3x() {
        User u = new User("andre", "1234", UserType.NORMALUSER);
        when(userDaoMock.getByName("andre"))
                .thenReturn( u );



        efetuarLoginPM.setLogin("andre");
        efetuarLoginPM.setPassword("123");

        try {
            when(wrongTriesDAO.count( u ))
                    .thenReturn(3);
            efetuarLoginPM.pressLogin();
            fail();

        } catch(Exception e) {
            assertEquals("Account is blocked", e.getMessage());
        }
    }

    @Test
    public void testWrongPassword2x() {
        when(userDaoMock.getByName("andre"))
                .thenReturn( new User("andre", "1234", UserType.NORMALUSER) );

        efetuarLoginPM.setLogin("andre");
        efetuarLoginPM.setPassword("123");


        try {
            efetuarLoginPM.pressLogin();
            fail();

        } catch(Exception e) {
            assertEquals("Wrong password", e.getMessage());
        }

        try {
            efetuarLoginPM.pressLogin();
            fail();

        } catch(Exception e) {
            assertEquals("Wrong password", e.getMessage());
        }

        try {
            efetuarLoginPM.setPassword("1234");
            assertNotNull(efetuarLoginPM.pressLogin());
        } catch(Exception e) { }

    }

    @Test
    public void testAdminUserLogin() throws Exception {
        when(userDaoMock.getByName("admin"))
                .thenReturn( new User("admin", "admin", UserType.ADMIN) );        
        
        efetuarLoginPM.setLogin("admin");
        efetuarLoginPM.setPassword("admin");
        
        PagePM pagePM = efetuarLoginPM.pressLogin();
        assertTrue( pagePM instanceof AdminMainPagePM );
        assertEquals("admin", pagePM.getLoggedUser().getUsername());
    }
    
    @Test
    public void testNormalUserLogin() throws Exception {
        when(userDaoMock.getByName("user"))
                .thenReturn( new User("user", "normal", UserType.NORMALUSER) );        
        
        efetuarLoginPM.setLogin("user");
        efetuarLoginPM.setPassword("normal");
        
        PagePM pagePM = efetuarLoginPM.pressLogin();
        assertTrue( pagePM instanceof NormalUserMainPagePM );
        assertEquals("user", pagePM.getLoggedUser().getUsername());
    }    
}