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

    @Size(max = 100)
    @Column(name = "manychat_id", length = 100)
    private String manychatId;

    @Size(max = 100)
    @Column(name = "messenger_id", length = 100)
    private String messengerId;

    @Size(max = 100)
    @Column(name = "zalo_uid", length = 100)
    private String zaloUid;

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

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;


}