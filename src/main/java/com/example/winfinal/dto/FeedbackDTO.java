package com.example.winfinal.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackDTO {
    private Integer feedbackId;
    private Integer customerId;
    private String customerFullName;
    private Integer bookingId;
    private Integer rating;
    private String comment;
    private LocalDateTime feedbackDate;
}
