package shook.shook.voting_song.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class VotingSongPartRegisterRequest {

    @NotNull
    @PositiveOrZero
    private Integer startSecond;

    @NotNull
    @Positive
    private Integer length;
}
