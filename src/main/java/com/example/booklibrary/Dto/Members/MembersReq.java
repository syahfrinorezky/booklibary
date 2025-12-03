package com.example.booklibrary.Dto.Members;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor 
public class MembersReq {
    private String memberCode;

    @NotBlank(message = "Member name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;


    private String phone;
    private String address;
}
