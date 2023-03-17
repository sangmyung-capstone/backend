package capstone.bapool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestDto {

    private String name;
    @JsonProperty(value = "temp_var")
    private String tempVar;
    private List<Integer> list;
}
