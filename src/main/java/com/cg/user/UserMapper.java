package com.cg.user;

import com.cg.model.user.User;
import com.cg.user.dto.UserParam;
import com.cg.user.dto.UserResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResult toDTO(User entity) {
        return new UserResult()
                .setId(entity.getId())
                .setUsername(entity.getUsername())
                .setRole(entity.getRole());
    }

    public User toEntity(UserParam creationParam) {
        return new User()
                .setUsername(creationParam.getUsername());
    }

    public void transferFields(User entity, UserParam updateParam) {
        entity.setUsername(updateParam.getUsername());
    }

    public List<UserResult> toDTOList(List<User> entities) {
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
