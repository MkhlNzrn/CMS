package ru.abolsoft.core.account;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.abolsoft.core.account.AuthService;

@SpringBootTest
class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Test
    void auth() {
        var id = authService.registration("TEST", "TEST");
        Assertions.assertNotNull(id);
    }
}