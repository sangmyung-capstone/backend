package capstone.bapool.party;

import capstone.bapool.model.Party;
import capstone.bapool.model.PartyParticipant;
import capstone.bapool.model.User;
import capstone.bapool.model.enumerate.RoleType;

import java.util.Optional;

public interface PartyParticipantCustomRepository {
    PartyParticipant findByPartyAndRoleType(Party party, RoleType roletype);
}
