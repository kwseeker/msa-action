package top.kwseeker.msa.action.user.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kwseeker.msa.action.user.infrastructure.mybatis.mapper.BaseMapperX;
import top.kwseeker.msa.action.user.infrastructure.po.RoleMenuPO;

@Mapper
public interface RoleMenuMapper extends BaseMapperX<RoleMenuPO> {
}