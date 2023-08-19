package capstone.bapool.model;


import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.error.StatusEnum;
import capstone.bapool.model.enumerate.PartyStatus;
import lombok.Builder;
import org.hibernate.annotations.ColumnDefault;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    private String menu;

    private String detail;

    @OneToMany(mappedBy = "party", cascade = CascadeType.REMOVE)
    private List<PartyParticipant> partyParticipants = new ArrayList<PartyParticipant>();

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartyHashtag> partyHashtags = new ArrayList<>();


    @Builder
    public Party(Restaurant restaurant, PartyStatus partyStatus, String name, int maxPeople, LocalDateTime startDate, String menu, String detail) {
        this.restaurant = restaurant;
        this.partyStatus = partyStatus;
        this.name = name;
        this.maxPeople = maxPeople;
        this.startDate = startDate;
        this.menu = menu;
        this.detail = detail;
    }

    public void update(String name, Integer maxPeople, LocalDateTime startDate,
                       String menu, String detail) {
        if (partyStatus != PartyStatus.RECRUITING) {
            throw new BaseException(StatusEnum.PARTY_STATUS_IS_NOT_RECRUITING);
        }
        this.name = name;
        this.maxPeople = maxPeople;
        this.startDate = startDate;
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
    public Double getPartyParticipantAvgRating(){
        double sum = 0;
        for(PartyParticipant partyParticipant : partyParticipants){
            User user = partyParticipant.getUser();
            sum += user.getRating();
        }

        return sum / partyParticipants.size();
    }

    // 모집중인지
    public Boolean is_recruiting(){
        if(PartyStatus.RECRUITING.equals(partyStatus)){
            return true;
        }
        else {
            return false;
        }
    }

    public void removeHashtags() {
        this.partyHashtags.clear();
    }

    public void done() {
        partyStatus = PartyStatus.DONE;
    }
}
