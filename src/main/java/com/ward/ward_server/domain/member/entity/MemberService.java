package com.ward.ward_server.domain.member.entity;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    final MemberRepository memberRepository;
    Member create(String email){
        if(memberRepository.existsByEmail(email)) throw null;
        return memberRepository.save(new Member(email));
    }
}
