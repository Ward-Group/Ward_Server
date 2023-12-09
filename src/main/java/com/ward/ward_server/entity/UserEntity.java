package com.ward.ward_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntity {
    private long id;
    private String email;

    @JsonIgnore
    private String password;

    private String role;

    private String extraInfo;
}
