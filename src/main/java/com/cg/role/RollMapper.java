package com.cg.role;

import com.cg.model.Role;
import com.cg.role.dto.RoleResult;
import org.springframework.stereotype.Component;

@Component
public class RollMapper {
    public RoleResult toDTO(Role entity) {
        return new RoleResult()
                .setId(entity.getId())
                .setCode(entity.getCode())
                .setName(entity.getName());
    }

    public Role toEntity(RoleResult dto) {
        return new Role()
                .setId(dto.getId())
                .setCode(dto.getCode())
                .setName(dto.getName());
    }
}
