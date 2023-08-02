package capstone.bapool.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetRestaurantBottomListReq {

    @JsonProperty("restaurant_ids")
    private List<Long> restaurantIdList;

    @Override
    public String toString(){
        String result = "[";

        for(Long id : restaurantIdList){
            result += id;
            result += ", ";
        }

        result = result.substring(0, result.lastIndexOf(','));
        result += "]";
        return result;
    }
}
