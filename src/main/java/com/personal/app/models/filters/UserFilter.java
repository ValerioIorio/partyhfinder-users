package com.personal.app.models.filters;

import com.partyh.finder.common.models.AbstractFilter;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFilter extends AbstractFilter {

    private String username;

    public UserFilter(Integer pageSize, Integer numberOfPage, Boolean descending, String sortBy, String username) {
        super(pageSize, numberOfPage, descending, sortBy);
        this.username = username;
    }
}
