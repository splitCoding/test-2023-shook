package shook.shook.auth.jwt.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shook.shook.auth.jwt.exception.TokenException;

class TokenProviderTest {

    private static final long ACCESS_TOKEN_VALID_TIME = 12000L;
    private static final long REFRESH_TOKEN_VALID_TIME = 6048000L;
    private static final String SECRET_CODE = "2345asdfasdfsadfsdf243dfdsfsfs";
    private TokenProvider tokenProvider;

    @BeforeEach
    public void setUp() {
        tokenProvider = new TokenProvider(ACCESS_TOKEN_VALID_TIME,
            REFRESH_TOKEN_VALID_TIME,
            SECRET_CODE);
    }

    @DisplayName("올바른 access token을 생성한다.")
    @Test
    void createAccessToken() {
        //given
        //when
        final String accessToken = tokenProvider.createAccessToken(1L);
        final Claims result = tokenProvider.parseClaims(accessToken);

        //then
        assertThat(result.get("memberId")).isEqualTo(1);
        assertThat(result.getExpiration().getTime() - result.getIssuedAt().getTime())
            .isEqualTo(ACCESS_TOKEN_VALID_TIME);
    }

    @DisplayName("올바른 refresh token을 생성한다.")
    @Test
    void createRefreshToken() {
        //given
        // when
        final String refreshToken = tokenProvider.createRefreshToken(1L);
        final Claims result = tokenProvider.parseClaims(refreshToken);

        // then
        assertThat(result.get("memberId")).isEqualTo(1);
        assertThat(result.getExpiration().getTime() - result.getIssuedAt().getTime())
            .isEqualTo(REFRESH_TOKEN_VALID_TIME);
    }

    @DisplayName("잘못 만들어진 token을 parsing하면 에러를 발생한다.")
    @Test
    void parsing_fail_malformed_token() {
        // given
        final String inValidToken = "asdfsev.asefsbd.23dfvs";

        //when
        //then
        assertThatThrownBy(() -> tokenProvider.parseClaims(inValidToken))
            .isInstanceOf(TokenException.NotIssuedTokenException.class);
    }

    @DisplayName("기한이 만료된 token을 parsing하면 에러를 발생한다.")
    @Test
    void parsing_fail_expired_token() {
        // given
        tokenProvider = new TokenProvider(0, 0, SECRET_CODE);
        final String expiredToken = tokenProvider.createAccessToken(1L);

        //when
        //then
        assertThatThrownBy(() -> tokenProvider.parseClaims(expiredToken))
            .isInstanceOf(TokenException.ExpiredTokenException.class);
    }
}
