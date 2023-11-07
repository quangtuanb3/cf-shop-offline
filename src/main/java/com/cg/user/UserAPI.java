package com.cg.user;

import com.cg.user.dto.UserParam;
import com.cg.user.dto.UserResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAPI {
    private final IUserService userService;

    @GetMapping
    public ResponseEntity<?> findAll(String username) {
        List<UserResult> dtoList = userService.findAll();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(Long id) {
        UserResult dto = userService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<?> create(UserParam creationParam) {
        UserResult dto = userService.create(creationParam);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, UserParam updateParam) {
        UserResult dto = userService.update(id, updateParam);
        return ResponseEntity.ok(dto);
    }
}
