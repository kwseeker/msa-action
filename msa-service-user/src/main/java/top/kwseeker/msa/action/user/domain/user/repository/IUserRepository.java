package top.kwseeker.msa.action.user.domain.user.repository;

import top.kwseeker.msa.action.user.domain.user.model.entity.UserEntity;

public interface IUserRepository {

    /**
     * 新增用户
     * @return 新用户ID
     */
    Long insert(UserEntity userEntity);

    UserEntity getUserById(Long uid);

    UserEntity getUserByUsername(String username);
}
