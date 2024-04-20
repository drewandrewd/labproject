import org.example.dao.AuditDao;
import org.example.dao.UserDao;
import org.example.model.User;
import org.example.service.AuditService;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private AuditService auditService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = UserService.getInstance();
        userService.setUserDao(userDao);
        userService.setAuditService(auditService);
    }

    @Test
    void register() {
        String userName = "test_user";
        String password = "password";

        userService.register(userName, password);

        verify(userDao, times(1)).register(any(User.class));
        verify(auditService, times(1)).logAuthentication(userName, true);
    }

    @Test
    void login_Successful() {
        String userName = "test_user";
        String password = "password";
        User user = new User(userName, password);

        when(userDao.login(userName, password)).thenReturn(user);

        userService.login(userName, password);

        assertTrue(userService.isLoggedIn());
        verify(auditService, times(1)).logAuthentication(userName, true);
    }

    @Test
    void login_Unsuccessful() {
        String userName = "test_user";
        String password = "password";

        when(userDao.login(userName, password)).thenReturn(null);

        userService.login(userName, password);

        assertFalse(userService.isLoggedIn());
        verify(auditService, times(1)).logAuthentication(userName, false);
    }

    @Test
    void logout() {
        userService.logout();

        assertFalse(userService.isLoggedIn());
    }

    @Test
    void isAdmin_UserIsNull() {
        assertFalse(userService.isAdmin());
    }

    @Test
    void isAdmin_UserIsAdmin() {
        User user = new User("test_user", "password");
        user.setAdmin(true);

        userService.setCurrentUser(user);

        assertTrue(userService.isAdmin());
    }

    @Test
    void isAdmin_UserIsNotAdmin() {
        User user = new User("test_user", "password");
        user.setAdmin(false);

        userService.setCurrentUser(user);

        assertFalse(userService.isAdmin());
    }

    @Test
    void isLoggedIn_UserIsNull() {
        assertFalse(userService.isLoggedIn());
    }

    @Test
    void isLoggedIn_UserIsNotNull() {
        User user = new User("test_user", "password");

        userService.setCurrentUser(user);

        assertTrue(userService.isLoggedIn());
    }

    @Test
    void setCurrentUser() {
        User user = new User("test_user", "password");

        userService.setCurrentUser(user);

        assertEquals(user, userService.getUser());
    }

    @Test
    void printUsers() {
        userService.printUsers();

        verify(userDao, times(1)).getAllUsers();
    }

    @Test
    void getUserByUsername() {
        String userName = "test_user";
        User user = new User(userName, "password");

        when(userDao.getUserByUsername(userName)).thenReturn(user);

        assertEquals(user, userService.getUserByUsername(userName));
    }
}
