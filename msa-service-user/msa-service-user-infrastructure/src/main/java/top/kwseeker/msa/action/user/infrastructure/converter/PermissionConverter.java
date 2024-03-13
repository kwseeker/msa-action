package top.kwseeker.msa.action.user.infrastructure.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.kwseeker.msa.action.user.domain.permission.model.entity.MenuEntity;
import top.kwseeker.msa.action.user.domain.permission.model.entity.RoleEntity;
import top.kwseeker.msa.action.user.infrastructure.po.MenuPO;
import top.kwseeker.msa.action.user.infrastructure.po.RolePO;

@Mapper
public interface PermissionConverter {

    PermissionConverter INSTANCE = Mappers.getMapper(PermissionConverter.class);

    MenuEntity convert(MenuPO menuPO);

    RoleEntity convert(RolePO rolePO);
}
