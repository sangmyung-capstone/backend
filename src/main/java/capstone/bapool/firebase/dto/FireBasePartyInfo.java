package capstone.bapool.firebase.dto;


import capstone.bapool.party.dto.PatchPartyReq;
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
    private String siteUrls;
    private String restaurantName;
    private Long groupLeaderId;

    public FireBasePartyInfo(PostPartyReq partyReq, Long userId) {
        this.groupName = partyReq.getPartyName();
        this.groupDetail = partyReq.getDetail();
        this.curNumberOfPeople = 1;
        this.maxNumberOfPeople = partyReq.getMaxPeople();
        this.startDate = partyReq.getStartDate().toString();
        this.endDate = partyReq.getEndDate().toString();
        this.hashTag = partyReq.getHashtag();
        this.siteUrls = partyReq.getPostPartyRestaurantReq().getSiteUrl();
        this.restaurantName = partyReq.getPostPartyRestaurantReq().getName();
        this.groupLeaderId = userId;
    }

    public FireBasePartyInfo(PatchPartyReq patchPartyReq) {
        this.groupName = patchPartyReq.getPartyName();
        this.groupDetail = patchPartyReq.getDetail();
        this.curNumberOfPeople = 1;
        this.maxNumberOfPeople = patchPartyReq.getMaxPeople();
        this.startDate = patchPartyReq.getStartDate().toString();
        this.endDate = patchPartyReq.getEndDate().toString();
    }
}
