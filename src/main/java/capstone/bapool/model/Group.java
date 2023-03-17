package capstone.bapool.model;

import net.bytebuddy.asm.Advice;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
@Table(name="gruops")
@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="grp_id")
    private Long id;

    private boolean isClose;

    private String name;

    private int maxPeople;

    private int participants;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String menu;

    private String detail;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @CreationTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "resto_id")
    private Restaurant restaurant;
}
