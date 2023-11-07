package com.cg.role;

import com.cg.model.Role;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;


public interface IRoleService{
    List<Role> findAll();

    Role findById(Long id);

    Role save(Role role);

    void delete(Role role);

    void deleteById(Long id);
}
