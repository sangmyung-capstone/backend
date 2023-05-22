package capstone.bapool.party;

import capstone.bapool.model.Party;
import capstone.bapool.model.PartyParticipant;
import capstone.bapool.model.enumerate.RoleType;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static capstone.bapool.model.QPartyAndUser.partyAndUser;

public class PartyParticipantCustomImpl implements PartyParticipantCustomRepository{
    private final JPAQueryFactory queryFactory;

    public PartyParticipantCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public PartyParticipant findByPartyAndRoleType(Party party, RoleType roletype) {
        return queryFactory
                .select(partyAndUser)
                .from(partyAndUser)
                .where(partyAndUser.party.eq(party)
                        .and(partyAndUser.roleType.eq(roletype)))
                .fetchFirst();
    }
}
