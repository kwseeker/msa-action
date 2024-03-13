package top.kwseeker.msa.action.user.infrastructure.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import top.kwseeker.msa.action.user.domain.user.model.entity.UserEntity;
import top.kwseeker.msa.action.user.infrastructure.po.UserPO;

@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    @Mapping(target = "loginIp", ignore = true)
    @Mapping(target = "loginDate", ignore = true)
    UserPO convert(UserEntity userEntity);

    UserEntity convert(UserPO userPO);
}
