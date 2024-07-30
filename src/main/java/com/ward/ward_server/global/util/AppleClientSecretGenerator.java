package com.ward.ward_server.global.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

public class AppleClientSecretGenerator {

    public static String generateClientSecret(String teamId, String clientId, String keyId, String privateKey) throws Exception {
        String privateKeyContent = privateKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\r?\\n", "")
                .replaceAll("\\s+", "");

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        System.out.println("Processed private key content: " + privateKeyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
        PrivateKey pk = keyFactory.generatePrivate(keySpec);

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + 1000 * 60 * 10; // 10 minutes expiration time
        Date exp = new Date(expMillis);
        return Jwts.builder()
                .setHeaderParam("kid", keyId)
                .setHeaderParam("alg", "ES256")
                .setIssuer(teamId)
                .setIssuedAt(now)
                .setExpiration(exp)
                .setAudience("https://appleid.apple.com")
                .setSubject(clientId)
                .signWith(pk, SignatureAlgorithm.ES256)
                .compact();
    }
}
