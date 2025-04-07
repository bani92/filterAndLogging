package com.example.filterandlogging.member.service;

import com.example.filterandlogging.member.domain.Member;
import com.example.filterandlogging.member.dto.MemberLoginReqDto;
import com.example.filterandlogging.member.dto.MemberReqDto;
import com.example.filterandlogging.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member create(MemberReqDto memberReqDto) {
        // 이미 가입되어있는 이메일 체크
        if(memberRepository.findByEmail(memberReqDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Member newMember = Member.builder()
                .name(memberReqDto.getName())
                .email(memberReqDto.getEmail())
                .password(passwordEncoder.encode(memberReqDto.getPassword()))
                .build();

        return memberRepository.save(newMember);
    }

    public Member login(MemberLoginReqDto memberLoginReqDto) {
        Member member = memberRepository.findByEmail(memberLoginReqDto.getEmail()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));
        // DTO의 비밀번호를 암호화하여 member의 암호화된 비밀번호와 비교
        if(!passwordEncoder.matches(memberLoginReqDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return member;
    }
}
