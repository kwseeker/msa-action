package top.kwseeker.msa.mall.trigger.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivityDrawEntity;
import top.kwseeker.msa.mall.trigger.model.dto.ActivityDrawDTO;

@Mapper
public interface Converter {

    Converter INSTANCE = Mappers.getMapper(Converter.class);

    @Mapping(target = "userId", ignore = true)
    ActivityDrawEntity convert(ActivityDrawDTO drawDTO);
}
