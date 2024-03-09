package top.kwseeker.msa.action.user.infrastructure.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import top.kwseeker.msa.action.user.domain.user.model.entity.UserEntity;
import top.kwseeker.msa.action.user.domain.user.repository.IUserRepository;
import top.kwseeker.msa.action.user.infrastructure.converter.UserConverter;
import top.kwseeker.msa.action.user.infrastructure.dao.UserMapper;
import top.kwseeker.msa.action.user.infrastructure.po.UserPO;

@Slf4j
@Repository
public class UserRepository extends ServiceImpl<UserMapper, UserPO> implements IUserRepository {

    @Override
    public void insert(UserEntity userEntity) {
        //UserPO userPO = new UserPO();
        //userPO.setUsername(userEntity.getUsername());
        //userPO.setPassword(userEntity.getPassword());
        //userPO.setNickname(userEntity.getNickname());
        //userPO.setRemark(userEntity.getRemark());
        //userPO.setDeptId(userEntity.getDeptId());
        //userPO.setEmail(userEntity.getEmail());
        //userPO.setMobile(userEntity.getMobile());
        //userPO.setSex(userEntity.getSex());
        //userPO.setAvatar(userEntity.getAvatar());
        UserPO userPO = UserConverter.INSTANCE.convert(userEntity);
        int ret = getBaseMapper().insert(userPO);
        log.info("添加新用户：ret={}", ret);
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        UserPO userPO = getBaseMapper().selectByUsername(username);
        return UserConverter.INSTANCE.convert(userPO);
        //UserEntity userEntity = new UserEntity();
        //userEntity.setId(userPO.getId());
        //userEntity.setUsername(userPO.getUsername());
        //userEntity.setPassword(userPO.getPassword());
        //userEntity.setNickname(userPO.getNickname());
        //userEntity.setRemark(userPO.getRemark());
        //userEntity.setDeptId(userPO.getDeptId());
        //userEntity.setEmail(userPO.getEmail());
        //userEntity.setMobile(userPO.getMobile());
        //userEntity.setSex(userPO.getSex());
        //userEntity.setAvatar(userPO.getAvatar());
        //return userEntity;
    }
}
