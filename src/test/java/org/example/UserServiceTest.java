package org.example;

import org.example.enums.UserRole;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        List<User> users = new ArrayList<>();
        User user = new User(1L, "abc", "abc123", "abc@gmail.com", "ABC", "Ha Noi", "0123456", UserRole.CUSTOMER);
        users.add(user);
        userRepository = mock(UserRepository.class);
        when(userRepository.getAllUser()).thenReturn(users);

        userService = new UserService(userRepository);
    }

    @Test
    void should_return_true_if_user_login_with_valid_username_and_password() {
        String username = "abc";
        String password = "abc123";

        assertThat(userService.login(username, password).getUsername(), equalTo("abc"));
        verify(userRepository, times(1)).getAllUser();

    }

    @Test
    void should_throw_exception_if_user_login_with_invalid_username_and_password() {
        String username = "abcdef";
        String password = "abc123";

        Exception exception = assertThrows(RuntimeException.class, ()->userService.login(username, password));
        assertThat(exception.getMessage(), equalTo("User not found"));
        verify(userRepository, times(1)).getAllUser();
    }
}
