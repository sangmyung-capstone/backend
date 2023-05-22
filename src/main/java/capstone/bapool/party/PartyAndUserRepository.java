package capstone.bapool.party;

import capstone.bapool.model.Party;
import capstone.bapool.model.PartyParticipant;
import capstone.bapool.model.User;
import capstone.bapool.model.enumerate.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PartyAndUserRepository extends JpaRepository<PartyParticipant, Long>, PartyParticipantCustomRepository {
    Optional<PartyParticipant> findPartyParticipantByUserAndParty(User user, Party party);

    boolean existsByUserAndParty(User user, Party party);



}
