package com.gatmauel.admin.repository;

import com.gatmauel.admin.entity.admin.AdminRole;
import org.junit.After;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gatmauel.admin.entity.admin.Admin;
import com.gatmauel.admin.repository.admin.AdminRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AdminRepositoryTest {
    @Autowired
    AdminRepository adminRepository;

    @After
    public void cleanup() {
        adminRepository.deleteAll();
    }

    @Transactional
    @Test
    public void testInsertDefaultAndSelectAdmin() {
        String email="admin@gatmauel.com";

        Admin admin=Admin.builder().
                email(email).
                build();
        admin.addAdminRole(AdminRole.ADMIN);
        adminRepository.save(admin);

        List<Admin> adminsList=adminRepository.findAll();
        Admin result=adminsList.get(0);

        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getHashedPassword()).isEqualTo(null);
        assertThat(result.isEVerified()).isEqualTo(false);
        assertThat(result.getNick()).isEqualTo("사장님");
        assertTrue(result.getRoleSet().contains(AdminRole.ADMIN));
    }

    @Transactional
    @Test
    public void testInsertAndSelectAdmin() {
        String email="admin@gatmauel.com";
        String hashedPassword="dntjr753";
        String nick="맨유경비원";
        boolean eVerified=true;

        Admin admin=Admin.builder().
                email(email).
                hashedPassword(hashedPassword).
                eVerified(eVerified).
                nick(nick).
                build();
        admin.addAdminRole(AdminRole.ADMIN);

        adminRepository.save(admin);

        List<Admin> adminsList=adminRepository.findAll();
        Admin result=adminsList.get(0);

        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getHashedPassword()).isEqualTo(hashedPassword);
        assertThat(result.isEVerified()).isEqualTo(eVerified);
        assertThat(result.getNick()).isEqualTo(nick);
        assertTrue(result.getRoleSet().contains(AdminRole.ADMIN));
    }
}