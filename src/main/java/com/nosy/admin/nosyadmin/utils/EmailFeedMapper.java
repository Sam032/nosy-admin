package com.nosy.admin.nosyadmin.utils;

import com.nosy.admin.nosyadmin.dto.EmailFeedDto;
import com.nosy.admin.nosyadmin.model.EmailFeed;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public abstract class EmailFeedMapper {

    public static final EmailFeedMapper INSTANCE = Mappers.getMapper(EmailFeedMapper.class);

    @Mapping(source = "emailFeedId", target = "id")
    @Mapping(source = "emailFeedName", target = "name")
    @Mapping(source = "emailFeedAddress", target = "address")
    @Mapping(source = "emailFeedSubscribers", target = "subscribers")
    public abstract EmailFeedDto toEmailFeedDto(EmailFeed emailFeed);

    @Mapping(source = "id", target = "emailFeedId")
    @Mapping(source = "name", target = "emailFeedName")
    @Mapping(source = "address", target = "emailFeedAddress")
    @Mapping(source = "subscribers", target = "emailFeedSubscribers")
    public abstract EmailFeed toEmailFeed(EmailFeedDto emailTemplateDto);

    public abstract List<EmailFeedDto> toEmailFeedDtoList(List<EmailFeed> emailFeedList);

}