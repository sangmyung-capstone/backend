package capstone.bapool.firebase.dto;


import capstone.bapool.party.dto.PostPartyReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FireBasePartyInfo {
    private String groupName;
    private String groupMenu;
    private String groupDetail;
    private Integer curNumberOfPeople;
    private Integer maxNumberOfPeople;
    private String startDate;
    private String endDate;
    private List<Integer> hashTag;

    public FireBasePartyInfo(PostPartyReq partyReq) {
        this.groupName = partyReq.getPartyName();
        this.groupDetail = partyReq.getDetail();
        this.curNumberOfPeople = 1;
        this.maxNumberOfPeople = partyReq.getMaxPeople();
        this.startDate = partyReq.getStartDate().toString();
        this.endDate = partyReq.getEndDate().toString();
        this.hashTag = partyReq.getHashtag();
    }
}
