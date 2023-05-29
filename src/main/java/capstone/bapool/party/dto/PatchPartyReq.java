package capstone.bapool.party.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatchPartyReq {
    @NotNull
    @JsonProperty("party_id")
    private Long partyId;

    @JsonProperty("party_name")
    @NotBlank
    private String partyName;

    @JsonProperty("max_people")
    @NotNull
    private Integer maxPeople;

    @JsonProperty("start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asis/Seoul")
    @NotNull
    private LocalDateTime startDate;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asis/Seoul")
    @JsonProperty("end_date")
    @NotNull
    private LocalDateTime endDate;

    @NotBlank
    private String menu;

    private String detail;
}
