package org.example.r2s_leadops.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "lead_activities")
public class LeadActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "lead_id")
    private Lead lead;

    @Size(max = 50)
    @Column(name = "activity_type", length = 50)
    private String activityType;

    @Column(name = "content", length = Integer.MAX_VALUE)
    private String content;

    @Size(max = 100)
    @Column(name = "result", length = 100)
    private String result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "old_status_id")
    private LeadStatus oldStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_status_id")
    private LeadStatus newStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by")
    private User performedBy;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "performed_at")
    private Instant performedAt;

    @Column(name = "next_follow_up_at")
    private Instant nextFollowUpAt;


}