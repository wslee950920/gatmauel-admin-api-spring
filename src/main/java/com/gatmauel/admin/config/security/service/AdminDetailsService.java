package com.gatmauel.admin.config.security.service;

import com.gatmauel.admin.config.security.dto.AuthAdminDTO;
import com.gatmauel.admin.entity.admin.Admin;
import com.gatmauel.admin.repository.admin.AdminRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        log.debug("username:"+username);
        Optional<Admin> optional=adminRepository.findByEmail(username);
        if(optional.isEmpty()){
            log.debug("empty - "+optional);
            throw new UsernameNotFoundException("Check Email");
        }

        Admin admin=optional.get();
        log.debug("admin:"+admin);
        if(admin.getDelDate()!=null){
            throw new UsernameNotFoundException("Deleted Email");
        }

        if(!admin.isEVerified()){
            throw new UsernameNotFoundException("Verify Email");
        }

        AuthAdminDTO dto=new AuthAdminDTO(
                admin.getId(),
                admin.getEmail(),
                admin.getHashedPassword(),
                admin.isEVerified(),
                admin.getRoleSet().stream()
                        .map(role->new SimpleGrantedAuthority(("ROLE_"+role.name())))
                        .collect(Collectors.toSet())
        );

        return dto;
    }
}
