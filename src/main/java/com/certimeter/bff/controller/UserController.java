package com.certimeter.bff.controller;

import com.certimeter.bff.resourcemodel.User;
import com.certimeter.bff.pagination.UserResPagination;
import com.certimeter.bff.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/bff/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserResPagination> getAllUsers(
                                                         @RequestParam(value = "page", defaultValue = "0", required = false) int pageNo,
                                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                         @RequestParam Optional <String> username,
                                                         @RequestParam Optional <String> matchMode,
                                                         @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        return new ResponseEntity<>(userService.getAllUsers(accessTokenTrunked, pageNo, pageSize, username, matchMode), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        return ResponseEntity.ok(userService.addUser(accessTokenTrunked, user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        return ResponseEntity.ok(userService.updateUser(accessTokenTrunked, id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        return ResponseEntity.ok(userService.removeUser(accessTokenTrunked, id));
    }
}