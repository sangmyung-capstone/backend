package capstone.bapool.model;


import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.error.StatusEnum;
import capstone.bapool.model.enumerate.PartyStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.hibernate.annotations.ColumnDefault;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Party extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Enumerated(value = EnumType.STRING)
    private PartyStatus partyStatus;

    private String name;

    @ColumnDefault("0")
    @Column(name = "max_people")
    private int maxPeople;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    private String menu;

    private String detail;

    @OneToMany(mappedBy = "party")
    private List<PartyParticipant> partyParticipants = new ArrayList<PartyParticipant>();

    @OneToMany(mappedBy = "party")
    private List<PartyHashtag> partyHashtags = new ArrayList<>();

    // 게터 사용 제한
    private List<PartyHashtag> getPartyHashtags() {
        return partyHashtags;
    }

    @Builder
    public Party(Restaurant restaurant, PartyStatus partyStatus, String name, int maxPeople, LocalDateTime startDate, LocalDateTime endDate, String menu, String detail) {
        this.restaurant = restaurant;
        this.partyStatus = partyStatus;
        this.name = name;
        this.maxPeople = maxPeople;
        this.startDate = startDate;
        this.endDate = endDate;
        this.menu = menu;
        this.detail = detail;
    }

    public void update(String name, Integer maxPeople, LocalDateTime startDate,
                       LocalDateTime endDate, String menu, String detail) {
        if (partyStatus != PartyStatus.RECRUITING) {
            throw new BaseException(StatusEnum.PARTY_STATUS_IS_NOT_RECRUITING);
        }
        this.name = name;
        this.maxPeople = maxPeople;
        this.startDate = startDate;
        this.endDate = endDate;
        this.menu = menu;
        this.detail = detail;
    }

    public boolean isLastMember() {
        if (partyParticipants.size() == 1) {
            return true;
        }
        return false;
    }

    public void close() {
        partyStatus = PartyStatus.DEADLINE;
    }

    public int getCurPartyMember() {
        return partyParticipants.size();
    }

    // 파티에 참여한 유저인지 확인
    public boolean isMeParticipate(User user){
        for(PartyParticipant partyParticipant : partyParticipants){
            if(partyParticipant.getUser() == user){
                return true;
            }
        }

        return false;
    }

    // 차단한 유저가 있는지 확인
    public boolean hasBlockUser(User user){
        for(PartyParticipant partyParticipant : partyParticipants){
            for(BlockUser blockUser : user.getBlockUsers()){
                if(partyParticipant.getUser() == blockUser.getBlockedUser()){
                    return true;
                }
            }
        }

        return false;
    }

    // 파티 해시태그 조회
    public List<Integer> getPartyHashtag(){
        List<Integer> partyHashtag = new ArrayList<>();
        for(PartyHashtag hashtag : partyHashtags){
            partyHashtag.add(hashtag.getHashtagId());
        }

        return partyHashtag;
    }

    // 파티에 참여한 유저 평점 조회
    public List<Double> getPartyParticipantRating(){
        List<Double> rating = new ArrayList<>();
        for(PartyParticipant partyParticipant : partyParticipants){
            User user = partyParticipant.getUser();
            rating.add(user.getRating());
        }

        return rating;
    }
}
