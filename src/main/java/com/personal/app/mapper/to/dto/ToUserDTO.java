package com.personal.app.mapper.to.dto;

import com.partyh.finder.common.config.mapper.CustomMapper;
import com.personal.app.model.UserDTO;
import com.personal.app.models.entities.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ToUserDTO implements CustomMapper<User, UserDTO>{

    @Override
    public void map(ModelMapper modelMapper) {
        Converter<List<User>, List<Long>> userToId = context -> context.getSource() == null ? null : context.getSource().stream().map(User::getId).collect(Collectors.toList());
        modelMapper.createTypeMap(User.class, UserDTO.class)
                .addMappings(mapper -> mapper.using(userToId).map(User::getMatchedUsers, UserDTO::setMatchedUsersIds));
    }


}
