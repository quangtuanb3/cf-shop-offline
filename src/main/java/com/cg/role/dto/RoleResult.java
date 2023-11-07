package com.cg.role.dto;

import com.cg.model.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class RoleResult {
    private Long id;
    private String code;
    private ERole name;

//    public Role toRoleDTO() {
//        return new Role()
//                .setId(id)
//                .setCode(code)
//                .setName(name);
//    }

}
