package com.personal.app.validators;

import com.partyh.finder.common.validators.commons.AbstractValidator;
import com.personal.app.model.ChatDTO;
import jdk.jfr.Category;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
@Component
public class ChatValidator extends AbstractValidator<ChatDTO> {
    @Override
    public void validate(ChatDTO input, HttpMethod method, LinkedHashMap<String, Boolean> object_presence) {
        LinkedHashMap<String, String> errors = new LinkedHashMap<>();
        errors.putAll(validateMandatoryFields(errors, input.getName()));
        errors.putAll(validatePresenceOfOptionalObject(object_presence, errors));
        throwExceptionIfNecessary(errors);
    }

    @Override
    public void validate(ChatDTO input, HttpMethod method) {
        LinkedHashMap<String, String> errors = new LinkedHashMap<>();
        errors.putAll(validateMandatoryFields(errors, input.getName()));
        throwExceptionIfNecessary(errors);
    }
}
