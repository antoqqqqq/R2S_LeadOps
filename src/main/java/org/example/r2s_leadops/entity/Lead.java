package org.example.r2s_leadops.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "leads")
public class Lead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 20)
    @Column(name = "normalized_phone", length = 20)
    private String normalizedPhone;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interested_course_id")
    private Cours interestedCourse;

    @Size(max = 50)
    @Column(name = "current_level", length = 50)
    private String currentLevel;

    @Column(name = "study_need", length = Integer.MAX_VALUE)
    private String studyNeed;

    @Size(max = 100)
    @Column(name = "expected_enrollment_time", length = 100)
    private String expectedEnrollmentTime;

    @Size(max = 100)
    @Column(name = "preferred_contact_time", length = 100)
    private String preferredContactTime;

    @Size(max = 150)
    @Column(name = "school", length = 150)
    private String school;

    @Size(max = 100)
    @Column(name = "major", length = 100)
    private String major;

    @Size(max = 50)
    @Column(name = "year_of_study", length = 50)
    private String yearOfStudy;

    @Size(max = 100)
    @Column(name = "city", length = 100)
    private String city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private LeadSource source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private LeadStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @ColumnDefault("0")
    @Column(name = "total_score")
    private Integer totalScore;

    @Size(max = 20)
    @Column(name = "lead_temperature", length = 20)
    private String leadTemperature;

    @Column(name = "next_follow_up_at")
    private Instant nextFollowUpAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;


}