package com.personal.app.specifications;

import com.partyh.finder.common.specifications.SpecificationFactory;
import com.personal.app.models.entities.User;
import com.personal.app.models.filters.UserFilter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserSpecification extends SpecificationFactory<User, UserFilter> {

    @Override
    protected List<Predicate> buildPredicates(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder, UserFilter filter) {
        return new ArrayList<>();

    }

    @Override
    protected Example<User> buildExample(UserFilter filter) {
        User user = User.builder()
                .username(filter.getUsername())
                .build();
        return Example.of(user, getExampleMatcherContainingStringAndIgnoreNull()); //getExampleMatcherContainingStringAndIgnoreNull potrebbe non funzionare con dei valori booleani dentro l'entita
    }



}
