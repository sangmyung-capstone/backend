package capstone.bapool.party;

import capstone.bapool.entity.Party;
import capstone.bapool.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
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

    public List<Party> selectPartisInRestaurant(Restaurant restaurant){

        String query = "select p\n" +
                "from Party as p\n" +
                "where p.restaurant = :restaurant";

        return em.createQuery(query, Party.class)
                .setParameter("restaurant", restaurant)
                .getResultList();
    }
}
