package com.gatmauel.admin.service.common;

public interface EmailService {
    void sendConfirmEmail(String email, String token) throws Exception;
}
