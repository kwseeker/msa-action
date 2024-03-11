package top.kwseeker.msa.action.user.types.utils;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JWTUtilTest {

    @Test
    public void testCreateTokenAndVerify() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "RSA256");
        headers.put("typ", "JWT");
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("username", "kwseeker");
        String jwt = JWTUtil.createJWT(headers, payloads, 7200);

        DecodedJWT decodedJWT = JWTUtil.verifyJWT(jwt);

        assertEquals("kwseeker", decodedJWT.getClaims().get("username").asString());
    }

    @Test
    public void testDateUtil() {
        System.out.println(DateUtil.currentSeconds());
        System.out.println(System.currentTimeMillis() / 1000);
        System.out.println((DateUtil.currentSeconds() % (24 * 3600)) / 3600);
        System.out.println(DateUtil.date().toString("yyyy-MM-dd HH:mm:ss"));
    }
}