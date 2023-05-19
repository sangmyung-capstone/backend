package capstone.bapool.model;

import capstone.bapool.model.enumerate.PartyStatus;
import lombok.Builder;
import net.bytebuddy.asm.Advice;
import org.hibernate.annotations.ColumnDefault;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Party {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "party")
    private List<PartyAndUser> partyAndUsers = new ArrayList<PartyAndUser>();

    @OneToMany(mappedBy = "party")
    private List<Hashtag> hashtags = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private PartyStatus partyStatus;

    private String name;

    @ColumnDefault("0")
    @Column(name = "max_people")
    private int maxPeople;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    private String menu;

    private String detail;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @Builder
    public Party(Restaurant restaurant, PartyStatus partyStatus, String name, int maxPeople, LocalDateTime startDate, LocalDateTime endDate, String menu, String detail) {
        this.restaurant = restaurant;
        this.partyStatus = partyStatus;
        this.name = name;
        this.maxPeople = maxPeople;
        this.startDate = startDate;
        this.endDate = endDate;
        this.menu = menu;
        this.detail = detail;
    }
}
