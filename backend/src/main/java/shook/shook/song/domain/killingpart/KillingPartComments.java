package shook.shook.song.domain.killingpart;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shook.shook.song.exception.killingpart.KillingPartCommentException;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class KillingPartComments {

    @OneToMany(mappedBy = "killingPart")
    private final List<KillingPartComment> comments = new ArrayList<>();

    public void addComment(final KillingPartComment comment) {
        validateComment(comment);
        comments.add(comment);
    }

    private void validateComment(final KillingPartComment comment) {
        if (comments.contains(comment)) {
            throw new KillingPartCommentException.DuplicateCommentExistException();
        }
    }

    public List<KillingPartComment> getComments() {
        return new ArrayList<>(comments);
    }

    public List<KillingPartComment> getCommentsInRecentOrder() {
        return comments.stream()
            .sorted(Comparator.comparing(KillingPartComment::getCreatedAt).reversed())
            .toList();
    }
}
