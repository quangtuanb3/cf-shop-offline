package com.cg.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Accessors(chain = true)
public class UserParam {
    @NotNull
    @NotBlank
    @Length(min = 3)
    private String username;
    private String password;
}
