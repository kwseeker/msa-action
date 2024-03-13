package top.kwseeker.msa.action.user.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kwseeker.msa.action.user.infrastructure.mybatis.mapper.BaseMapperX;
import top.kwseeker.msa.action.user.infrastructure.po.UserPO;

@Mapper
public interface UserMapper extends BaseMapperX<UserPO> {

    default UserPO selectByUsername(String username) {
        return selectOne(UserPO::getUsername, username);
    }
}
