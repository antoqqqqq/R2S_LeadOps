package org.example.r2s_leadops.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadUpdateRequest {

    private String careerGoal;

    private String currentLevel;

    private Boolean doNotContact;

    @Email(message = "Email không hợp lệ")
    private String email;

    @Size(max = 100, message = "fullName tối đa 100 ký tự")
    private String fullName;

    private String painPoint;

    @Size(max = 20, message = "phone tối đa 20 ký tự")
    private String phone;

    private String preferredChannel;

    private String startTimeline;
}