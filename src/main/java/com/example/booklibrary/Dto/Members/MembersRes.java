package com.example.booklibrary.Dto.Members;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembersRes {
    private Long id;
    private String memberCode;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
}
