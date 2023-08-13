package capstone.bapool.user;

import capstone.bapool.model.UserHashtag;
import capstone.bapool.user.dto.UserHashtagInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserHashtagRepository extends JpaRepository<UserHashtag, Long> {
    @Query(value = "select new capstone.bapool.user.dto.UserHashtagInfo(uhh.hashtagId, COUNT(uhh.hashtagId)) " +
            "from UserHashtag uhh " +
            "where uhh.user.id = :userId " +
            "group by uhh.hashtagId")
    List<UserHashtagInfo> findByUserId(@Param("userId") Long userId);
}
