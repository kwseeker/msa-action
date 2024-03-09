package top.kwseeker.msa.action.user.domain.user.repository;

import top.kwseeker.msa.action.user.domain.user.model.entity.UserEntity;

public interface IUserRepository {

    void insert(UserEntity userEntity);

    UserEntity getUserByUsername(String username);
}
