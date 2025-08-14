package com.tae.Etickette;

import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public void test() {
        throw new RuntimeException("테스트 입니다.");
    }
    @GetMapping("/test/member")
    public void testMe() {
        throw new ResourceNotFoundException(ErrorCode.INTERNAL_SERVER_ERROR,"회원 정보가 없습니다.");
    }
}
