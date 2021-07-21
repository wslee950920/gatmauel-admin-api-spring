package com.gatmauel.admin.service.auth;

import com.gatmauel.admin.dto.admin.AdminDTO;
import com.gatmauel.admin.entity.admin.Admin;
import com.gatmauel.admin.entity.admin.RegistrationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

public interface AuthService {
    Map<String, Object> register(AdminDTO dto) throws Exception;

    Map<String, Object> modify(Long id, AdminDTO dto);

    Map<String, Long> remove(Long id);

    String confirm(String token) throws Exception;

    default Admin dtoToEntity(AdminDTO dto){
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

        Admin admin=Admin.builder()
                .email(dto.getEmail())
                .hashedPassword(passwordEncoder.encode(dto.getPassword()))
                .eVerified(dto.isEVerified())
                .nick(dto.getNick())
                .build();

        return admin;
    }

    default AdminDTO entityToDTO(Admin admin){
        AdminDTO dto= AdminDTO.builder().
                id(admin.getId()).
                email(admin.getEmail()).
                password(admin.getHashedPassword()).
                eVerified(admin.isEVerified()).
                nick(admin.getNick()).
                build();

        return dto;
    }
}
