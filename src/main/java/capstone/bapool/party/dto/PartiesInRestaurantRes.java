package capstone.bapool.party.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class PartiesInRestaurantRes {

    @JsonProperty(value = "restaurant_name")
    private String restaurantName;

    @JsonProperty(value = "parties")
    private List<PartyInfo> partyInfos;

    public PartiesInRestaurantRes(){
        this.partyInfos = new ArrayList<>();
    }

    public void setRestaurantName(String restaurantName){
        this.restaurantName = restaurantName;
    }

    public void setPartyInfos(List<PartyInfo> partyInfos){
        this.partyInfos = partyInfos;
    }

    public void addPartyInfos(PartyInfo partyInfo){
        partyInfos.add(partyInfo);
    }
}
