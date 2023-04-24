package com.personal.app.validators;

import com.partyh.finder.common.validators.commons.AbstractValidator;
import com.personal.app.model.UserDTO;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@Component
public class UserValidator extends AbstractValidator<UserDTO> {

    @Override
    public void validate(UserDTO input, HttpMethod method, LinkedHashMap<String, Boolean> object_presence) {
        LinkedHashMap<String, String> errors = new LinkedHashMap<>();
        errors.putAll(validateId(input.getId(), method, errors));
        validatePresenceOfOptionalObject(object_presence, errors);
        errors.putAll(validateMandatoryFields(errors, List.of("first name" , "last name", "username", "address", "gender"),  input.getFirstName(), input.getLastName(), input.getUsername(), input.getAddress(), input.getGender().toString()));
        errors.putAll(validateEmail(input.getEmail(), errors));
        errors.putAll(validateFieldEqualsToSpecificValues(errors, "gender", input.getGender(), 0, 1, 2));
        throwExceptionIfNecessary(errors);
    }

    @Override
    public void validate(UserDTO input, HttpMethod method) {
        LinkedHashMap<String, String> errors = new LinkedHashMap<>();
        errors.putAll(validateId(input.getId(), method, errors));
        errors.putAll(validateMandatoryFields(errors,  List.of("first name", "last name", "email" , "username" , "address", "phone number", "gender"), input.getFirstName(), input.getLastName(), input.getEmail(), input.getUsername(), input.getAddress(), input.getPhoneNumber(), input.getGender().toString()));
        errors.putAll(validateEmail(input.getEmail(), errors));
        errors.putAll(validateFieldEqualsToSpecificValues(errors, "gender", input.getGender(), 0, 1, 2));
        throwExceptionIfNecessary(errors);
    }
}
