package capstone.bapool.user;

import capstone.bapool.model.Party;
import capstone.bapool.model.User;
import capstone.bapool.model.UserHashtag;
import capstone.bapool.model.enumerate.PartyStatus;
import capstone.bapool.party.dto.AtePartyInfo;
import capstone.bapool.user.dto.UserHashtagInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserHashtagRepository extends JpaRepository<UserHashtag, Long> {
//    @Query(value = "select uhh.hashtagId as hashtag_id, count(uhh.hashtagId) as count " +
//            "from UserHashtag uhh " +
//            "where uhh.user.id = :userId " +
//            "group by uhh.hashtagId")
    @Query(value = "select new capstone.bapool.user.dto.UserHashtagInfo(uhh.hashtagId, COUNT(uhh.hashtagId)) " +
            "from UserHashtag uhh " +
            "where uhh.user.id = :userId " +
            "group by uhh.hashtagId")
    /*@Query(value ="select new moon.odyssey.entity.YearReportSum(rp.year, SUM(rp.loanSmall), SUM (rp.loanMajor), SUM (rp.loanTotal))" +
        "from Report rp " +
        "group by rp.year"
    )*/
    List<UserHashtagInfo> findByUserId(@Param("userId") Long userId);

//    @Query(value = "select new capstone.bapool.party.dto.AtePartyInfo
//              (p.id, p.name, res.name, res.imgUrl, res.address, res.category, pp.ratingComplete)\n" +
//            "from Party p\n" +
//            "join fetch PartyParticipant pp on pp.party = p\n" +
//            "join fetch Restaurant res on p.restaurant = res\n" +
//            "where p.partyStatus =:partyStatus\n" +
//            "and pp.user=:user")
//    List<AtePartyInfo> findByUserAndPartyStatus(@Param("user") User user, @Param("partyStatus") PartyStatus partyStatus);
}
