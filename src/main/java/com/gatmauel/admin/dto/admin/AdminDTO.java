package com.gatmauel.admin.dto.admin;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private Long id;

    @Email(message="Email address should be valid")
    @NotNull
    private String email;

    @NotNull
    private String password;

    @Builder.Default
    private boolean eVerified=false;

    @Builder.Default
    private String nick="사징님";
}
