package capstone.bapool.party;

import capstone.bapool.model.Party;
import capstone.bapool.model.PartyHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartyHashtagRepository extends JpaRepository<PartyHashtag, Long> {
    @Query("delete from PartyHashtag ph where ph.party = :party")
    public void deleteAllByParty(@Param("party") Party party);
}
