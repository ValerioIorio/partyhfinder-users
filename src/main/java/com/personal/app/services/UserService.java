package com.personal.app.services;

import com.personal.app.model.UserDTO;
import com.personal.app.model.UserPageDTO;
import com.personal.app.models.filters.UserFilter;

public interface UserService {
    UserDTO addUser(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO);

    void deleteUser(Long id);

    UserDTO getUser(Long id);

    UserPageDTO findAllUsers(UserFilter userFilter);
}
