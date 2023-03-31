package capstone.bapool.user;

import capstone.bapool.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByRefreshToken(String refreshToken);

    Optional<User> findUserByName(String name);
}
