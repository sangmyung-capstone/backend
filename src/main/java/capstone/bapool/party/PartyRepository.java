package capstone.bapool.party;

import capstone.bapool.model.Party;
import capstone.bapool.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {

    /**
     * 식당안의 파티개수 구하기
     * @param restaurant 식당
     * @return 식당안의 파티 개수
     */
    Long countByRestaurant(Restaurant restaurant);
}
