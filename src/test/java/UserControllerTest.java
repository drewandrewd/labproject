import org.example.in.UserController;
import org.example.repositories.AuditRepository;
import org.example.model.User;
import org.example.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты класса UserController")
public class UserControllerTest {


    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    @DisplayName("Регистрация пользователя")
    void register_shouldAddUserToRepository() {
        userController.register("testUser", "password");
        User user = userController.getUserRepository().login("testUser", "password");
        assertNotNull(user);
        assertEquals("testUser", user.getName());
    }

    @Test
    @DisplayName("Аутентификация пользователя")
    void login_shouldAuthenticateUser() {
        userController.register("testUser", "password");
        userController.login("testUser", "password");
        assertTrue(userController.isLoggedIn());
    }

    @Test
    @DisplayName("Аутентификация пользователя (неверные учетные данные)")
    void login_shouldNotAuthenticateUserWithInvalidCredentials() {
        userController.register("testUser", "password");
        userController.login("testUser", "wrongPassword");
        assertFalse(userController.isLoggedIn());
    }

    @Test
    @DisplayName("Выход пользователя")
    void logout_shouldLogoutUser() {
        userController.register("testUser", "password");
        userController.login("testUser", "password");
        userController.logout();
        assertFalse(userController.isLoggedIn());
    }

    @Test
    @DisplayName("Проверка на администратора")
    void isAdmin_shouldReturnTrueForAdminUser() {
        User adminUser = new User("admin", "adminPassword");
        adminUser.setAdmin(true);
        userController.getUserRepository().register(adminUser);
        userController.login("admin", "adminPassword");
        System.out.println(userController.getUserRepository().getAllUsers());
        assertTrue(userController.isAdmin());
    }

}

