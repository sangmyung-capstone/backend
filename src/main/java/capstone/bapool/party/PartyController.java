package capstone.bapool.party;

import capstone.bapool.party.dto.PostPartyReq;
import capstone.bapool.party.dto.PostPartyRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/parties")
@RequiredArgsConstructor
public class PartyController {
    private final PartyService partyService;

    @PostMapping(value = "/userId")
    public ResponseEntity<PostPartyRes> create(
            @PathVariable Long userId,
            @Valid @RequestBody PostPartyReq postPartyReq
    )
    {
        Long partyId = partyService.save(postPartyReq, userId);
        return ResponseEntity.ok(new PostPartyRes(partyId));
    }
}
