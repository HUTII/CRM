package org.example.crm;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.Resource;
import org.example.crm.utils.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class CrmApplicationTests {
    @Resource
    JwtTokenUtil jwtTokenUtil;

    @Test
    void contextLoads() {
    }

    @Test
    public void generateToken() {
        // JWT头部分信息【Header】
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        // 载核【Payload】
        Map<String, Object> payload = new HashMap<>();
        payload.put("sub", "1234567890");
        payload.put("name","John Doe");
        payload.put("admin",true);

        // 声明Token失效时间
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND,300);// 300s

        // 生成Token
        String token = Jwts.builder()
                .setHeader(header)// 设置Header
                .setClaims(payload) // 设置载核
                .setExpiration(instance.getTime())// 设置生效时间
                .signWith(SignatureAlgorithm.HS256,"secret") // 签名,这里采用私钥进行签名,不要泄露了自己的私钥信息
                .compact(); // 压缩生成xxx.xxx.xxx

        System.out.println(token);
    }

    @Test
    public void getInfoByJwt() {
        // 生成的token
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImV4cCI6MTczMTIyNjc1NX0.DFtI16ZTJdVKKC_DxTJbiah_4LYBTrzt-ymhArS6yhg";
        // 解析head信息
        JwsHeader<?> jwsHeader = Jwts
                .parser()
                .setSigningKey("secret")
                .parseClaimsJws(token)
                .getHeader();

        System.out.println(jwsHeader); // {typ=JWT, alg=HS256}
        System.out.println("typ:"+jwsHeader.get("typ"));

        // 解析Payload
        Claims claims =    Jwts
                .parser()
                .setSigningKey("secret")
                .parseClaimsJws(token)
                .getBody();
        System.out.println(claims);// {sub=1234567890, name=John Doe, admin=true, exp=1663297431}
        System.out.println("admin:"+claims.get("admin"));

        // 解析Signature
        String signature =    Jwts
                .parser()
                .setSigningKey("secret")
                .parseClaimsJws(token)
                .getSignature();
        System.out.println(signature); // Ju5EzKBpUnuIRhDG1SU0NwMGsd9Jl_8YBcMM6PB2C20
    }

    @Test
    public void generateTokenByUsername() {
        String username = "admin";
        String token = jwtTokenUtil.generateToken(username);
        System.out.println(token);
    }

    @Test
    public void getUsernameFromToken() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImNyZWF0ZWQiOjE3MzEyMjk4MDM5MTYsImV4cCI6MTczMTgzNDYwM30.7OeC1o8ubIMUOy88kToV9Os4_XaeAaL9V8pG47Rb6BQxNSh2R9Px8mcsgSLMekhsPiokz-m-mNZ6SzR6cxAwrQ";
        String username = jwtTokenUtil.getUsernameFromToken(token);
        System.out.println(username);
    }

    @Test
    public void tokenIsValid() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImNyZWF0ZWQiOjE3MzEyMjk4MDM5MTYsImV4cCI6MTczMTgzNDYwM30.7OeC1o8ubIMUOy88kToV9Os4_XaeAaL9V8pG47Rb6BQxNSh2R9Px8mcsgSLMekhsPiokz-m-mNZ6SzR6cxAwrQ";
        boolean isValid = jwtTokenUtil.validateToken(token);
        System.out.println(isValid);
    }
}
