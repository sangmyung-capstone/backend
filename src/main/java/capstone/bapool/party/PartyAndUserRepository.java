package capstone.bapool.party;

import capstone.bapool.model.PartyAndUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyAndUserRepository extends JpaRepository<PartyAndUser, Long> {
}
