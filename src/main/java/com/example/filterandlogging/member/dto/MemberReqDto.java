package com.example.filterandlogging.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberReqDto {
    private String name;
    private String email;
    private String password;
}
