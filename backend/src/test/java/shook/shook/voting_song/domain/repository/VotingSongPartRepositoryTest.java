package shook.shook.voting_song.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import shook.shook.part.domain.PartLength;
import shook.shook.support.UsingJpaTest;
import shook.shook.voting_song.domain.VotingSong;
import shook.shook.voting_song.domain.VotingSongPart;

class VotingSongPartRepositoryTest extends UsingJpaTest {

    private static VotingSong SAVED_SONG;

    @Autowired
    private VotingSongPartRepository votingSongPartRepository;

    @Autowired
    private VotingSongRepository votingSongRepository;

    @BeforeEach
    void setUp() {
        SAVED_SONG = votingSongRepository.save(new VotingSong("제목", "비디오URL", "이미지URL", "가수", 30));
    }

    @DisplayName("VotingSongPart 를 저장한다.")
    @Test
    void save() {
        //given
        final VotingSongPart votingSongPart =
            VotingSongPart.forSave(14, PartLength.SHORT, SAVED_SONG);

        //when
        final VotingSongPart saved = votingSongPartRepository.save(votingSongPart);

        //then
        assertThat(votingSongPart).isSameAs(saved);
        assertThat(votingSongPart.getId()).isNotNull();
    }

    @DisplayName("VotingSongPart 을 저장할 때의 시간 정보로 createAt이 자동 생성된다.")
    @Test
    void createdAt() {
        //given
        final VotingSongPart votingSongPart =
            VotingSongPart.forSave(14, PartLength.SHORT, SAVED_SONG);

        //when
        final LocalDateTime prev = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        final VotingSongPart saved = votingSongPartRepository.save(votingSongPart);
        final LocalDateTime after = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

        //then
        assertThat(votingSongPart).isSameAs(saved);
        assertThat(votingSongPart.getCreatedAt()).isBetween(prev, after);
    }

    @DisplayName("파트 수집 중인 노래의 모든 등록된 파트를 조회한다.")
    @Test
    void findAllBySong() {
        //given
        final VotingSongPart firstPart = VotingSongPart.forSave(1, PartLength.SHORT, SAVED_SONG);
        final VotingSongPart secondPart = VotingSongPart.forSave(5, PartLength.SHORT, SAVED_SONG);
        votingSongPartRepository.save(firstPart);
        votingSongPartRepository.save(secondPart);

        //when
        saveAndClearEntityManager();
        final List<VotingSongPart> allBySong =
            votingSongPartRepository.findAllByVotingSong(SAVED_SONG);

        //then
        assertThat(allBySong).containsAll(List.of(firstPart, secondPart));
    }

    @DisplayName("특정 파트를 조회한다.")
    @Nested
    class findById {

        @DisplayName("id로 파트를 조회한다.")
        @Test
        void findOnePart() {
            // given
            final VotingSongPart part = VotingSongPart.forSave(1, PartLength.SHORT, SAVED_SONG);
            votingSongPartRepository.save(part);

            // when
            final VotingSongPart saved = votingSongPartRepository.findById(part.getId()).get();

            // then
            assertThat(saved).isSameAs(part);
            assertThat(saved.getCreatedAt()).isNotNull();
        }

        @DisplayName("파트가 없으면 빈 Optional을 반환한다.")
        @Test
        void findNotExistedPart() {
            // given
            // when
            // then
            assertThat(votingSongPartRepository.findById(1L)).isEmpty();
        }
    }
}
