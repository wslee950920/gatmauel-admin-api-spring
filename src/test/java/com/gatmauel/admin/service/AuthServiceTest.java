package com.gatmauel.admin.service;

import com.gatmauel.admin.dto.admin.AdminDTO;
import com.gatmauel.admin.entity.admin.Admin;
import com.gatmauel.admin.entity.admin.AdminRole;
import com.gatmauel.admin.entity.admin.EmailAddressException;
import com.gatmauel.admin.repository.admin.AdminRepository;
import com.gatmauel.admin.service.auth.AuthService;
import com.gatmauel.admin.service.common.EmailService;
import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Validator validator;

    @Autowired
    private AdminRepository adminRepository;

    @MockBean
    private EmailService emailService;

    private String email="admin@gatmauel.com";

    private String password="password";

    @Before
    public void registerAdmin() throws Exception{
        Admin admin= Admin.builder()
                .email(this.email)
                .hashedPassword(passwordEncoder.encode(this.password))
                .build();
        admin.addAdminRole(AdminRole.ADMIN);

        doNothing().when(emailService).sendConfirmEmail(this.email, "token");

        adminRepository.save(admin);
    }

    @After
    public void cleanAll(){
        adminRepository.deleteAll();
    }

    @Test
    public void testRegisterAdmin() throws Exception {
        String email="amicusadaras6@gatmauel.com";
        String password="password";

        AdminDTO dto= AdminDTO.builder()
                .email(email)
                .password(password).build();

        doNothing().when(emailService).sendConfirmEmail(email, "token");

        Map<String, Object> result=authService.register(dto);
        log.debug(result);

        assertThat(result.get("email")).isEqualTo(email);
    }

    @Test
    public void testRegisterNotEmail(){
        String email="qkdrnvhrrur";
        String password="password";

        AdminDTO dto=AdminDTO.builder().
                email(email).
                password(password).build();

        Set<ConstraintViolation<AdminDTO>> violations = validator.validate(dto);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test(expected = EmailAddressException.class)
    public void testRegisterConflictEmail() throws Exception{
        AdminDTO dto=AdminDTO.builder()
                .email(this.email)
                .password(this.password)
                .build();

        Map<String, Object> result=authService.register(dto);
        log.debug(result);
    }

    @Test
    public void testModifyPassword() throws Exception{
        String newPassword="newPassword";

        Optional<Admin> optional=adminRepository.findByEmail(this.email);
        Admin admin=optional.get();

        AdminDTO dto=authService.entityToDTO(admin);
        dto.setPassword(newPassword);
        log.debug("modify password dto : {}", dto);

        Map<String, Object> result=authService.modify(admin.getId(), dto);

        assertThat(result.get("id")).isEqualTo(admin.getId());
        assertThat(result.get("email")).isEqualTo(admin.getEmail());
    }

    @Test
    public void testRemoveAdmin(){
        Optional<Admin> optional=adminRepository.findByEmail(this.email);
        Admin admin=optional.get();

        Map<String, Long> result=authService.remove(admin.getId());

        assertThat(result.get("deleted")).isEqualTo(admin.getId());
    }
}
