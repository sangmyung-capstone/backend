package capstone.bapool.party;

import capstone.bapool.model.PartyParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyAndUserRepository extends JpaRepository<PartyParticipant, Long> {
}
