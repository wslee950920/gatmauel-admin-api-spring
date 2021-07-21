package com.gatmauel.admin.security;

import com.gatmauel.admin.entity.admin.Admin;
import com.gatmauel.admin.entity.admin.AdminRole;
import com.gatmauel.admin.repository.admin.AdminRepository;

import lombok.extern.log4j.Log4j2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class AuthAdminTest {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void insertAdminTest(){
        String email="amicusadaras6@gmail.com";
        String password="@DnTjr753@";

        Admin admin= Admin.builder()
                .email(email)
                .hashedPassword(passwordEncoder.encode(password))
                .eVerified(true)
                .build();
        admin.addAdminRole(AdminRole.ADMIN);

        adminRepository.save(admin);
    }

    @After
    public void cleanup() {
        adminRepository.deleteAll();
    }

    @Test
    public void foo(){}
}
