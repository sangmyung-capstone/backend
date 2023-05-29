package capstone.bapool.party;

import capstone.bapool.model.PartyHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<PartyHashtag, Long> {
}
