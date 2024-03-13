package top.kwseeker.msa.action.user.infrastructure.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import top.kwseeker.msa.action.user.domain.user.model.entity.UserEntity;
import top.kwseeker.msa.action.user.domain.user.repository.IUserRepository;
import top.kwseeker.msa.action.user.infrastructure.converter.UserConverter;
import top.kwseeker.msa.action.user.infrastructure.dao.UserMapper;
import top.kwseeker.msa.action.user.infrastructure.po.UserPO;
import top.kwseeker.msa.action.user.types.common.UserErrorCodes;
import top.kwseeker.msa.action.user.types.exception.UserDomainException;

@Slf4j
@Repository
public class UserRepository extends ServiceImpl<UserMapper, UserPO> implements IUserRepository {

    @Override
    public Long insert(UserEntity userEntity) {
        UserPO userPO = UserConverter.INSTANCE.convert(userEntity);
        boolean ret = save(userPO);
        if (!ret) {
            throw new UserDomainException(UserErrorCodes.CREATE_USER_FAILED);
        }
        return userPO.getId();
    }

    @Override
    public UserEntity getUserById(Long uid) {
        UserPO userPO = getById(uid);
        return UserConverter.INSTANCE.convert(userPO);
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        UserPO userPO = getBaseMapper().selectByUsername(username);
        return UserConverter.INSTANCE.convert(userPO);
    }
}
