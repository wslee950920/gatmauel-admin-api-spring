package com.gatmauel.admin.config.security.handler;

import com.gatmauel.admin.config.security.dto.AuthAdminDTO;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        log.debug(authentication);

        AuthAdminDTO dto=(AuthAdminDTO) authentication.getPrincipal();

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=utf-8");

        JSONObject json=new JSONObject();
        json.put("id", dto.getId());
        json.put("email", dto.getEmail());

        PrintWriter out=response.getWriter();
        out.print(json);
    }
}
