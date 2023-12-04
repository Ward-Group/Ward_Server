package com.ward.ward_server.domain.member.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    final MemberService memberService;
    @PostMapping("/member")
    ResponseEntity<Member> create(@RequestBody MemberRequest memberRequest){

        Member response=memberService.create(memberRequest.getEmail());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
