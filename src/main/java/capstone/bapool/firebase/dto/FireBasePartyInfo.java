package capstone.bapool.firebase.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private List<Integer> hasTag;


}
