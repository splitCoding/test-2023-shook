package shook.shook.song.application.killingpart.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import shook.shook.song.domain.killingpart.KillingPartComment;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class KillingPartCommentResponse {

    private final Long id;
    private final String content;
    private final LocalDateTime createdAt;

    public static KillingPartCommentResponse from(final KillingPartComment partComment) {
        return new KillingPartCommentResponse(
            partComment.getId(),
            partComment.getContent(),
            partComment.getCreatedAt()
        );
    }

    public static List<KillingPartCommentResponse> getList(
        final List<KillingPartComment> partComments) {
        return partComments.stream()
            .map(KillingPartCommentResponse::from)
            .toList();
    }
}
