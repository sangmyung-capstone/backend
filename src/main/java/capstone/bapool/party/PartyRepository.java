package capstone.bapool.party;

import capstone.bapool.model.Party;
import capstone.bapool.model.Restaurant;
import capstone.bapool.model.User;
import capstone.bapool.model.enumerate.PartyStatus;
import capstone.bapool.party.dto.PartyInfoSimple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {

    /**
     * 식당안의 파티개수 구하기
     * @param restaurant 식당
     * @return 식당안의 파티 개수
     */
    Long countByRestaurant(Restaurant restaurant);

    /**
     * 식당안의 파티리스트 조회
     * @param restaurant 식당
     * @return 해당 식당안의 파티리스트
     */
    List<Party> findByRestaurant(Restaurant restaurant);

//    @Query("SELECT p FROM Party p JOIN p.partyParticipants u WHERE p.partyStatus=:partyStatus")
    @Query(value = "select *\n" +
            "from party join party_participant pp\n" +
            "where party.party_id = pp.party_id\n" +
            "    and party_status=:partyStatus\n" +
            "    and pp.user_id=:userId", nativeQuery = true)
    Optional<List<Party>> findAtePartyByUser(@Param("userId") Long userId, @Param("partyStatus") String partyStatus);

    @Query(value = "select new capstone.bapool.party.dto.PartyInfoSimple(p.id, p.name, res.name, res.imgUrl, res.address, res.category)" +
            "from Party p" +
            " join PartyParticipant pp on pp.party = p" +
            " join Restaurant res on p.restaurant = res" +
            " and pp.user=:user")
    List<PartyInfoSimple> findTemp(@Param("user") User user);
}
