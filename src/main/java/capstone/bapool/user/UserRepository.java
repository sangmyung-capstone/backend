package capstone.bapool.user;

import capstone.bapool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByRefreshToken(String refreshToken);

    boolean existsUserByName(String name);
}
