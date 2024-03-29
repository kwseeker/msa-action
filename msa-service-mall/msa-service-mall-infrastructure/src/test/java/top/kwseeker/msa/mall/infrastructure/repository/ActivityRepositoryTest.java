package top.kwseeker.msa.mall.infrastructure.repository;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivitySetEntity;
import top.kwseeker.msa.mall.infrastructure.dao.MockApplication;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
import static top.kwseeker.msa.mall.infrastructure.repository.ActivityRepository.SettingKeyPrefix;

@SpringBootTest(classes = MockApplication.class)
@ContextConfiguration(classes = {RedisClientConfig.class})
//@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@TestPropertySource(properties = {
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.datasource.url=jdbc:mysql://localhost:3306/msa-action?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC",
        "spring.datasource.username=root",
        "spring.datasource.password=123456",
})
class ActivityRepositoryTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void testSetObjectValue() {
        String key = SettingKeyPrefix + "-1";
        String key2 = SettingKeyPrefix + "0";
        ActivitySetEntity object = redissonClient.<ActivitySetEntity>getBucket(key).get();
        ActivitySetEntity nullObject = redissonClient.<ActivitySetEntity>getBucket(key2).get();
        ActivitySetEntity activitySetEntity = ActivitySetEntity.builder()
                .activityId(-1)
                .name("测试-1")
                .itemId(1)
                .stock(100)
                .build();
        redissonClient.getBucket(key).set(activitySetEntity, Duration.ofSeconds(7200));
        object = redissonClient.<ActivitySetEntity>getBucket(key).get();
    }

    @Test
    public void testRedissonIncr() throws InterruptedException {
        String key = "test:redisson:incr";
        boolean delete = redissonClient.getAtomicLong(key).delete();
        assertTrue(delete);
        CountDownLatch latch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    long incr = redissonClient.getAtomicLong(key).incrementAndGet();
                    //System.out.println(incr);
                }
                latch.countDown();
            }).start();
        }
        latch.await();
        Long sum = redissonClient.getAtomicLong(key).get();
        System.out.println("sum=" + sum);
        assertEquals(1000000, sum);
    }

    @Test
    public void testChronoUnit() {
        LocalDateTime dateTime1 = LocalDateTime.of(2022, 1, 1, 12, 0, 0, 300000000);
        LocalDateTime dateTime2 = LocalDateTime.of(2022, 1, 1, 12, 0, 10, 400000000);
        long secondsBetween = ChronoUnit.SECONDS.between(dateTime1, dateTime2);
        long millisBetween = ChronoUnit.MILLIS.between(dateTime1, dateTime2);
        assertEquals(10, secondsBetween);
        assertEquals(10100, millisBetween);
        long until = dateTime1.until(dateTime2, ChronoUnit.MILLIS);
        assertEquals(10100, until);
    }
}