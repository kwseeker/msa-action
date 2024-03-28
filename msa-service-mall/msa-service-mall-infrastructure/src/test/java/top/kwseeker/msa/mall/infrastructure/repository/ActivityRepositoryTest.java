package top.kwseeker.msa.mall.infrastructure.repository;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import top.kwseeker.msa.mall.infrastructure.dao.MockApplication;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

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
    }
}