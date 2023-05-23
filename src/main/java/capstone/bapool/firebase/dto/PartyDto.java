package capstone.bapool.firebase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PartyDto {
    private FireBasePartyInfo fireBaseGroupInfo;
    private Long groupId;
    private Long userId;
}
