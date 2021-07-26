package com.gatmauel.admin.service.auth;

import com.gatmauel.admin.dto.admin.AdminDTO;
import com.gatmauel.admin.entity.admin.Admin;
import com.gatmauel.admin.entity.admin.AdminRole;
import com.gatmauel.admin.entity.admin.EmailAddressException;
import com.gatmauel.admin.repository.admin.AdminRepository;
import com.gatmauel.admin.service.common.EmailService;
import com.gatmauel.admin.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{
    private final AdminRepository adminRepository;

    private final JWTUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    @Transactional
    @Override
    public Map<String, Object> register(AdminDTO dto) throws Exception {
        Admin admin;

        Optional<Admin> optional=adminRepository.findByEmail(dto.getEmail());
        log.debug(optional.isPresent());
        if(optional.isPresent()){
            throw new EmailAddressException("Email already exist");
        }

        admin=dtoToEntity(dto);
        admin.addAdminRole(AdminRole.ADMIN);
        log.debug(admin);

        adminRepository.save(admin);

        String token=jwtUtil.generateToken(admin.getId());
        log.debug(token);

        emailService.sendConfirmEmail(admin.getEmail(), token);

        HashMap<String, Object> result=new HashMap<>();

        result.put("id", admin.getId());
        result.put("email", admin.getEmail());

        return result;
    }

    @Transactional
    public Map<String, Object> modify(Long id, AdminDTO dto) throws Exception{
        Assert.hasText(dto.getPassword(), "password must not empty");

        Admin admin=adminRepository.getById(id);
        admin.changePassword(passwordEncoder.encode(dto.getPassword()));

        adminRepository.save(admin);
        log.debug("modify password result : {}", admin);

        Map<String, Object> result=new HashMap<>();
        result.put("id", admin.getId());
        result.put("email", admin.getEmail());

        return result;
    }

    //hard delete하면 알아서 admin_role_set에서 레코드를 지운다.
    @Transactional
    public Map<String, Long> remove(Long id){
        Admin admin=adminRepository.getById(id);
        admin.updateDelDate();

        adminRepository.save(admin);

        Map<String, Long> result=new HashMap<>();
        result.put("deleted", id);

        return result;
    }

    @Transactional
    public String confirm(String token) throws Exception{
        Assert.hasText(token, "token must not empty");

        Long id=jwtUtil.validateAndExtract(token);
        log.debug("id {}", id);

        Admin admin=adminRepository.getById(id);
        admin.changeEVerified(true);

        adminRepository.save(admin);

        return admin.getEmail();
    }
}
