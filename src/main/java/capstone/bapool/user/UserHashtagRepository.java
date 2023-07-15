package capstone.bapool.user;

import capstone.bapool.model.User;
import capstone.bapool.model.UserHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserHashtagRepository extends JpaRepository<UserHashtag, Long> {

    List<UserHashtag> findByUserId(Long userId);
}
