package shook.shook.voting_song.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shook.shook.part.domain.PartLength;
import shook.shook.voting_song.application.dto.VotingSongPartRegisterRequest;
import shook.shook.voting_song.domain.Vote;
import shook.shook.voting_song.domain.VotingSong;
import shook.shook.voting_song.domain.VotingSongPart;
import shook.shook.voting_song.domain.repository.VoteRepository;
import shook.shook.voting_song.domain.repository.VotingSongPartRepository;
import shook.shook.voting_song.domain.repository.VotingSongRepository;
import shook.shook.voting_song.exception.VotingSongException.VotingSongNotExistException;
import shook.shook.voting_song.exception.VotingSongPartException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VotingSongPartService {

    private final VotingSongRepository votingSongRepository;
    private final VotingSongPartRepository votingSongPartRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public void register(
        final Long votingSongId,
        final VotingSongPartRegisterRequest request
    ) {
        final VotingSong votingSong = votingSongRepository.findById(votingSongId)
            .orElseThrow(VotingSongNotExistException::new);

        final int startSecond = request.getStartSecond();
        final PartLength partLength = PartLength.findBySecond(request.getLength());
        final VotingSongPart votingSongPart =
            VotingSongPart.forSave(startSecond, partLength, votingSong);

        if (votingSong.isUniquePart(votingSongPart)) {
            addPartAndVote(votingSong, votingSongPart);
            return;
        }

        voteToExistPart(votingSong, votingSongPart);
    }

    private void addPartAndVote(
        final VotingSong votingSong,
        final VotingSongPart votingSongPart
    ) {
        votingSong.addPart(votingSongPart);
        votingSongPartRepository.save(votingSongPart);

        voteToPart(votingSongPart);
    }

    private void voteToExistPart(
        final VotingSong votingSong,
        final VotingSongPart votingSongPart
    ) {
        final VotingSongPart existPart = votingSong.getSameLengthPartStartAt(votingSongPart)
            .orElseThrow(VotingSongPartException.PartNotExistException::new);

        voteToPart(existPart);
    }

    private void voteToPart(final VotingSongPart votingSongPart) {
        final Vote newVote = Vote.forSave(votingSongPart);
        votingSongPart.vote(newVote);
        voteRepository.save(newVote);
    }
}
