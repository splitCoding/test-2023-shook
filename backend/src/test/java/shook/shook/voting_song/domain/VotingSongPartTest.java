package shook.shook.voting_song.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shook.shook.part.domain.PartLength;
import shook.shook.part.exception.PartException;
import shook.shook.voting_song.exception.VoteException;

class VotingSongPartTest {

    private final VotingSong votingSong = new VotingSong("제목", "비디오URL", "이미지URL", "가수", 30);

    @DisplayName("Id가 같은 파트는 동등성 비교에 참을 반환한다.")
    @Test
    void equals_true() {
        //given
        final VotingSongPart firstPart = VotingSongPart.saved(1L, 4, PartLength.SHORT, votingSong);
        final VotingSongPart secondPart = VotingSongPart.saved(1L, 14, PartLength.SHORT,
            votingSong);

        //when
        final boolean equals = firstPart.equals(secondPart);

        //then
        assertThat(equals).isTrue();
    }

    @DisplayName("Id가 없는 경우 동등성 비교에 항상 거짓을 반환한다.")
    @Nested
    class Equals {

        @DisplayName("비교하는 객체 중 하나만 없을 때")
        @Test
        void equals_false_nullId() {
            //given
            final VotingSongPart firstPart =
                VotingSongPart.saved(null, 4, PartLength.SHORT, votingSong);
            final VotingSongPart secondPart =
                VotingSongPart.saved(1L, 14, PartLength.SHORT, votingSong);

            //when
            final boolean equals = firstPart.equals(secondPart);

            //then
            assertThat(equals).isFalse();
        }

        @DisplayName("비교하는 객체 둘다 없을 때")
        @Test
        void equals_false_bothNullId() {
            //given
            final VotingSongPart firstPart =
                VotingSongPart.saved(null, 4, PartLength.SHORT, votingSong);
            final VotingSongPart secondPart =
                VotingSongPart.saved(null, 14, PartLength.SHORT, votingSong);

            //when
            final boolean equals = firstPart.equals(secondPart);

            //then
            assertThat(equals).isFalse();
        }
    }

    @DisplayName("파트를 생성한다.")
    @Nested
    class Create {

        @DisplayName("성공한다.")
        @Test
        void create_success() {
            //given
            //when
            //then
            assertDoesNotThrow(() -> VotingSongPart.forSave(14, PartLength.SHORT, votingSong));
        }

        @DisplayName("파트의 시작초가 0보다 작으면 예외가 발생한다.")
        @Test
        void create_fail_startUnderZero() {
            //given
            //when
            //then
            assertThatThrownBy(() -> VotingSongPart.forSave(-1, PartLength.SHORT, votingSong))
                .isInstanceOf(PartException.StartLessThanZeroException.class);
        }

        @DisplayName("파트의 시작초가 길이보다 크거나 같으면 예외가 발생한다.")
        @Test
        void create_fail_startOverSongLength() {
            //given
            //when
            //then
            assertThatThrownBy(() -> VotingSongPart.forSave(30, PartLength.SHORT, votingSong))
                .isInstanceOf(PartException.StartOverSongLengthException.class);
        }

        @DisplayName("파트의 끝이 길이보다 크면 예외가 발생한다.")
        @Test
        void create_fail_endOverSongLength() {
            //given
            //when
            //then
            assertThatThrownBy(() -> VotingSongPart.forSave(29, PartLength.SHORT, votingSong))
                .isInstanceOf(PartException.EndOverSongLengthException.class);
        }
    }

    @DisplayName("파트에 투표한다.")
    @Nested
    class VoteToPart {

        @DisplayName("한 번 투표한다.")
        @Test
        void vote_success_one() {
            //given
            final VotingSongPart part = VotingSongPart.saved(1L, 14, PartLength.SHORT, votingSong);
            final Vote vote = Vote.saved(1L, part);

            //when
            part.vote(vote);

            //then
            assertThat(part.getVotes()).containsOnly(vote);
        }

        @DisplayName("여러 번 투표한다.")
        @Test
        void vote_success_many() {
            //given
            final VotingSongPart part = VotingSongPart.forSave(14, PartLength.SHORT, votingSong);
            final Vote firstVote = Vote.forSave(part);
            final Vote secondVote = Vote.forSave(part);

            //when
            part.vote(firstVote);
            part.vote(secondVote);

            //then
            assertThat(part.getVotes()).containsOnly(firstVote, secondVote);
        }

        @DisplayName("다른 파트의 투표로 투표하면 예외가 발생한다.")
        @Test
        void vote_fail_voteForOtherPart() {
            //given
            final VotingSongPart firstPart = VotingSongPart.saved(1L, 14, PartLength.SHORT,
                votingSong);
            final VotingSongPart secondPart = VotingSongPart.saved(2L, 10, PartLength.SHORT,
                votingSong);
            final Vote voteForSecondPart = Vote.forSave(secondPart);

            //when
            //then
            assertThatThrownBy(() -> firstPart.vote(voteForSecondPart))
                .isInstanceOf(VoteException.VoteForOtherPartException.class);
        }
    }

    @Nested
    @DisplayName("총 득표수를 반환한다.")
    class GetVoteCount {

        @DisplayName("Id 가 다른 두 개의 투표를 통해 투표했을 때 득표수가 2번 증가한다.")
        @Test
        void getVoteCount_twoVoteDifferentId() {
            //given
            final VotingSongPart part = VotingSongPart.forSave(14, PartLength.SHORT, votingSong);
            final Vote firstVote = Vote.saved(1L, part);
            final Vote secondVote = Vote.saved(2L, part);
            part.vote(firstVote);
            part.vote(secondVote);

            //when
            final int voteCount = part.getVoteCount();

            //then
            assertThat(voteCount).isEqualTo(2);
        }

        @DisplayName("Id 가 같은 두 개의 투표를 통해 투표했을 때 예외가 발생한다.")
        @Test
        void getVoteCount_towVoteSameId() {
            //given
            final VotingSongPart part = VotingSongPart.forSave(14, PartLength.SHORT, votingSong);
            final Vote firstVote = Vote.saved(1L, part);
            final Vote secondVote = Vote.saved(1L, part);
            part.vote(firstVote);

            //when
            //then
            assertThatThrownBy(() -> part.vote(secondVote))
                .isInstanceOf(VoteException.DuplicateVoteExistException.class);
        }
    }
}
