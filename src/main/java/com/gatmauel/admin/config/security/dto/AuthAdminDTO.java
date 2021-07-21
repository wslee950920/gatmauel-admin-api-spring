package com.gatmauel.admin.config.security.dto;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Log4j2
@Getter
@ToString
public class AuthAdminDTO extends User {
    private Long id;

    private String email;

    private boolean eVerified;

    public AuthAdminDTO(
            Long id,
            String email,
            String password,
            boolean eVerified,
            Collection<? extends GrantedAuthority> authorities
    ){
        super(email, password, authorities);

        this.id=id;
        this.email=email;
        this.eVerified=eVerified;
    }
}
