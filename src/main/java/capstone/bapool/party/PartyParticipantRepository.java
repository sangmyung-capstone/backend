package capstone.bapool.party;

import capstone.bapool.model.Party;
import capstone.bapool.model.PartyParticipant;
import capstone.bapool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PartyParticipantRepository extends JpaRepository<PartyParticipant, Long>, PartyParticipantCustomRepository {
    Optional<PartyParticipant> findPartyParticipantByUserAndParty(User user, Party party);

    boolean existsByUserAndParty(User user, Party party);

    @Query("select pp " +
            "from PartyParticipant pp " +
            "join fetch pp.party p " +
            "where pp.user = :user")
    List<PartyParticipant> findPartyParticipantByUser(@Param("user") User user);

}
