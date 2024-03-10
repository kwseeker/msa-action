package top.kwseeker.msa.action.user.domain.user.service;

import top.kwseeker.msa.action.user.domain.user.model.entity.UserEntity;
import top.kwseeker.msa.action.user.trigger.http.dto.UserCreateDTO;

/**
 * 用户管理服务
 */
public interface IUserService {

    UserEntity getUser(Long uid);

    UserEntity getUserByUsername(String username);

    /**
     * 创建用户
     * @param userCreateDTO 新用户信息
     * @return 新建用户的ID
     */
    Long createUser(UserCreateDTO userCreateDTO);
}
