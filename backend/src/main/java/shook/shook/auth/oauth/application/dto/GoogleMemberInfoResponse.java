package shook.shook.auth.oauth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GoogleMemberInfoResponse {

    private String email;

    @JsonProperty("verified_email")
    private boolean verifiedEmail;

}
