package com.cg.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserParam {
    private String username;
    private String password;
    private Long roleId;
}
