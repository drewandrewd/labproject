import org.example.service.UserService;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты класса UserController")
public class UserServiceTest {


    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    @DisplayName("Регистрация пользователя")
    void register_shouldAddUserToRepository() {
        userService.register("testUser", "password");
        User user = userService.getUserRepository().login("testUser", "password");
        assertNotNull(user);
        assertEquals("testUser", user.getName());
    }

    @Test
    @DisplayName("Аутентификация пользователя")
    void login_shouldAuthenticateUser() {
        userService.register("testUser", "password");
        userService.login("testUser", "password");
        assertTrue(userService.isLoggedIn());
    }

    @Test
    @DisplayName("Аутентификация пользователя (неверные учетные данные)")
    void login_shouldNotAuthenticateUserWithInvalidCredentials() {
        userService.register("testUser", "password");
        userService.login("testUser", "wrongPassword");
        assertFalse(userService.isLoggedIn());
    }

    @Test
    @DisplayName("Выход пользователя")
    void logout_shouldLogoutUser() {
        userService.register("testUser", "password");
        userService.login("testUser", "password");
        userService.logout();
        assertFalse(userService.isLoggedIn());
    }

    @Test
    @DisplayName("Проверка на администратора")
    void isAdmin_shouldReturnTrueForAdminUser() {
        User adminUser = new User("admin", "adminPassword");
        adminUser.setAdmin(true);
        userService.getUserRepository().register(adminUser);
        userService.login("admin", "adminPassword");
        System.out.println(userService.getUserRepository().getAllUsers());
        assertTrue(userService.isAdmin());
    }

}

