package capstone.bapool.restaurant;

import capstone.bapool.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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


}
