//package com.ward.ward_server.controller;
//
//import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
//import com.ward.ward_server.api.user.auth.security.UserPrincipalAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.test.context.support.WithSecurityContextFactory;
//
//import java.util.Arrays;
//
//public class WithMockUserSecurityContextFactory implements WithSecurityContextFactory<WithMockUser> {
//
//    @Override
//    public SecurityContext createSecurityContext(WithMockUser annotation) {
//        var authorities = Arrays.stream(annotation.authorities())
//                .map(SimpleGrantedAuthority::new)
//                .toList();
//
//        var principle= CustomUserDetails.builder()
//                .userId(annotation.userId())
//                .email("fake@email.com")
//                .authorities(authorities)
//                .build();
//
//        var context = SecurityContextHolder.createEmptyContext();
//        context.setAuthentication(new UserPrincipalAuthenticationToken(principle));
//        return context;
//    }
//}
