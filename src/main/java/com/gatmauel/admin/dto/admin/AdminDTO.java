package com.gatmauel.admin.dto.admin;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private Long id;

    @Email(message="Email address should be valid")
    private String email;

    @Size(max=100, message="must not more than 100 characters")
    private String password;

    @Builder.Default
    private boolean eVerified=false;

    @Builder.Default
    private String nick="사징님";
}
