package com.example.filterandlogging.member.controller;

import com.example.filterandlogging.member.domain.Member;
import com.example.filterandlogging.member.dto.MemberReqDto;
import com.example.filterandlogging.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> memberCreate(@RequestBody MemberReqDto memberReqDto) {
        Member member = memberService.create(memberReqDto);
        return new ResponseEntity<>(member.getId(), HttpStatus.OK);
    }
}
