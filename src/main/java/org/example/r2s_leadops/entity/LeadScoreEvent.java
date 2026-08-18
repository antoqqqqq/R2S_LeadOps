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
@Table(name = "lead_score_events")
public class LeadScoreEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "opportunity_id", nullable = false)
    private LeadOpportunity opportunity;

    @Size(max = 20)
    @NotNull
    @Column(name = "score_type", nullable = false, length = 20)
    private String scoreType;

    @Size(max = 50)
    @Column(name = "rule_code", length = 50)
    private String ruleCode;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @Column(name = "score_change", nullable = false)
    private Integer scoreChange;

    @NotNull
    @Column(name = "score_after", nullable = false)
    private Integer scoreAfter;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;


}