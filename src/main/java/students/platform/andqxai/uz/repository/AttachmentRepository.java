package students.platform.andqxai.uz.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import students.platform.andqxai.uz.domain.Attachment;

/**
 * Spring Data JPA repository for the Attachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long>, JpaSpecificationExecutor<Attachment> {
    @Query("select a from Attachment a where a.name = ?1")
    Optional<Attachment> findAttachmentByName(String name);
}
