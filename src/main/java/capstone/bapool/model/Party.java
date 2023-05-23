package capstone.bapool.model;


import capstone.bapool.model.enumerate.PartyStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.hibernate.annotations.ColumnDefault;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Party {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "party")
    private List<PartyParticipant> partyParticipants = new ArrayList<PartyParticipant>();

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

    public void update(String name, Integer maxPeople, LocalDateTime startDate,
                       LocalDateTime endDate, String menu, String detail) {
        this.name = name;
        this.maxPeople = maxPeople;
        this.startDate = startDate;
        this.endDate = endDate;
        this.menu = menu;
        this.detail = detail;
    }

    public boolean isLastMember() {
        if (partyParticipants.size() == 1) {
            return true;
        }
        return false;
    }

    public void close() {
        partyStatus = PartyStatus.DEADLINE;
    }
}
