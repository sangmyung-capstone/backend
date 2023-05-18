package capstone.bapool.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name= "party_and_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyAndUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_and_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;


    public PartyAndUser(User user, Party party) {
        this.user = user;
        this.party = party;
        user.getPartyAndUsers().add(this);
        party.getPartyAndUsers().add(this);
    }

}
