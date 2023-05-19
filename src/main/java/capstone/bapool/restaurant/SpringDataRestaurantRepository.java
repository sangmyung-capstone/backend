package capstone.bapool.restaurant;

import capstone.bapool.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataRestaurantRepository extends JpaRepository<Restaurant, Long> {
}
