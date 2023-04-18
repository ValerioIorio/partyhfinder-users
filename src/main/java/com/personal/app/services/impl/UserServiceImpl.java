package com.personal.app.services.impl;

import com.partyh.finder.common.exception.impl.PFNotFoundException;
import com.partyh.finder.common.utils.FilterUtil;
import com.partyh.finder.common.validators.commons.ValidationUtils;
import com.personal.app.model.MissiveDTO;
import com.personal.app.model.MissivePageDTO;
import com.personal.app.models.costants.UserConstants;
import com.personal.app.models.entities.Missive;
import com.personal.app.models.entities.User;
import com.personal.app.model.UserDTO;
import com.personal.app.model.UserPageDTO;
import com.personal.app.models.filters.UserFilter;
import com.personal.app.repository.UserRepository;
import com.personal.app.services.UserService;
import com.personal.app.specifications.UserSpecification;
import com.personal.app.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    public UserDTO addUser(UserDTO input) {
        // validate input
        userValidator.validate(input, HttpMethod.POST);
        //set default values
        User user = mapper.map(input, User.class);
        user.setStatus(UserConstants.ENABLED);
        user.setUpdatedAt(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setChats(null);
        user.setMatchedUsers(null);
        return mapper.map(userRepository.save(user), UserDTO.class);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        // validate input
        userValidator.validateId(userDTO.getId(), HttpMethod.PUT);
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());

        //validate dto
        userValidator.validate(userDTO, HttpMethod.PUT, new LinkedHashMap<>(Map.of("user", optionalUser.isPresent())));

        //get the previous user and the new user
        User previousUser = optionalUser.get();
        User user = mapper.map(userDTO, User.class);


        //unchangeable values are email, password, phone number, created at

        //set default values
        user.setUpdatedAt(LocalDateTime.now());
        //set unchangeable values
        user.setCreatedAt(previousUser.getCreatedAt());
        user.setEmail(previousUser.getEmail());
        user.setPassword(previousUser.getPassword());
        user.setPhoneNumber(previousUser.getPhoneNumber());
        user.setLastLogin(previousUser.getLastLogin());
        user.setLastLogout(previousUser.getLastLogout());
        user.setLastFailedLogin(previousUser.getLastFailedLogin());
        user.setStatus(previousUser.getStatus());
        return mapper.map(userRepository.save(user), UserDTO.class);
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
        }else {
            throw new PFNotFoundException("User not found");
        }
    }

    @Override
    public UserDTO getUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return mapper.map(optionalUser.get(), UserDTO.class);
        }else {
            throw new PFNotFoundException("User not found");
        }
    }

    @Override
    public UserPageDTO findAllUsers(UserFilter filter) {
        //set default values of sorting
        if(!filter.checkValidSortBy(ALLOWED_SORT_BY)){
            filter.setSortBy(DEFAULT_SORT_BY);
        }
        //set default values of descending
        if(ValidationUtils.isNull(filter.getDescending())){
            filter.setDescending(DEFAULT_SORT_ORDER);
        }

        //get the page of Users
        Page<User> userPage = userRepository.findAll(
                userSpecification.buildSpecification(filter),
                PageRequest.of(FilterUtil.getNumberOfPage(filter.getNumberOfPage()), FilterUtil.getPageSize(filter.getPageSize()))
                        .withSort(FilterUtil.getDirection(filter.getDescending()), FilterUtil.getSortBy(filter.getSortBy())
                        )
        );

        //map the page of missive to page of missiveDTO
        UserPageDTO pageDTO = new UserPageDTO();
        pageDTO.setItems(userPage.stream().map(user -> mapper.map(user, UserDTO.class)).collect(Collectors.toList()));
        pageDTO.setTotalItems(userPage.getTotalElements());
        pageDTO.setItemsPerPage(userPage.getSize());
        pageDTO.setCurrentPage(userPage.getNumber());
        pageDTO.setTotalPages(userPage.getTotalPages());
        return pageDTO;
    }

    @Value("${user.default.sort.by}")
    public String DEFAULT_SORT_BY;
    @Value("${user.allowed.sort.by}")
    public String[] ALLOWED_SORT_BY;
    @Value("${user.default.descending.sort.order}")
    public Boolean DEFAULT_SORT_ORDER; //descending

    private final UserValidator userValidator;
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final UserSpecification userSpecification;
}
