package top.kwseeker.msa.action.user.types.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import com.auth0.jwt.HeaderParams;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import top.kwseeker.msa.action.user.types.common.UserErrorCodes;
import top.kwseeker.msa.action.user.types.exception.UserDomainException;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class JWTUtil {

    /**
     * RSA 密钥对文件生成：
     * openssl genpkey -algorithm RSA -out pri.pem -aes256
     * openssl rsa -pubout -in pri.pem -out pub.pem
     * 另外使用密钥创建 RSAPublicKey RSAPrivateKey 时使用 PEM 格式会报错，需要转成 DER 格式
     * openssl pkcs8 -topk8 -inform PEM -outform DER -in pri.pem -nocrypt > pri.der
     * openssl pkey -in pub.pem -pubin -outform der -out pub.der
     */
    private static final String pubKeyFile = "rsa/pub.der";
    private static final String priKeyFile = "rsa/pri.der";
    private static final int leeway = 60;  //60s
    private static final Algorithm algorithm;
    private static final JWTVerifier verifier;

    static {
        RSAPublicKey rsaPublicKey = loadRSAPublicKey(pubKeyFile);
        RSAPrivateKey rsaPrivateKey = loadRSAPrivateKey(priKeyFile);
        algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
        verifier = JWT.require(algorithm)
                .acceptExpiresAt(leeway)
                .build();
    }

    public static String createJWT(Map<String, Object> payloads, int expireSeconds) {
        Map<String, Object> headers = new HashMap<>();
        headers.put(HeaderParams.ALGORITHM, "RSA256");
        headers.put(HeaderParams.TYPE, "JWT");
        return createJWT(headers, payloads, expireSeconds);
    }

    public static String createJWT(Map<String, Object> headers, Map<String, Object> payloads, int expireSeconds) {
        String token;
        try {
            JWTCreator.Builder builder = JWT.create()
                    .withHeader(headers)
                    .withExpiresAt(new Date(System.currentTimeMillis() + expireSeconds * 1000L));
            if (payloads != null && !payloads.isEmpty()) {
                for (Map.Entry<String, Object> entry : payloads.entrySet()) {
                    if (entry.getValue() instanceof Boolean) {
                        builder.withClaim(entry.getKey(), (Boolean) entry.getValue());
                    } else if (entry.getValue() instanceof Integer) {
                        builder.withClaim(entry.getKey(), (Integer) entry.getValue());
                    } else if (entry.getValue() instanceof Double) {
                        builder.withClaim(entry.getKey(), (Double) entry.getValue());
                    } else if (entry.getValue() instanceof Long) {
                        builder.withClaim(entry.getKey(), (Long) entry.getValue());
                    } else if (entry.getValue() instanceof String) {
                        builder.withClaim(entry.getKey(), (String) entry.getValue());
                    } else if (entry.getValue() instanceof Date) {
                        builder.withClaim(entry.getKey(), (Date) entry.getValue());
                    } else if (entry.getValue() instanceof Instant) {
                        builder.withClaim(entry.getKey(), (Instant) entry.getValue());
                    } else if (entry.getValue() instanceof Map) {
                        builder.withClaim(entry.getKey(), (Map<String, ?>) entry.getValue());
                    } else if (entry.getValue() instanceof List<?>) {
                        builder.withClaim(entry.getKey(), (List<?>) entry.getValue());
                    } else {
                        throw new UserDomainException(UserErrorCodes.CREATE_TOKEN_FAILED.getCode(), "不支持的Claim类型");
                    }
                }
            }
            token = builder.sign(algorithm);
        } catch (JWTCreationException e) {
            throw new UserDomainException(UserErrorCodes.CREATE_TOKEN_FAILED, e);
        }
        return token;
    }

    public static DecodedJWT verifyJWT(String token) {
        try {
            //decodedJWT 是经过校验以及解密后的令牌数据
            //检查是否过期, 由于负载中加了exp，所以会自动校验过期时间
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new UserDomainException(UserErrorCodes.VERIFY_TOKEN_FAILED, e);
        }
    }

    private static RSAPublicKey loadRSAPublicKey(String publicKeyPath) {
        try (InputStream inputStream = JWTUtil.class.getClassLoader().getResourceAsStream(publicKeyPath)) {

            if (inputStream == null) {
                throw new RuntimeException("load rsa public key failed: " + publicKeyPath);
            }
            byte[] bytes = IoUtil.readBytes(inputStream);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static RSAPrivateKey loadRSAPrivateKey(String privateKeyPath) {
        try (InputStream inputStream = JWTUtil.class.getClassLoader().getResourceAsStream(privateKeyPath)) {
            if (inputStream == null) {
                throw new RuntimeException("load rsa private key failed: " + privateKeyPath);
            }
            byte[] bytes = IoUtil.readBytes(inputStream);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
