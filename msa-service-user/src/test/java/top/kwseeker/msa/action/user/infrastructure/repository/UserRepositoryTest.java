package top.kwseeker.msa.action.user.infrastructure.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.kwseeker.msa.action.user.domain.user.model.entity.UserEntity;
import top.kwseeker.msa.action.user.domain.user.repository.IUserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private IUserRepository userRepository;

    @Test
    public void testSelect() {
        UserEntity userEntity = userRepository.getUserByUsername("kwseeker");
        assertEquals(11L, userEntity.getId());
    }
}