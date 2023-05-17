package capstone.bapool.party.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
public class PartyInfo {

    private Long partyId;
    private String partyName;
    private int participants;
    private int maxPeople;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String menu;
    private List<Integer> hastag;
    private String detail;
    private boolean hasBlockUser;
    private List<Integer> rating;

    @Builder
    public PartyInfo(Long partyId, String partyName, int participants, int maxPeople, LocalDateTime startDate, LocalDateTime endDate, String menu, List<Integer> hastag, String detail, boolean hasBlockUser, List<Integer> rating) {
        this.partyId = partyId;
        this.partyName = partyName;
        this.participants = participants;
        this.maxPeople = maxPeople;
        this.startDate = startDate;
        this.endDate = endDate;
        this.menu = menu;
        this.hastag = hastag;
        this.detail = detail;
        this.hasBlockUser = hasBlockUser;
        this.rating = rating;
    }
}
