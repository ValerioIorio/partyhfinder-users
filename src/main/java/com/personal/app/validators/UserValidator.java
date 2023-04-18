package com.personal.app.validators;

import com.partyh.finder.common.validators.commons.AbstractValidator;
import com.personal.app.model.UserDTO;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class UserValidator extends AbstractValidator<UserDTO> {

    @Override
    public void validate(UserDTO input, HttpMethod method, LinkedHashMap<String, Boolean> object_presence) {

    }

    @Override
    public void validate(UserDTO input, HttpMethod method) {
        LinkedHashMap<String, String> errors = new LinkedHashMap<>();
        errors.putAll(validateId(input.getId(), method, errors));

        if(method.equals(HttpMethod.POST)){
            errors.putAll(validateMandatoryFields(errors, input.getFirstName(), input.getLastName(), input.getEmail(), input.getUsername(), input.getAddress(), input.getPhoneNumber(), input.getGender().toString()));
        }else if(method.equals(HttpMethod.PUT)){
            errors.putAll(validateMandatoryFields(errors, input.getFirstName(), input.getLastName(), input.getUsername(), input.getAddress(), input.getGender().toString()));
        }

        errors.putAll(validateEmail(input.getEmail(), errors));
        errors.putAll(validateFieldEqualsToSpecificValues(errors, "gender", input.getGender(), 0, 1, 2));
        throwExceptionIfNecessary(errors);
    }
}
