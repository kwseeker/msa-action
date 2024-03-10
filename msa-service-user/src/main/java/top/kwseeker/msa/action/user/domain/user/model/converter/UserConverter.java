package top.kwseeker.msa.action.user.domain.user.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import top.kwseeker.msa.action.user.domain.user.model.entity.UserEntity;
import top.kwseeker.msa.action.user.trigger.http.dto.UserCreateDTO;

@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    UserEntity convert(UserCreateDTO userCreateDTO);
}
