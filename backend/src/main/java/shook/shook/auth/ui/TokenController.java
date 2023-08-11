package shook.shook.auth.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import shook.shook.auth.application.AuthService;
import shook.shook.auth.application.dto.TokenReissueResponse;
import shook.shook.auth.exception.TokenException;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private final AuthService authService;

    @GetMapping("/token")
    public ResponseEntity<TokenReissueResponse> reissue(
        @CookieValue(value = "refreshToken", defaultValue = "none") final String refreshToken
    ) {
        if (refreshToken.equals("none")) {
            throw new TokenException.TokenNotFoundException();
        }
        final TokenReissueResponse response = authService.reissueToken(refreshToken);
        return ResponseEntity.ok(response);
    }
}
