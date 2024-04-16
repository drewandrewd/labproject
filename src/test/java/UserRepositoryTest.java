import org.example.model.User;
import org.example.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты класса UserRepository")
public class UserRepositoryTest {

    private UserRepository userRepository;
    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        testUser = new User("testUser", "password");
    }

    @Test
    @DisplayName("Регистрация пользователя")
    void register_AddUserToRepository() {
        userRepository.register(testUser);
        assertTrue(userRepository.getAllUsers().containsKey("testUser"));
        assertEquals(testUser, userRepository.getAllUsers().get("testUser"));
    }

    @Test
    @DisplayName("Вход пользователя")
    void login_ReturnUserIfExists() {
        userRepository.register(testUser);
        User loggedInUser = userRepository.login("testUser", "password");
        assertNotNull(loggedInUser);
        assertEquals(testUser, loggedInUser);
    }

    @Test
    @DisplayName("Вход пользователя (пользователь не существует)")
    void login_IfUserDoesNotExist() {
        User loggedInUser = userRepository.login("nonExistingUser", "password");
        assertNull(loggedInUser);
    }

    @Test
    @DisplayName("Вход пользователя (неправильный пароль)")
    void login_IfPasswordIncorrect() {
        userRepository.register(testUser);
        User loggedInUser = userRepository.login("testUser", "wrongPassword");
        assertNull(loggedInUser);
    }
}
