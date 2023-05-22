package capstone.bapool.model;

import capstone.bapool.model.enumerate.RoleType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name= "party_participant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyParticipant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_and_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(value = EnumType.STRING)
    private RoleType roleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;


    private PartyParticipant(User user, Party party, RoleType roleType) {
        this.user = user;
        this.party = party;
        user.getPartyParticipants().add(this);
        party.getPartyParticipants().add(this);
        this.roleType = roleType;
    }

    public static PartyParticipant makeMapping(User user, Party party, RoleType roleType) {
        return new PartyParticipant(user, party, roleType);
    }

    public boolean isLeader() {
        if (roleType == RoleType.LEADER) {
            return true;
        }
        return false;
    }

    public void becomeLeader() {
        this.roleType = RoleType.LEADER;
    }

}
