package com.nosy.admin.nosyadmin.utils;

import com.nosy.admin.nosyadmin.dto.EmailTemplateDto;
import com.nosy.admin.nosyadmin.model.EmailTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class EmailTemplateMapper {
    public static final EmailTemplateMapper INSTANCE = Mappers.getMapper( EmailTemplateMapper.class );
    @Mappings({
            @Mapping(source = "emailTemplateId", target = "id"),
            @Mapping(source = "emailTemplateName", target = "name"),
            @Mapping(source = "emailTemplateFromAddress", target = "fromAddress"),
            @Mapping(source = "emailTemplateTo", target = "to"),
            @Mapping(source = "emailTemplateCc", target = "cc"),
            @Mapping(source = "emailTemplateText", target = "text"),
            @Mapping(source="emailTemplateFromProvider", target = "fromProvider"),
            @Mapping(source="emailTemplatePriority", target="priority"),
            @Mapping(source="emailTemplateRetryTimes", target="retryTimes"),
            @Mapping(source="emailTemplateRetryPeriod", target="retryPeriod"),


    })
    public abstract EmailTemplateDto toEmailTemplateDto(EmailTemplate emailTemplate);

    @Mappings({
            @Mapping(source = "id", target = "emailTemplateId"),
            @Mapping(source = "name", target = "emailTemplateName"),
            @Mapping(source = "fromAddress", target = "emailTemplateFromAddress"),
            @Mapping(source = "to", target = "emailTemplateTo"),
            @Mapping(source = "cc", target = "emailTemplateCc"),
            @Mapping(source = "text", target = "emailTemplateText"),
            @Mapping(source="fromProvider", target = "emailTemplateFromProvider"),
            @Mapping(source="priority", target="emailTemplatePriority"),
            @Mapping(source="retryTimes", target="emailTemplateRetryTimes"),
            @Mapping(source="retryPeriod", target="emailTemplateRetryPeriod"),



    })
    public abstract EmailTemplate toEmailTemplate(EmailTemplateDto emailTemplateDto);
}
