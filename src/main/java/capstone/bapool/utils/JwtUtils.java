package capstone.bapool.utils;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.error.StatusEnum;
import capstone.bapool.user.dto.ReissueRes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static capstone.bapool.config.error.StatusEnum.EXPIRATION_TOKEN;


@Slf4j
@NoArgsConstructor
@Component
public class JwtUtils {

    public static final long ACCESS_TOKEN_VALID_TIME = 1000 * 60 * 60 * 12L;
    public static final long REFRESH_TOKEN_VALID_TIME = 1000 * 60 * 60 * 24 * 14 * 1L;
    @Value("${jwt.secret-key}")
    private String secretKey;


    public ReissueRes generateTokens(Long userId) {
        String accessToken = createAccessToken(userId);
        String refreshToken = createRefreshToken(userId);
        ReissueRes reissueRes = new ReissueRes(userId, accessToken, refreshToken);
        return reissueRes;
    }

    private String createAccessToken(Long userId) {
        return createJwt(userId, ACCESS_TOKEN_VALID_TIME * 100);
    }

    private String createRefreshToken(Long userId) {
        return createJwt(userId, REFRESH_TOKEN_VALID_TIME);
    }

    private String createJwt(Long userId, Long tokenValid) {
        byte[] keyBytes = Decoders.BASE64.decode(getSecretKey());
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .signWith(key)
                .claim("userId", userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValid))
                .compact();
    }

    public Long resolveRequest(HttpServletRequest request) throws BaseException {
        try {
            String accessToken = getAccessToken(request);
            return resolveToken(accessToken);
        } catch (ExpiredJwtException e) {
            throw new BaseException(EXPIRATION_TOKEN);
        } catch (BaseException e) {
            throw e;
        }
    }

    private String getAccessToken(HttpServletRequest request) throws BaseException {
        String accessToken = request.getHeader("ACCESS-TOKEN");

        validateAccessToken(accessToken);
        return accessToken;
    }

    private void validateAccessToken(String accessToken) {
        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(StatusEnum.EMPTY_ACCESS_KEY);
        }
    }

    private Long resolveToken(String accessToken) throws BaseException {
        return Optional.ofNullable(Jwts.parserBuilder()
                        .setSigningKey(getSecretKey())
                        .build()
                        .parseClaimsJws(accessToken)
                        .getBody())
                .map((c) -> c.get("userId", Long.class))
                .orElseThrow(() -> new BaseException(StatusEnum.EMPTY_ACCESS_KEY));
    }

    public boolean validateRequest(HttpServletRequest request) {
        String jwtToken = getAccessToken(request);
        return validateToken(jwtToken);
    }

    public void validate(String jwtToken) throws BaseException {
        if (!validateToken(jwtToken)) {
            throw new BaseException(EXPIRATION_TOKEN);
        }
    }

    private boolean validateToken(String jwtToken) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(jwtToken);
        return !claims.getBody().getExpiration().before(new Date());
    }

    private String getSecretKey() {
        String secretKeyEncodeBase64 = Encoders.BASE64.encode(secretKey.getBytes());
        return secretKeyEncodeBase64;
    }
}
