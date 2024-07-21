package com.example.demo;

import com.example.demo.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testLombok() {
        Member member = new Member();
        member.setName("John Doe");
        member.setEmail("john.doe@example.com");

        assertEquals("John Doe", member.getName());
        assertEquals("john.doe@example.com", member.getEmail());
    }

}
