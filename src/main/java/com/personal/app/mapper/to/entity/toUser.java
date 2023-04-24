package com.personal.app.mapper.to.entity;

import com.partyh.finder.common.config.mapper.CustomMapper;
import com.personal.app.model.UserDTO;
import com.personal.app.models.entities.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class toUser implements CustomMapper<UserDTO, User> {

    //when mapping a userDTO to a user, will ignore the matchedUsersIds field, and set the matchedUsers field an empty list
    //because in the creation of a user, the matchedUsers field is not set
    //and in the update of a user, the matchedUsers field are set with the previous matchedUsers field
    //for add a matched user, will be used the dedicated endpoint

    @Override
    public void map(ModelMapper modelMapper) {
        //converter that take in input a list of ids and return an empty list of users
        Converter<List<Long>, List<User>> idToUser = context ->
                new ArrayList<>();
        modelMapper.createTypeMap(UserDTO.class, User.class)
                .addMappings(mapper -> mapper.using(idToUser).map(UserDTO::getMatchedUsersIds, User::setMatchedUsers));
    }
}
