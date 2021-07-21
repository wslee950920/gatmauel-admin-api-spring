package com.gatmauel.admin.config.security.handler;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ApiLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        response.setContentType("application/json;charset=utf-8");

        JSONObject json=new JSONObject();
        json.put("code", "204");

        PrintWriter out=response.getWriter();
        out.print(json);
    }
}
