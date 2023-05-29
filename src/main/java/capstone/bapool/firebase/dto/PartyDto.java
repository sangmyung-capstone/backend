package capstone.bapool.firebase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PartyDto {
    private FireBasePartyInfo fireBaseGroupInfo;
    private Long userId;
}
