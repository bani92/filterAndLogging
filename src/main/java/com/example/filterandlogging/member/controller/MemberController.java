package com.example.filterandlogging.member.controller;

import com.example.filterandlogging.common.auth.JwtTokenProvider;
import com.example.filterandlogging.member.domain.Member;
import com.example.filterandlogging.member.dto.MemberLoginReqDto;
import com.example.filterandlogging.member.dto.MemberReqDto;
import com.example.filterandlogging.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/create")
    public ResponseEntity<?> memberCreate(@RequestBody MemberReqDto memberReqDto) {
        Member member = memberService.create(memberReqDto);
        return new ResponseEntity<>(member.getId(), HttpStatus.OK);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody MemberLoginReqDto memberLoginReqDto) {
        // email , 패스워드 검증
        Member member = memberService.login(memberLoginReqDto);

        // 일치할경우 access 토큰 발행
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(),member.getRole().toString());
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("token", jwtToken);
        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }
}
