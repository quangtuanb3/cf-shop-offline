package com.cg.user;

import com.cg.user.dto.UserParam;
import com.cg.user.dto.UserResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.rananu.shared.Result;
import vn.rananu.shared.annotation.RananuBody;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAPI {
    private final IUserService userService;

    @GetMapping
    @RananuBody
    public List<?> findAll(String username) {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @RananuBody
    public UserResult findById(Long id) {
        return userService.getById(id);
    }

    @PostMapping
    @RananuBody(message = "tao user thanh cong")
    public UserResult create(UserParam creationParam) {
        UserResult dto = userService.create(creationParam);
        return dto;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, UserParam updateParam) {
        UserResult dto = userService.update(id, updateParam);
        return ResponseEntity.ok(dto);
    }
}
