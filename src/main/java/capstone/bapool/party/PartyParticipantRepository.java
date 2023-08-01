package capstone.bapool.party;

import capstone.bapool.model.Party;
import capstone.bapool.model.PartyParticipant;
import capstone.bapool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyParticipantRepository extends JpaRepository<PartyParticipant, Long>, PartyParticipantCustomRepository {
    Optional<PartyParticipant> findPartyParticipantByUserAndParty(User user, Party party);

    boolean existsByUserAndParty(User user, Party party);

}
