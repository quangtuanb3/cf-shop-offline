package com.cg.user.dto;

import com.cg.model.user.UserRole;
import com.cg.role.dto.RoleResult;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserResult {
    private Long id;
    private String username;
    private UserRole role;
}
