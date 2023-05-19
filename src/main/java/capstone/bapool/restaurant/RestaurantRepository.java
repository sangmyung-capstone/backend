package capstone.bapool.restaurant;

import capstone.bapool.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class RestaurantRepository {

    private EntityManager em;

    @Autowired
    public RestaurantRepository(EntityManager em){
        this.em = em;
    }

    public void save(Restaurant restaurant){
        em.persist(restaurant);
    }

    public Restaurant findOne(Long restaurantId){
        return em.find(Restaurant.class, restaurantId);
    }
}
