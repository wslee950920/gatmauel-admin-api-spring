package com.gatmauel.admin.config.security.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import org.apache.http.entity.ContentType;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {
    private boolean postOnly=true;

    private HashMap<String, String> jsonRequest;

    public ApiLoginFilter(String defaultFilterProcessesUrl){
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException{
        if(postOnly&&!request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("Authentication method not supported: "+request.getMethod());
        }

        if(request.getHeader("Content-Type").equals(ContentType.APPLICATION_JSON.getMimeType())){
            ObjectMapper mapper=new ObjectMapper();

            try {
                this.jsonRequest=mapper.readValue(request.getReader().lines().collect(Collectors.joining()), new TypeReference<HashMap<String, String>>() {
                });
            } catch(IOException e){
                e.printStackTrace();

                throw new AuthenticationServiceException("Request Content-Type(application/json) Parsing Error");
            }
        }

        String email=jsonRequest.get("email");
        String password=jsonRequest.get("password");

        if(email==null){
            throw new BadCredentialsException("email cannot be null");
        }

        UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(email, password);

        log.debug("attemp");
        return getAuthenticationManager().authenticate(authToken);
    }
}
