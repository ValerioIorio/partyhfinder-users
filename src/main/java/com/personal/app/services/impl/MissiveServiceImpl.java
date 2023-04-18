package com.personal.app.services.impl;

import com.partyh.finder.common.exception.impl.PFIdNullException;
import com.partyh.finder.common.utils.FilterUtil;
import com.partyh.finder.common.validators.commons.ValidationUtils;
import com.personal.app.models.entities.Chat;
import com.personal.app.models.entities.Missive;
import com.personal.app.models.entities.User;
import com.personal.app.model.MissiveDTO;
import com.personal.app.model.MissivePageDTO;
import com.personal.app.models.filters.MissiveFilter;
import com.personal.app.repository.ChatRepository;
import com.personal.app.repository.MissiveRepository;
import com.personal.app.repository.UserRepository;
import com.personal.app.services.MissiveService;
import com.personal.app.specifications.MissiveSpecification;
import com.personal.app.validators.MissiveValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissiveServiceImpl implements MissiveService {
    @Override
    public MissiveDTO addMissive(MissiveDTO missiveDTO) {
        //check if the dto is null
        missiveValidator.validateId(missiveDTO.getId(), HttpMethod.POST);
        //check if the sender id and the chat id are not null
        if (!ValidationUtils.isNull(missiveDTO.getSender().getId()) && !ValidationUtils.isNull(missiveDTO.getChat().getId())) {
            Optional<User> senderOptional = userRepository.findById(missiveDTO.getSender().getId());
            Optional<Chat> chatOptional = chatRepository.findById(missiveDTO.getChat().getId());

            //validate
            missiveValidator.validate(
                    missiveDTO,
                    HttpMethod.POST,
                    new LinkedHashMap<>(
                            Map.of("sender", senderOptional.isPresent(), "chat", chatOptional.isPresent())
                    )
            );

            User sender = senderOptional.get();
            Chat chat = chatOptional.get();

            //set default values
            Missive missive = mapper.map(missiveDTO, Missive.class);
            missive.setSender(sender);
            missive.setChat(chat);
            missive.setCreatedAt(LocalDateTime.now());
            missive.setUpdatedAt(LocalDateTime.now());
            missive.setSentAt(LocalDateTime.now());
            missive.setReadAt(null);
            missive.setDeletedAt(null);
            missive.setRead(false);
            //save missive
            return mapper.map(this.missiveRepository.save(missive), MissiveDTO.class);
        } else {
            throw new PFIdNullException("Sender Id and Chat Id cannot be null");
        }

    }

    @Override
    public MissivePageDTO findAllMissive(MissiveFilter filter) {
        //set default values of sorting
        if(ValidationUtils.isBlank(filter.getSortBy())){
            filter.setSortBy(Missive.DEFAULT_SORT_BY);
        }
        //if the sender id is not null, then get the sender or receiver object, and if the result of the query is null, then set the sender to null
        if (!ValidationUtils.isNull(filter.getSenderId())){
            filter.setSender(userRepository.findById(filter.getSender().getId()).orElse(null));
        }
        //get the page of missive
        Page<Missive> missivePage = missiveRepository.findAll(
                missiveSpecification.buildSpecification(filter),
                PageRequest.of(FilterUtil.getNumberOfPage(filter.getNumberOfPage()), FilterUtil.getPageSize(filter.getPageSize()))
                        .withSort(FilterUtil.getDirection(filter.getDescending()), FilterUtil.getSortBy(filter.getSortBy())
                )
        );
        //map the page of missive to page of missiveDTO
        MissivePageDTO pageDTO = new MissivePageDTO();
        pageDTO.setItems(missivePage.stream().map(missive -> mapper.map(missive, MissiveDTO.class)).collect(Collectors.toList()));
        pageDTO.setTotalItems(missivePage.getTotalElements());
        pageDTO.setItemsPerPage(missivePage.getSize());
        pageDTO.setCurrentPage(missivePage.getNumber());
        pageDTO.setTotalPages(missivePage.getTotalPages());
        return pageDTO;
    }

    @Override
    public MissiveDTO getMissive(Long id) {
        Optional<Missive> missiveOptional = missiveRepository.findById(id);
        if(missiveOptional.isPresent()){
            return mapper.map(missiveOptional.get(), MissiveDTO.class);
        }else {
            throw new NoSuchElementException("Missive not found");
        }
    }

    @Override
    public MissiveDTO updateMissive(MissiveDTO missiveDTO) {
        //check if the dto id is not null (we don't check if the chat id and the sender id are not null because they are not changeable)
        missiveValidator.validateId(missiveDTO.getId(), HttpMethod.PUT);
        Optional<Missive> missiveOptional = missiveRepository.findById(missiveDTO.getId());
        //validate
        missiveValidator.validate(
                missiveDTO,
                HttpMethod.PUT,
                new LinkedHashMap<>(
                        Map.of("missive", missiveOptional.isPresent())
                )
        );
        //get the previous missive
        Missive previousMissive = missiveOptional.get();
        //map the new missive dto to an entity
        Missive missive = mapper.map(missiveDTO, Missive.class);
        //set not changeable values
        missive.setSender(previousMissive.getSender());
        missive.setChat(previousMissive.getChat());
        missive.setCreatedAt(previousMissive.getCreatedAt());
        missive.setSentAt(previousMissive.getSentAt());
        missive.setReadAt(previousMissive.getReadAt());
        missive.setDeletedAt(previousMissive.getDeletedAt());
        missive.setRead(previousMissive.isRead());
        //set default values
        missive.setUpdatedAt(LocalDateTime.now());
        //save missive
        return mapper.map(this.missiveRepository.save(missive), MissiveDTO.class);
    }

    @Override
    public void deleteMissive(Long id) {
        Optional<Missive> missiveOptional = missiveRepository.findById(id);
        if(missiveOptional.isPresent()){
            missiveRepository.delete(missiveOptional.get());
        }else {
            throw new NoSuchElementException("Missive not found");
        }
    }

    private final ModelMapper mapper;
    private final MissiveValidator missiveValidator;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MissiveRepository missiveRepository;
    private final MissiveSpecification missiveSpecification;
}
