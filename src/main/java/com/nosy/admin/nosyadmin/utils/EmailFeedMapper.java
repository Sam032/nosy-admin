package com.nosy.admin.nosyadmin.utils;

import com.nosy.admin.nosyadmin.dto.EmailFeedDto;
import com.nosy.admin.nosyadmin.model.EmailFeed;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class EmailFeedMapper {

    public static final EmailFeedMapper INSTANCE = Mappers.getMapper(EmailFeedMapper.class);

    @Mapping(source = "emailFeedId", target = "id")
    @Mapping(source = "emailFeedName", target = "name")
    public abstract EmailFeedDto toEmailFeedDto(EmailFeed emailFeed);

    @Mapping(source = "id", target = "emailFeedId")
    @Mapping(source = "name", target = "emailFeedName")
    public abstract EmailFeed toEmailFeed(EmailFeedDto emailTemplateDto);

}