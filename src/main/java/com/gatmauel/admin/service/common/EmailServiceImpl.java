package com.gatmauel.admin.service.common;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;

import com.amazonaws.services.simpleemail.model.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

import org.springframework.util.Assert;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Getter
@RequiredArgsConstructor
@Log4j2
@Service
public class EmailServiceImpl implements EmailService{
    private final AmazonSimpleEmailService sesClient;

    private final SpringTemplateEngine templateEngine;

    private final String to="gatmauel9300@gmail.com";

    private final String from="noreply@gatmauel.com";

    private final String subject="갯마을 관리자 인증";

    @Override
    public void sendConfirmEmail(String email, String token) throws Exception{
        Assert.hasText(email, "email must not null");
        Assert.hasText(token, "token must not null");

        String html=createMessageBody(email, token);

        SendEmailRequest request=new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(this.to))
                .withMessage(new Message()
                        .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(html)))
                        .withSubject(new Content().withCharset("UTF-8").withData(this.subject)))
                .withSource(this.from);

        sesClient.sendEmail(request);
        log.info("sent email");
    }

    private String createMessageBody(String email, String token) {
        try {
            Context context=new Context();
            context.setVariable("token", token);
            context.setVariable("email", email);

            return templateEngine.process("register", context);
        } catch (Exception e) {
            log.error(e.getMessage());

            return null;
        }
    }
}
