package com.example.onlinebookstore;

import com.example.onlinebookstore.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class OnlineBookStoreApplicationTests {

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void contextLoads() {
    }

}
