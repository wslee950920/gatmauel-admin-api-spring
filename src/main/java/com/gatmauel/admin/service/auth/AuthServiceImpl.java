package com.gatmauel.admin.service.auth;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.gatmauel.admin.dto.admin.AdminDTO;
import com.gatmauel.admin.entity.admin.Admin;
import com.gatmauel.admin.entity.admin.AdminRole;
import com.gatmauel.admin.entity.admin.EmailAddressException;
import com.gatmauel.admin.repository.admin.AdminRepository;
import com.gatmauel.admin.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{
    private final AdminRepository adminRepository;

    private final AmazonSimpleEmailService sesClient;

    private final JWTUtil jwtUtil;

    private final SpringTemplateEngine templateEngine;

    private final PasswordEncoder passwordEncoder;

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

        String From="noreply@gatmauel.com";
        String To="gatmauel9300@gmail.com";
        String Subject="갯마을 관리자 인증";

        String token=jwtUtil.generateToken(admin.getId());
        log.debug(token);

        Context context=new Context();
        context.setVariable("token", token);
        context.setVariable("email", dto.getEmail());

        String html=templateEngine.process("register", context);
        log.debug(html);

        //uncheckd exception    //밖으로 안 던지고 try-catch로 처리하면 롤백 안 된다.
        SendEmailRequest request=new SendEmailRequest().
                withDestination(new Destination().withToAddresses(To)).
                withMessage(new Message()
                        .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(html)))
                        .withSubject(new Content().withCharset("UTF-8").withData(Subject)))
                        .withSource(From);

        sesClient.sendEmail(request);
        log.info("Email sent");

        HashMap<String, Object> result=new HashMap<>();

        result.put("id", admin.getId());
        result.put("email", admin.getEmail());

        return result;
    }

    @Transactional
    public Map<String, Object> modify(Long id, AdminDTO dto){
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
        Long id=jwtUtil.validateAndExtract(token);
        log.debug("id {}", id);

        Admin admin=adminRepository.getById(id);
        admin.changeEVerified(true);

        adminRepository.save(admin);

        return admin.getEmail();
    }
}
