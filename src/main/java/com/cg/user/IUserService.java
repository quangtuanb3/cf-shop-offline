package com.cg.user;

import com.cg.model.User;
import com.cg.service.IGeneralService;
import com.cg.user.dto.UserParam;
import com.cg.user.dto.UserResult;

import java.util.List;

public interface IUserService {
    List<UserResult> findAll();

    User findById(Long id);

    UserResult getById(Long id);

    UserResult create(UserParam creationParam);

    UserResult update(Long id, UserParam userParam);

    Boolean existsByUsername(String username);

    User save(User user);

    User getByUsername(String username);

    User findByUsername(String username);
}
