package org.example.r2s_leadops.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "lead_opportunities")
public class LeadOpportunity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "source_id")
    private LeadSource source;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "status_id")
    private LeadStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @Size(max = 50)
    @Column(name = "current_level", length = 50)
    private String currentLevel;

    @Column(name = "career_goal", length = Integer.MAX_VALUE)
    private String careerGoal;

    @Column(name = "pain_point", length = Integer.MAX_VALUE)
    private String painPoint;

    @Size(max = 100)
    @Column(name = "start_timeline", length = 100)
    private String startTimeline;

    @Size(max = 100)
    @Column(name = "preferred_channel", length = 100)
    private String preferredChannel;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "fit_score", nullable = false)
    private Integer fitScore;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "engagement_score", nullable = false)
    private Integer engagementScore;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "intent_score", nullable = false)
    private Integer intentScore;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "total_score", nullable = false)
    private Integer totalScore;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "do_not_contact", nullable = false)
    private Boolean doNotContact;

    @Column(name = "last_activity_at")
    private Instant lastActivityAt;

    @Column(name = "next_action_at")
    private Instant nextActionAt;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;


}