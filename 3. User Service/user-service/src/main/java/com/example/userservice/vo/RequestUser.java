package com.example.userservice.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;


@Data
public class RequestUser {
    @NotNull
    @Size(min=2, message = "Email not be less than two characters")
    @Email
    private String email;
    @NotNull(message= "Name cannot be null")
    @Size(min=2, message = "Name not be less than two characters")
    private String name;
    @NotNull(message= "Password cannot be null")
    @Size(min=6, message = "Password not be less than 6 characters")
    private  String pwd;

}
