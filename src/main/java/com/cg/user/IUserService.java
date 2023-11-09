package com.cg.user;

import com.cg.model.user.User;
import com.cg.user.dto.UserParam;
import com.cg.user.dto.UserResult;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService{
    List<UserResult> findAll();

    User findById(Long id);

    UserResult getById(Long id);

    UserResult create(UserParam creationParam);

    UserResult update(Long id, UserParam userParam);
}
