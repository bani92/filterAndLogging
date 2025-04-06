package com.example.filterandlogging.member.service;

import com.example.filterandlogging.member.domain.Member;
import com.example.filterandlogging.member.dto.MemberReqDto;
import com.example.filterandlogging.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member create(MemberReqDto memberReqDto) {
        // 이미 가입되어있는 이메일 체크
        if(memberRepository.findByEmail(memberReqDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Member newMember = Member.builder()
                .name(memberReqDto.getName())
                .email(memberReqDto.getEmail())
                .password(memberReqDto.getPassword())
                .build();

        return memberRepository.save(newMember);
    }
}
