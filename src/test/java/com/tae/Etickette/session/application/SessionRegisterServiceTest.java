package com.tae.Etickette.session.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SessionRegisterServiceTest {
    @Autowired
    public SessionRegisterService sessionRegisterService;

    @Test
    public void a() {
        sessionRegisterService.initSeatingPlan();
    }

}