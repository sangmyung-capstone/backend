package capstone.bapool.user;

import capstone.bapool.model.BlockUser;
import capstone.bapool.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlockUserRepository extends JpaRepository<BlockUser, Long> {

    BlockUser findByBlockUserAndBlockedUser(User blockUser, User blockedUser);
}
