package org.example.ticketservice.infrastructure.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DelegateVerifier implements ApplicationRunner {

    @Autowired
    private ApplicationContext ctx;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("Delegate bean present: " +
                ctx.containsBean("handlePromotionRequestApprovalDelegate"));
    }
}