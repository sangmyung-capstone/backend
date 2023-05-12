package capstone.bapool.party;

import capstone.bapool.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class PartyRepository {
    private EntityManager em;

    @Autowired
    public PartyRepository(EntityManager em){
        this.em = em;
    }

    public int countParty(Restaurant restaurant){

//        TypedQuery<Integer> query = em.createQuery("select count(*) from Party as p where p.restaurant = :restaurant",Integer.class)
//                .setParameter("restaurant", restaurant);
//        return query.getSingleResult();

        return em.createQuery("select count(*) from Party as p where p.restaurant = :restaurant",Integer.class)
                .setParameter("restaurant", restaurant)
                .getSingleResult();
    }
}
