package shook.shook.song.domain.killingpart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shook.shook.song.domain.killingpart.KillingPartComment;

@Repository
public interface KillingPartCommentRepository extends JpaRepository<KillingPartComment, Long> {

}
