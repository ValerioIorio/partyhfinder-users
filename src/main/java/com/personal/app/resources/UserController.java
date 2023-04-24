package com.personal.app.resources;

import com.personal.app.model.UserDTO;
import com.personal.app.model.UserPageDTO;
import com.personal.app.models.filters.UserFilter;
import com.personal.app.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequiredArgsConstructor
@Log4j2
public class UserController implements UsersApi{
    @Override
    public ResponseEntity<UserDTO> addUser(UserDTO userDTO) {
        return ResponseEntity.ok(this.userService.addUser(userDTO));
    }

    @Override
    public ResponseEntity<UserDTO> updateUser(UserDTO userDTO) {
        return ResponseEntity.ok(this.userService.updateUser(userDTO));
    }
    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        this.userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<UserDTO> getUser(Long id) {
        return ResponseEntity.ok(this.userService.getUser(id));
    }

    @Override
    public ResponseEntity<UserPageDTO> findAllUsers(Integer pageSize, Integer numberOfPage, Boolean descending, String sortBy, String username) {
        return ResponseEntity.ok(this.userService.findAllUsers(new UserFilter(pageSize, numberOfPage, descending, sortBy, username)));
    }

    @Override
    public ResponseEntity<Void> checkUser(Long id) {
        this.userService.checkUser(id);
        return ResponseEntity.ok().build();
    }

    private final UserService userService;
}
