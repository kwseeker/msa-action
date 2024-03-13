package top.kwseeker.msa.action.user.domain.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.kwseeker.msa.action.user.domain.auth.model.vo.UserCreateVO;
import top.kwseeker.msa.action.user.domain.user.model.converter.UserConverter;
import top.kwseeker.msa.action.user.domain.user.model.entity.UserEntity;
import top.kwseeker.msa.action.user.domain.user.repository.IUserRepository;

import javax.annotation.Resource;

@Slf4j
@Service
public class UserService implements IUserService {

    @Resource
    private IUserRepository userRepository;

    @Override
    public UserEntity getUser(Long uid) {
        return userRepository.getUserById(uid);
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public Long createUser(UserCreateVO userCreateVO) {
        // 校验用户名、手机号、Email是否唯一、部门是否有效等
        // 不是此项目的重点，先忽略

        // 插入用户
        UserEntity userEntity = UserConverter.INSTANCE.convert(userCreateVO);
        return userRepository.insert(userEntity);
    }
}
