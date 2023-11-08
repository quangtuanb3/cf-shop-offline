package com.cg.user;


import com.cg.exception.ResourceNotFoundException;
import com.cg.model.user.User;
import com.cg.user.dto.UserParam;
import com.cg.user.dto.UserResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserResult> findAll() {
        List<User> entities = userRepository.findAll();
        return userMapper.toDTOList(entities);
    }

    @Override
    public User findById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResult getById(Long id) {
        return userMapper.toDTO(findById(id));
    }

    @Override
    @Transactional
    public UserResult create(UserParam creationParam) {
        User entity = userMapper.toEntity(creationParam);
        entity = userRepository.save(entity);
        return userMapper.toDTO(entity);
    }


    @Override
    @Transactional
    public UserResult update(Long id, UserParam userParam) {
        User entity = findById(id);
        userMapper.transferFields(entity, userParam);
        return userMapper.toDTO(entity);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Not found!"));
    }
}
