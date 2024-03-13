package top.kwseeker.msa.action.user.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kwseeker.msa.action.user.infrastructure.mybatis.mapper.BaseMapperX;
import top.kwseeker.msa.action.user.infrastructure.po.UserRolePO;

@Mapper
public interface UserRoleMapper extends BaseMapperX<UserRolePO> {
}