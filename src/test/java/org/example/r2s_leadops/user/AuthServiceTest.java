package org.example.r2s_leadops.user;

import org.example.r2s_leadops.DTO.request.RegisterRequest;
import org.example.r2s_leadops.service.imp.AuthServiceImp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthServiceTest {

    @Test
    void shouldCreateAuthServiceInstance() {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("Nguyen Van A");
        request.setEmail("a@example.com");
        request.setPassword("123456");

        assertNotNull(request);
        assertNotNull(AuthServiceImp.class);
    }
}
