package capstone.bapool.model;

import net.bytebuddy.asm.Advice;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="party_id")
    private Long id;

    @Column(columnDefinition = "tinyint(4)")
    @ColumnDefault("false")
    private boolean isClose;

    private String name;

    @ColumnDefault("0")
    private int maxPeople;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String menu;

    private String detail;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
