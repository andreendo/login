package PM.login.PM;

import PM.login.model.UserType;
import PM.login.model.User;
import PM.login.DAO.UserDAO;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.*;

/**
 *
 * @author andreendo
 */
public class EfetuarLoginPMTest {
    
    
    PerformLoginPM efetuarLoginPM;
    UserDAO userDaoMock;
    
    public EfetuarLoginPMTest() {
        
    }
    
    @Before
    public void before(){
        efetuarLoginPM = new PerformLoginPM();
        userDaoMock = mock(UserDAO.class);
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
            verify(userDaoMock, times(1)).save(any());
        }
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
    
    @Test
    public void testWrongPassword3Times() {
        when(userDaoMock.getByName("andre"))
                .thenReturn( new User("andre", "1234", UserType.NORMALUSER, 2) );
        
        efetuarLoginPM.setLogin("andre");
        efetuarLoginPM.setPassword("123");

        try {
            efetuarLoginPM.pressLogin();
            fail();
        } catch(Exception e) {
            assertEquals("Blocked password, please contact support.", e.getMessage());
            verify(userDaoMock, times(1)).save(any());
        }
    }
    
    @Test
    public void testLoginThirdTime(){
        when(userDaoMock.getByName("andre"))
                .thenReturn( new User("andre", "1234", UserType.NORMALUSER, 2) );
        
        efetuarLoginPM.setLogin("andre");
        efetuarLoginPM.setPassword("1234");

        try {
            PagePM pagePm = efetuarLoginPM.pressLogin();
            assertEquals(pagePm.getLoggedUser().getWrongAttempts(), 0);
            verify(userDaoMock, times(1)).save(any());
        } catch(Exception e) {
            fail();
        }
    }
    
    @Test
    public void testCorrectLoginWithBlockedPassword() {
        when(userDaoMock.getByName("andre"))
                .thenReturn( new User("andre", "1234", UserType.NORMALUSER, 3) );
        
        efetuarLoginPM.setLogin("andre");
        efetuarLoginPM.setPassword("1234");

        try {
            efetuarLoginPM.pressLogin();
            fail();
        } catch(Exception e) {
            assertEquals("Blocked password, please contact support.", e.getMessage());
        }
    }
}